package com.example.urlShortener.service;

public interface UrlShortenerService {
    String shortenenUrl(String originalUrl);

    String getOriginalUrl(String shortCode);
}
