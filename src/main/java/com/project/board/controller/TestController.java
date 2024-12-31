package com.project.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint. No authentication required.";
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "This is a protected endpoint. Authentication successful.";
    }
}
