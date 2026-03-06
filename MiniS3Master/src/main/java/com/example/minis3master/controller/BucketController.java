package com.example.minis3master.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BucketController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "MiniS3Master"));
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<String>> listBuckets() {
        // Placeholder: return a static list so the CLI can validate connectivity
        return ResponseEntity.ok(List.of("bucket-a", "bucket-b"));
    }
}
