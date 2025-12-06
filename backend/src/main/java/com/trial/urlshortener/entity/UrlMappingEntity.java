package com.trial.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mapping")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlMappingEntity {
    @Id
    @Column
    private String shortCode;

    @Column
    private String originUrl;

    @Column
    private Long hitCount;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public static UrlMappingEntity create(String shortCode, String originUrl) {
        return new UrlMappingEntity(
                shortCode,
                originUrl,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public void increaseHitCount(Long delta) {
        hitCount += delta;
        updatedAt = LocalDateTime.now();
    }
}
