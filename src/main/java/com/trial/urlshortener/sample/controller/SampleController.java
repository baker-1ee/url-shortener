package com.trial.urlshortener.sample.controller;

import com.trial.urlshortener.sample.entity.Sample;
import com.trial.urlshortener.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService service;

    @PostMapping
    public ResponseEntity<Sample> create(@RequestBody String message) {
        return ResponseEntity.ok(service.create(message));
    }

    @GetMapping
    public ResponseEntity<List<Sample>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sample> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}