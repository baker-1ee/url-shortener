package com.trial.urlshortener.service.strategy;

import com.trial.urlshortener.enums.ShortUrlType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortCodeStrategyFactory {

    private final RandomShortCodeStrategy randomStrategy;
    private final SemanticShortCodeStrategy semanticStrategy;

    public ShortCodeStrategy getStrategy(ShortUrlType type) {
        return switch (type) {
            case RANDOM -> randomStrategy;
            case SEMANTIC -> semanticStrategy;
        };
    }
}
