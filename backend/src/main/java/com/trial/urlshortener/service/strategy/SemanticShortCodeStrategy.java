package com.trial.urlshortener.service.strategy;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SemanticShortCodeStrategy implements ShortCodeStrategy {

    @Override
    public List<String> generateCandidates(String originalUrl, int length) {
        URI uri = URI.create(originalUrl);

        String host = normalize(uri.getHost());
        List<String> segments = extractSegments(uri.getPath());

        List<String> candidates = new ArrayList<>();

        // 1) path 마지막 segment 기반 후보
        if (!segments.isEmpty()) {
            String last = segments.get(segments.size() - 1);
            candidates.addAll(makeVariations(last));
        }

        // 2) path + host 조합
        if (!segments.isEmpty()) {
            candidates.addAll(combine(host, segments.get(segments.size() - 1)));
        }

        // 3) host 기반 요약 후보
        candidates.addAll(makeVariations(host));

        // 4) 길이로 자르기 + 필터링
        return candidates.stream()
                .filter(s -> s.length() >= 3)
                .map(s -> s.substring(0, Math.min(length, s.length())))
                .distinct()
                .collect(Collectors.toList());
    }

    private String normalize(String s) {
        return s == null ? "" : s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    }

    private List<String> extractSegments(String path) {
        if (path == null) return List.of();
        return Arrays.stream(path.split("/"))
                .map(this::normalize)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // 단어 변형을 여러 개 생성
    private List<String> makeVariations(String s) {
        List<String> list = new ArrayList<>();
        if (s.length() >= 3) list.add(s.substring(0, 3));
        if (s.length() >= 4) list.add(s.substring(0, 4));
        if (s.length() >= 5) list.add(s.substring(0, 5));
        if (s.length() >= 6) list.add(s.substring(0, 6));
        list.add(s);
        return list;
    }

    private List<String> combine(String host, String last) {
        List<String> list = new ArrayList<>();
        if (!host.isEmpty() && !last.isEmpty()) {
            list.add(host.substring(0, Math.min(3, host.length())) +
                    last.substring(0, Math.min(3, last.length())));
            list.add(host.substring(0, Math.min(2, host.length())) +
                    last.substring(0, Math.min(4, last.length())));
        }
        return list;
    }
}
