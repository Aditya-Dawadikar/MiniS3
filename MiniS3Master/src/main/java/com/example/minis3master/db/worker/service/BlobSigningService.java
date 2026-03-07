package com.example.minis3master.db.worker.service;

import com.example.minis3master.db.worker.entity.BlobReplicationEntity;
import com.example.minis3master.db.worker.entity.WorkerEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BlobSigningService {
    private static final int DEFAULT_URL_COUNT = 3;
    private static final long DEFAULT_EXPIRY_SECONDS = 300;

    // Temporary hardcoded keys; later this should come from a key management service.
    private static final Map<String, String> WORKER_SIGNING_KEYS = Map.of(
            "worker-a", "mini-s3-dev-key-worker-a",
            "worker-b", "mini-s3-dev-key-worker-b",
            "worker-c", "mini-s3-dev-key-worker-c"
    );

    private final BlobReplicationService blobReplicationService;
    private final WorkerPersistenceService workerPersistenceService;
    private final ReplicaSelectionService replicaSelectionService;

    public record SignedUrls(List<String> urls, long expiresAt) {
    }

    public BlobSigningService(BlobReplicationService blobReplicationService,
                              WorkerPersistenceService workerPersistenceService,
                              ReplicaSelectionService replicaSelectionService) {
        this.blobReplicationService = blobReplicationService;
        this.workerPersistenceService = workerPersistenceService;
        this.replicaSelectionService = replicaSelectionService;
    }

    public SignedUrls generateSignedGetUrls(String fileId) {
        long expiresAt = Instant.now().getEpochSecond() + DEFAULT_EXPIRY_SECONDS;
        List<BlobReplicationEntity> replicas = blobReplicationService.getBlobReplicationsByFileId(fileId);
        List<String> urls = new ArrayList<>();

        for (BlobReplicationEntity replica : replicas) {
            if (Boolean.TRUE.equals(replica.getIsDeleted())) {
                continue;
            }

            Optional<WorkerEntity> worker = workerPersistenceService.getWorkerById(replica.getWorkerId());
            if (worker.isEmpty()) {
                continue;
            }

            String token = buildToken(replica.getWorkerId(), fileId, "GET", expiresAt);
            String signedUrl = normalizeBaseUrl(worker.get().getUrl())
                    + "/api/blobs/download?key=" + urlEncode(fileId)
                    + "&token=" + urlEncode(token);
            urls.add(signedUrl);

            if (urls.size() >= DEFAULT_URL_COUNT) {
                break;
            }
        }

        return new SignedUrls(urls, expiresAt);
    }

    public SignedUrls generateSignedPostUrls(String fileId) {
        long expiresAt = Instant.now().getEpochSecond() + DEFAULT_EXPIRY_SECONDS;
        List<WorkerEntity> workers = replicaSelectionService.getUploadCandidatesByAvailableSpace(DEFAULT_URL_COUNT);

        List<String> urls = new ArrayList<>();

        for (WorkerEntity worker : workers) {
            String token = buildToken(worker.getId(), fileId, "POST", expiresAt);
            String signedUrl = normalizeBaseUrl(worker.getUrl())
                    + "/api/blobs?key=" + urlEncode(fileId)
                    + "&token=" + urlEncode(token);
            urls.add(signedUrl);

            if (urls.size() >= DEFAULT_URL_COUNT) {
                break;
            }
        }

        return new SignedUrls(urls, expiresAt);
    }

    private String buildToken(String workerId, String fileId, String method, long expiresAt) {
        String payload = workerId + "|" + fileId + "|" + method + "|" + expiresAt;
        String payloadB64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signatureB64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(hmacSha256(payload, resolveWorkerKey(workerId)));
        return payloadB64 + "." + signatureB64;
    }

    private byte[] hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to generate token signature", ex);
        }
    }

    private String resolveWorkerKey(String workerId) {
        return WORKER_SIGNING_KEYS.getOrDefault(workerId, "mini-s3-dev-key-default");
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String normalizeBaseUrl(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
