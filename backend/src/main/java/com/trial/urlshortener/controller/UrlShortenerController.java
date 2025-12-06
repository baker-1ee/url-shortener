package com.trial.urlshortener.controller;

import com.trial.urlshortener.dto.CreateShortUrlRequest;
import com.trial.urlshortener.dto.CreateShortUrlResponse;
import com.trial.urlshortener.service.ShortUrlCreateService;
import com.trial.urlshortener.service.ShortUrlResolveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {
    private final ShortUrlCreateService createService;
    private final ShortUrlResolveService resolveService;

    @PostMapping("/short-urls")
    public ResponseEntity<CreateShortUrlResponse> create(@Valid @RequestBody CreateShortUrlRequest request) {
        CreateShortUrlResponse response = createService.create(request);

        return ResponseEntity
                .created(URI.create(response.getShortUrl()))
                .body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originUrl = resolveService.resolve(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originUrl));

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }
}
