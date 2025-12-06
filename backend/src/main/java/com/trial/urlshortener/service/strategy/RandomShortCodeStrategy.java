package com.trial.urlshortener.service.strategy;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class RandomShortCodeStrategy implements ShortCodeStrategy {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final SecureRandom random = new SecureRandom();

    // 후보 개수 (1차 시도 시 여러 후보 제공)
    private static final int CANDIDATE_COUNT = 8;

    @Override
    public List<String> generateCandidates(String originalUrl, int length) {
        List<String> candidates = new ArrayList<>();

        for (int i = 0; i < CANDIDATE_COUNT; i++) {
            candidates.add(generateRandomCode(length));
        }

        return candidates;
    }

    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return sb.toString();
    }

}
