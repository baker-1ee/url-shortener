package com.trial.urlshortener.service;

import com.trial.urlshortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitCountFlushService {
    private final HitCountCounterService counterService;
    private final UrlMappingRepository repository;

    @Transactional
    @Scheduled(fixedRate = 10_000) // 10초마다 flush
    public void flush() {
        Map<String, Long> deltas = counterService.fetchAndClearAll();
        if (deltas.isEmpty()) {
            return;
        }

        log.info("[HitCountFlush] Flushing {} shortCodes", deltas.size());

        deltas.forEach((shortCode, delta) -> {
            int updated = repository.increaseHitCount(shortCode, delta, LocalDateTime.now());
            log.info("[HitCountFlush] shortCode={}, delta={}, updatedRows={}", shortCode, delta, updated);
        });
    }
}
