package com.trial.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HitCountCounterService {

    private static final String HITS_KEY = "shorturl:hits";
    private final StringRedisTemplate redisTemplate;

    public void increment(String shortCode) {
        redisTemplate.opsForHash().increment(HITS_KEY, shortCode, 1L);
    }

    public Map<String, Long> fetchAndClearAll() {
        String script = """
                local key = KEYS[1]
                local entries = redis.call('HGETALL', key)
                redis.call('DEL', key)
                return entries
                """;

        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(List.class);

        List<Object> raw = redisTemplate.execute(redisScript, List.of(HITS_KEY));

        Map<String, Long> result = new HashMap<>();

        if (raw != null) {
            for (int i = 0; i < raw.size(); i += 2) {
                String code = raw.get(i).toString();
                Long delta = Long.valueOf(raw.get(i + 1).toString());
                result.put(code, delta);
            }
        }

        return result;
    }
}
