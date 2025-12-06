package com.trial.urlshortener.service;

import com.trial.urlshortener.dto.CreateShortUrlRequest;
import com.trial.urlshortener.dto.CreateShortUrlResponse;
import com.trial.urlshortener.entity.UrlMappingEntity;
import com.trial.urlshortener.enums.ShortUrlType;
import com.trial.urlshortener.repository.UrlMappingRepository;
import com.trial.urlshortener.service.strategy.ShortCodeStrategy;
import com.trial.urlshortener.service.strategy.ShortCodeStrategyFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlCreateService {
    @Value("${shortener.base-url}")
    private String baseUrl;
    private final UrlMappingRepository repository;
    private final ShortCodeStrategyFactory strategyFactory;

    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 10;

    @Transactional
    public CreateShortUrlResponse create(CreateShortUrlRequest request) {
        String originUrl = request.getOriginUrl();
        ShortUrlType shortUrlType = request.getShortUrlType();
        
        try {
            return repository.findByOriginUrlAndUrlType(originUrl, shortUrlType)
                    .map(entity -> CreateShortUrlResponse.of(entity, baseUrl))
                    .orElseGet(() -> createNewShortUrl(request));
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("originUrl must be unique, but multiple records found: " + originUrl, e);
        }
    }

    private CreateShortUrlResponse createNewShortUrl(CreateShortUrlRequest request) {
        ShortCodeStrategy strategy = strategyFactory.getStrategy(request.getShortUrlType());

        int length = MIN_LENGTH;

        while (length <= MAX_LENGTH) {
            List<String> candidates = strategy.generateCandidates(request.getOriginUrl(), length);
            strategy.generateCandidates(request.getOriginUrl(), length);

            Collections.shuffle(candidates);

            for (String candidate : candidates) {
                if (!repository.existsById(candidate)) {
                    UrlMappingEntity entity = UrlMappingEntity.create(candidate, request.getOriginUrl(), request.getShortUrlType());
                    repository.save(entity);
                    return CreateShortUrlResponse.of(entity, baseUrl);
                }
            }

            // 후보 모두는 충돌 → 길이 증가
            length++;
        }

        throw new IllegalStateException("Failed to generate unique shortCode within allowed length");
    }

}
