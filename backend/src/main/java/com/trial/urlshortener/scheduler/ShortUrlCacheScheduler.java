package com.trial.urlshortener.scheduler;

import com.github.benmanes.caffeine.cache.Cache;
import com.trial.urlshortener.dto.CachedUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShortUrlCacheScheduler {

    private final Cache<String, CachedUrl> shortUrlCache;

    // 5초마다 캐시 정리 (TTL 만료된 항목 제거)
    @Scheduled(fixedRate = 10000)
    public void periodicCleanup() {
        log.info("[Scheduler] Running Caffeine cleanup()");
        shortUrlCache.cleanUp();
    }
}
