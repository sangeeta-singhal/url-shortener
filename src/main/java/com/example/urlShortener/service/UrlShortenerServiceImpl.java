package com.example.urlShortener.service;

import java.security.SecureRandom;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.urlShortener.model.UrlMapping;
import com.example.urlShortener.repository.UrlMappingRepository;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);

    private final UrlMappingRepository repository;
    private final SecureRandom random = new SecureRandom();

    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.repository = urlMappingRepository;
    }

    @Override
    public String shortenenUrl(String originalUrl) {
        logger.info("Request received to shorten URL: {}", originalUrl);
        Optional<UrlMapping> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }

        String shortCode = generateShortCode();

        while (repository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        logger.info("Generated shot code {} for URL {}", shortCode, originalUrl);
        UrlMapping mapping = new UrlMapping(originalUrl, shortCode);
        repository.save(mapping);

        return shortCode;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        logger.info("Redirect request received for short code: {}", shortCode);

        return repository.findByShortCode(shortCode)
                .map(mapping -> {
                    logger.info("Short code {} resolved to URL {}", shortCode, mapping.getOriginalUrl());
                    return mapping.getOriginalUrl();
                })
                .orElseThrow(() -> {
                    logger.warn("No URL mapping found for short code {}", shortCode);
                    return new RuntimeException("Short URL not Found");
                });
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return sb.toString();
    }

}
