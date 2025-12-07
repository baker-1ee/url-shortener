package com.trial.urlshortener.repository;

import com.trial.urlshortener.entity.UrlMappingEntity;
import com.trial.urlshortener.enums.ShortUrlType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMappingEntity, String> {

    Optional<UrlMappingEntity> findByOriginUrlAndUrlType(String originUrl, ShortUrlType urlType);

    Optional<UrlMappingEntity> findByShortCode(String shortCode);

    @Modifying
    @Query("update UrlMappingEntity u " +
            "set u.hitCount = u.hitCount + :delta, u.updatedAt = :updatedAt " +
            "where u.shortCode = :shortCode")
    int increaseHitCount(String shortCode, Long delta, LocalDateTime updatedAt);
}
