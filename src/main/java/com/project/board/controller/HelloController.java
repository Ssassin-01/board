package com.project.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HelloController {
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World";
    }
}
