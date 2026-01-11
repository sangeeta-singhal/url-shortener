package com.example.urlShortener.service;

import java.security.SecureRandom;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.urlShortener.model.UrlMapping;
import com.example.urlShortener.repository.UrlMappingRepository;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_CODE_LENGTH = 6;

    private final UrlMappingRepository repository;
    private final SecureRandom random = new SecureRandom();

    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.repository = urlMappingRepository;
    }

    @Override
    public String shortenenUrl(String originalUrl) {
        Optional<UrlMapping> existing = repository.findByOriginalUrl(originalUrl);
        if(existing.isPresent()) {
            return existing.get().getShortCode();
        }
        
        String shortCode = generateShortCode();

        while (repository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        UrlMapping mapping = new UrlMapping(originalUrl, shortCode);
        repository.save(mapping);

        return shortCode;
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(UrlMapping::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("Short URL not Found"));
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
        }

        return sb.toString();
    }

}
