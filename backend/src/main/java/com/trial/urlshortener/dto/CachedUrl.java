package com.trial.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CachedUrl {
    private String originUrl;
    private Long deltaHitCount;

    public void increaseHitCount() {
        deltaHitCount++;
    }
}
