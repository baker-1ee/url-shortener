package com.trial.urlshortener.service;

import com.trial.urlshortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlHitCountFlushService {
    private final UrlMappingRepository repository;

    @Transactional
    public void flush(String shortCode, Long deltaHitCount) {

        log.info("[WriteBehind] flush shortCode={}, deltaHitCount={}", shortCode, deltaHitCount);

        repository.findByShortCode(shortCode)
                .ifPresent(db -> {
                    db.increaseHitCount(deltaHitCount);
                    repository.save(db);
                });
    }
}
