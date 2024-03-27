package org.example.controller;

import org.example.dto.RequestDto;
import org.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @Autowired
    AppService service;

    @PostMapping("/api/createNewPost")
    public ResponseEntity<?> createNewPost(@RequestBody RequestDto requestDto) {
        return new ResponseEntity<>(service.serve(requestDto), HttpStatus.OK);
    }
}
