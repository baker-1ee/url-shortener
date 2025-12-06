package com.trial.urlshortener.service.strategy;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SemanticShortCodeStrategy implements ShortCodeStrategy {

    @Override
    public String generate(String originalUrl) {
        URI uri = URI.create(originalUrl);

        String host = uri.getHost();
        String path = uri.getPath();

        // host 약자 추출 예시 (bangbangcapital → bbcpt)
        String hostAbbr = abbreviate(host);

        // path 기반 의미 추출 예시 (/events/2025/christmas → 25chr)
        String semantic = extractMeaning(path);

        // 1순위: path 기반 의미
        if (semantic != null) {
            return semantic;
        }
        // 2순위: host 기반 의미
        return hostAbbr;
    }

    private String abbreviate(String host) {
        if (host == null) return "host";
        host = host.replaceAll("[^A-Za-z]", "");
        return host.length() <= 5 ? host : host.substring(0, 5);
    }

    private String extractMeaning(String path) {
        if (path == null) return null;

        String[] segments = path.split("/");
        if (segments.length == 0) return null;

        String last = segments[segments.length - 1];
        if (last.isEmpty()) return null;

        String alpha = last.replaceAll("[^A-Za-z]", "");
        if (alpha.length() < 3) return null;

        return alpha.substring(0, 5).toLowerCase();
    }
}
