package com.example.urlShortener.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public class ShortenUrlRequest {
    
    @NotBlank(message = "Original URL must not be empty")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    public ShortenUrlRequest() {}

    public ShortenUrlRequest(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
