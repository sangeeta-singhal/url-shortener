package com.example.urlShortener.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.urlShortener.model.UrlMapping;
import com.example.urlShortener.repository.UrlMappingRepository;
import com.example.urlShortener.util.Base62Encoder;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerServiceImpl.class);
    private final UrlMappingRepository repository;
    private static final Base62Encoder encoder = new Base62Encoder();

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

        String shortCode = encoder.generateShortCode();

        while (repository.findByShortCode(shortCode).isPresent()) {
            shortCode = encoder.generateShortCode();
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
}
