package com.trial.urlshortener.service;

import com.trial.urlshortener.dto.CreateShortUrlRequest;
import com.trial.urlshortener.dto.CreateShortUrlResponse;
import com.trial.urlshortener.entity.UrlMappingEntity;
import com.trial.urlshortener.repository.UrlMappingRepository;
import com.trial.urlshortener.service.strategy.ShortCodeStrategy;
import com.trial.urlshortener.service.strategy.ShortCodeStrategyFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    @Value("${shortener.base-url}")
    private String baseUrl;
    private final UrlMappingRepository repository;
    private final ShortCodeStrategyFactory strategyFactory;

    @Transactional
    public CreateShortUrlResponse create(CreateShortUrlRequest request) {
        String originUrl = request.getOriginUrl();
        try {
            return repository.findByOriginUrl(originUrl)
                    .map(entity -> CreateShortUrlResponse.of(entity, baseUrl))
                    .orElseGet(() -> createNewShortUrl(request));
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("originUrl must be unique, but multiple records found: " + originUrl, e);
        }
    }

    private CreateShortUrlResponse createNewShortUrl(CreateShortUrlRequest request) {
        ShortCodeStrategy strategy = strategyFactory.getStrategy(request.getShortUrlType());
        String shortCode = strategy.generate(request.getOriginUrl());
        UrlMappingEntity newUrlMapping = UrlMappingEntity.create(shortCode, request.getOriginUrl());
        repository.save(newUrlMapping);
        return CreateShortUrlResponse.of(newUrlMapping, baseUrl);
    }

    @Transactional
    public String resolve(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(entity -> {
                    entity.increaseHitCount(1L);
                    repository.save(entity);
                    return entity.getOriginUrl();
                })
                .orElseThrow(() -> new IllegalArgumentException("ShortCode not found: " + shortCode));
    }
}
