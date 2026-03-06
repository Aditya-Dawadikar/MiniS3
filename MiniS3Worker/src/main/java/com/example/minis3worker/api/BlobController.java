package com.example.minis3worker.api;

import com.example.minis3worker.storage.DiskManager;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blobs")
public class BlobController {
    private final DiskManager diskManager;

    private record ApiResponse<T>(boolean success, String message, T data) {
    }

    private record ErrorResponse(boolean success, String message, String error, String path, Instant timestamp) {
    }

    public BlobController(DiskManager diskManager) {
        this.diskManager = diskManager;
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key is required");
        }
        if (key.contains("..") || key.contains("/") || key.contains("\\")) {
            throw new IllegalArgumentException("Invalid key");
        }
    }

    // Upload: POST /api/blobs?key={key}
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> upload(@RequestParam String key,
                                                                    HttpServletRequest request) throws IOException {
        validateKey(key);
        diskManager.save(key, request.getInputStream());

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                true,
                "Blob uploaded successfully",
                Map.of("key", key)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Download: GET /api/blobs/download?key={key}
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam String key) throws IOException {
        validateKey(key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(diskManager.read(key)));
    }

    // Delete: DELETE /api/blobs?key={key}
    @DeleteMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> delete(@RequestParam String key) throws IOException {
        validateKey(key);

        diskManager.delete(key);

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                true,
                "Blob deleted successfully",
                Map.of("key", key)
        );
        return ResponseEntity.ok(response);
    }

    // List: GET /api/blobs
    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> list() throws IOException {
        List<String> files = diskManager.list();

        ApiResponse<List<String>> response = new ApiResponse<>(
                true,
                "Blob list fetched successfully",
                files
        );
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex,
                                                          HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                "Request validation failed",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchFileException ex,
                                                        HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                "Blob not found",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIo(IOException ex,
                                                  HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                "Storage operation failed",
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
