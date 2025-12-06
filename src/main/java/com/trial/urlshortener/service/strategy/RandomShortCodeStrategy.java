package com.trial.urlshortener.service.strategy;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomShortCodeStrategy implements ShortCodeStrategy {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 10;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate(String originalUrl) {
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return sb.toString();
    }
}
