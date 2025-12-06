package com.trial.urlshortener.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ShortUrlType {

    RANDOM("무작위 short URL"),
    SEMANTIC("원본 URL의 내용을 담는 의미 있는 short URL");

    private final String description;
}
