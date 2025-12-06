package com.trial.urlshortener.service.strategy;

public interface ShortCodeStrategy {
    String generate(String originUrl);
}
