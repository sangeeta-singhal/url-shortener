package com.example.urlShortener.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.urlShortener.dto.ShortenUrlRequest;
import com.example.urlShortener.dto.ShortenUrlResponse;
import com.example.urlShortener.service.UrlShortenerService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService service;
    private final String baseUrl;

    public UrlShortenerController(UrlShortenerService service, @Value("${app.base-url}") String baseUrl) {
        this.service = service;
        this.baseUrl = baseUrl;
    }

    @PostMapping("/api/urls")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        String shortCode = service.shortenenUrl(request.getOriginalUrl());
        String shortUrl = baseUrl + shortCode;

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ShortenUrlResponse(shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        String originalUrl = service.getOriginalUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
