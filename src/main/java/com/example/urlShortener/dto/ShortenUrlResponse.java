package com.example.urlShortener.dto;

public class ShortenUrlResponse {
    private String shortUrl;

    public ShortenUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
