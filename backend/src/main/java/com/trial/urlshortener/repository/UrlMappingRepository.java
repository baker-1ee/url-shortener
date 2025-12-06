package com.trial.urlshortener.repository;

import com.trial.urlshortener.entity.UrlMappingEntity;
import com.trial.urlshortener.enums.ShortUrlType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMappingEntity, String> {

    Optional<UrlMappingEntity> findByOriginUrlAndUrlType(String originUrl, ShortUrlType urlType);

    Optional<UrlMappingEntity> findByShortCode(String shortCode);
}
