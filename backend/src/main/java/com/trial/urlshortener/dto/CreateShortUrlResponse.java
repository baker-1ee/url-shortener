package com.trial.urlshortener.dto;

import com.trial.urlshortener.entity.UrlMappingEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateShortUrlResponse {
    private String shortUrl;
    private String originUrl;

    public static CreateShortUrlResponse of(UrlMappingEntity entity, String baseUrl) {
        return new CreateShortUrlResponse(
                baseUrl + "/" + entity.getShortCode(),
                entity.getOriginUrl()
        );
    }
}
