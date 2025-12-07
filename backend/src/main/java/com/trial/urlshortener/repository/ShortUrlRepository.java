package com.trial.urlshortener.repository;

import com.trial.urlshortener.entity.ShortUrlEntity;
import com.trial.urlshortener.enums.ShortUrlType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, String> {

    Optional<ShortUrlEntity> findByOriginUrlAndUrlType(String originUrl, ShortUrlType urlType);

    Optional<ShortUrlEntity> findByShortCode(String shortCode);

    @Modifying
    @Query("update ShortUrlEntity u " +
            "set u.hitCount = u.hitCount + :delta, u.updatedAt = :updatedAt " +
            "where u.shortCode = :shortCode")
    int increaseHitCount(String shortCode, Long delta, LocalDateTime updatedAt);
}
