package com.trial.urlshortener.repository;

import com.trial.urlshortener.entity.UrlMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMappingEntity, String> {


    Optional<UrlMappingEntity> findByOriginUrl(String originUrl);

    Optional<UrlMappingEntity> findByShortCode(String shortCode);
}
