package com.trial.urlshortener.entity;

import com.trial.urlshortener.enums.ShortUrlType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_mapping")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortUrlEntity {
    @Id
    @Column
    private String shortCode;

    @Column
    @Enumerated(EnumType.STRING)
    private ShortUrlType urlType;

    @Column(length = 2048)
    private String originUrl;

    @Column
    private Long hitCount;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public static ShortUrlEntity create(
            String shortCode,
            String originUrl,
            ShortUrlType urlType
    ) {
        return new ShortUrlEntity(
                shortCode,
                urlType,
                originUrl,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
