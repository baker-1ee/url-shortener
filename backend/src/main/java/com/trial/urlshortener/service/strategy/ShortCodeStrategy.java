package com.trial.urlshortener.service.strategy;

import java.util.List;

public interface ShortCodeStrategy {
    List<String> generateCandidates(String originUrl, int length);
}
