package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// En tu controller existente o crea uno nuevo
@RestController
@RequestMapping("/api/v1/")
//@CrossOrigin(origins = "*") // Tu CORS existente
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", Instant.now().toString());
        response.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() / 1000 + "s");

        // Memoria usada (opcional)
        Runtime runtime = Runtime.getRuntime();
        response.put("memoryUsedMB", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);

        return ResponseEntity.ok(response);
    }
}