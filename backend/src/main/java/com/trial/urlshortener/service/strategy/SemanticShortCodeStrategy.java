package com.trial.urlshortener.service.strategy;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SemanticShortCodeStrategy implements ShortCodeStrategy {

    private static final int MIN_LENGTH = 5;

    @Override
    public List<String> generateCandidates(String originalUrl, int length) {
        URI uri = URI.create(originalUrl);

        String host = normalizeHost(uri.getHost());
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

        // 3) host 기반 후보
        candidates.addAll(makeVariations(host));

        // 4) 최소 길이 이상으로 강제 보장 + distinct
        return candidates.stream()
                .filter(s -> !s.isEmpty())
                .map(s -> padToLength(s, Math.max(MIN_LENGTH, length)))
                .distinct()
                .collect(Collectors.toList());
    }

    // ------------------------------
    // Host normalize (www 제거 + .com 등 제거)
    // ------------------------------
    String normalizeHost(String host) {
        if (host == null) return "";

        host = host.toLowerCase();

        // 1) www 제거
        host = host.replaceFirst("^www\\.", "");

        // 2) 도메인 확장자 제거
        host = host.replaceAll("\\.(com|co\\.kr|net|org)$", "");

        // 3) 특정 문자 제거
        host = host.replaceAll("[^A-Za-z0-9]", "");

        return host;
    }

    List<String> extractSegments(String path) {
        if (path == null) return List.of();

        return Arrays.stream(path.split("/"))
                .map(s -> s.replaceAll("[^A-Za-z0-9]", "").toLowerCase())
                .filter(s -> !s.isEmpty())
                .toList();
    }

    // 단어 기반 variation (길이 제한 없음)
    List<String> makeVariations(String s) {
        List<String> list = new ArrayList<>();

        if (s.length() >= 3) list.add(s.substring(0, 3));
        if (s.length() >= 4) list.add(s.substring(0, 4));
        if (s.length() >= 5) list.add(s.substring(0, 5));
        if (s.length() >= 6) list.add(s.substring(0, 6));

        // 원본 문자열도 추가
        list.add(s);

        return list;
    }

    List<String> combine(String host, String last) {
        List<String> list = new ArrayList<>();

        if (!host.isEmpty() && !last.isEmpty()) {
            list.add(host.substring(0, Math.min(3, host.length())) +
                    last.substring(0, Math.min(3, last.length())));

            list.add(host.substring(0, Math.min(2, host.length())) +
                    last.substring(0, Math.min(4, last.length())));
        }

        return list;
    }

    // ★ 최소 길이 보장 + 규칙적 확장
    private String padToLength(String s, int length) {
        if (s.length() >= length) {
            return s.substring(0, length);
        }

        StringBuilder sb = new StringBuilder(s);

        // 부족한 길이만큼 반복 패턴을 추가
        while (sb.length() < length) {
            sb.append(s.charAt(sb.length() % s.length()));
        }

        return sb.toString();
    }
}
