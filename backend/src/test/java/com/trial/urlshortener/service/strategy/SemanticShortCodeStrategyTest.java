package com.trial.urlshortener.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SemanticShortCodeStrategyTest {

    private SemanticShortCodeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SemanticShortCodeStrategy();
    }

    // ====================================================
    // normalizeHost() 테스트
    // ====================================================
    @Test
    @DisplayName("normalizeHost - www 제거 + 도메인 제거 + 영숫자만 남김")
    void testNormalizeHost() {
        assertThat(strategy.normalizeHost("www.naver.com"))
                .isEqualTo("naver");

        assertThat(strategy.normalizeHost("www.google.co.kr"))
                .isEqualTo("google");

        assertThat(strategy.normalizeHost("some-site_name123.org"))
                .isEqualTo("somesitename123");
    }

    // ====================================================
    // extractSegments() 테스트
    // ====================================================
    @Test
    @DisplayName("extractSegments - path를 의미 있는 segment 로 분리")
    void testExtractSegments() {
        List<String> segments = strategy.extractSegments("/news/article/CPMNMN0101.hc");

        assertThat(segments)
                .containsExactly("news", "article", "cpmnmn0101hc");
    }

    // ====================================================
    // combine() 테스트
    // ====================================================
    @Test
    @DisplayName("combine - host와 path 마지막 segment 조합 후보 생성")
    void testCombine() {
        List<String> combined = strategy.combine("naver", "article");

        assertThat(combined)
                .containsExactly(
                        "navart",  // host 3 + last 3
                        "naarti"   // host 2 + last 4
                );
    }

    // ====================================================
    // generateCandidates() 테스트
    // ====================================================
    @Test
    @DisplayName("generateCandidates - URL 기반 의미 후보 생성")
    void testGenerateCandidates() {
        String url = "https://www.naver.com/news/article/12345";

        List<String> candidates = strategy.generateCandidates(url, 5);

        assertThat(candidates).isNotEmpty();

        // 모든 후보가 최소 길이를 만족해야 한다
        assertThat(candidates).allMatch(c -> c.length() >= 5);

        // host 기반 후보 포함
        assertThat(candidates).anyMatch(code -> code.startsWith("nav"));

        // last segment "12345" 기반 후보 포함 (예: 12345 → 12345, 12312 등)
        assertThat(candidates).anyMatch(code -> code.matches("123.*"));
    }
}
