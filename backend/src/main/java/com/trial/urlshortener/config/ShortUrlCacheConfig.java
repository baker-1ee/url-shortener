package com.trial.urlshortener.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.trial.urlshortener.dto.CachedUrl;
import com.trial.urlshortener.service.ShortUrlHitCountFlushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ShortUrlCacheConfig {
    private final ShortUrlHitCountFlushService flushService;

    @Bean
    public Cache<String, CachedUrl> shortUrlCache() {

        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .removalListener((String key, CachedUrl value, RemovalCause cause) -> {
                    if (value != null) {
                        flushService.flush(key, value.getDeltaHitCount());
                    }
                })
                .build();
    }
}
