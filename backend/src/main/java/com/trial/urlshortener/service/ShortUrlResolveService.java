package com.trial.urlshortener.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.trial.urlshortener.dto.CachedUrl;
import com.trial.urlshortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlResolveService {
    private final Cache<String, CachedUrl> shortUrlCache;
    private final UrlMappingRepository repository;

    public String resolve(String shortCode) {
        CachedUrl cached = shortUrlCache.get(
                shortCode,
                key -> repository.findByShortCode(key)
                        .map(entity -> new CachedUrl(entity.getOriginUrl(), 0L))
                        .orElse(null)
        );

        if (cached == null)
            throw new IllegalArgumentException("ShortCode not found: " + shortCode);

        cached.increaseHitCount();
        return cached.getOriginUrl();
    }
}
