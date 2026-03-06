package com.example.minis3worker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WorkerController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "MiniS3Worker"));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<String>> listTasks() {
        // Placeholder: return a static list for testing connectivity
        return ResponseEntity.ok(List.of("replicate-bucket-a", "cleanup-temp"));
    }
}
