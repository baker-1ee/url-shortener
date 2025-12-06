package com.trial.urlshortener.dto;

import com.trial.urlshortener.entity.UrlMappingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CreateShortUrlResponse {
    @Schema(description = "shortUrl", example = "https://sho.rt/abc123")
    private String shortUrl;
    @Schema(description = "originUrl", example = "https://www.google.com")
    private String originUrl;

    public static CreateShortUrlResponse of(UrlMappingEntity entity, String baseUrl) {
        return new CreateShortUrlResponse(
                baseUrl + "/" + entity.getShortCode(),
                entity.getOriginUrl()
        );
    }
}
