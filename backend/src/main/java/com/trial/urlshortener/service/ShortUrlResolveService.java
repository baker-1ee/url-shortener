package com.trial.urlshortener.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.trial.urlshortener.entity.ShortUrlEntity;
import com.trial.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShortUrlResolveService {
    private final Cache<String, String> shortUrlCache;
    private final ShortUrlRepository repository;
    private final HitCountCounterService hitCountCounterService;

    @Transactional(readOnly = true)
    public String resolve(String shortCode) {
        // 1) URL 매핑 조회 (Caffeine + DB)
        String originUrl = shortUrlCache.get(
                shortCode,
                key -> repository.findByShortCode(key)
                        .map(ShortUrlEntity::getOriginUrl)
                        .orElse(null)
        );

        if (originUrl == null) {
            throw new IllegalArgumentException("ShortCode not found: " + shortCode);
        }
        // 2) 방문 횟수는 Redis 에 저장
        hitCountCounterService.increment(shortCode);

        return originUrl;
    }
}
