package com.SCM.IOMS.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // API này sẽ nằm ở đường dẫn: http://localhost:8080/api/ping
    @GetMapping("/ping")
    public Map<String, String> pingSystem() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Backend Spring Boot đã kết nối thành công!");
        return response;
    }
}