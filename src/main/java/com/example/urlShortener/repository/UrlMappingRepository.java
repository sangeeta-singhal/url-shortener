package com.example.urlShortener.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.urlShortener.model.UrlMapping;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    Optional<UrlMapping> findByOriginalUrl(String originalUrl);
    
}
