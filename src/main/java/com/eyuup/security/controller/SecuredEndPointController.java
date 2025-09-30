package com.eyuup.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/secured")
public class SecuredEndPointController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
            return ResponseEntity.ok("Hello from secured endpoint");
    }
    


}
