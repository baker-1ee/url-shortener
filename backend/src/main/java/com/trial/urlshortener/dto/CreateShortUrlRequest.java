package com.trial.urlshortener.dto;

import com.trial.urlshortener.enums.ShortUrlType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateShortUrlRequest {

    @Schema(description = "shortUrlType", example = "RANDOM")
    @NotNull(message = "shortUrlType must not be null")
    private ShortUrlType shortUrlType;

    @Schema(description = "originUrl", example = "https://www.google.com")
    @NotBlank(message = "originUrl must not be blank")
    @Size(max = 2048)
    private String originUrl;

}
