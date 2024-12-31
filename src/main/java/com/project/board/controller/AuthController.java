package com.project.board.controller;

import com.project.board.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/token")
    public String getToken(@RequestParam String username) {
        return jwtTokenProvider.generateToken(username);
    }
}
