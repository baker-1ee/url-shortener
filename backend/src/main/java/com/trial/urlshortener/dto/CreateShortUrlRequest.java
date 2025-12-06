package com.trial.urlshortener.dto;

import com.trial.urlshortener.enums.ShortUrlType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateShortUrlRequest {

    @NotNull(message = "shortUrlType must not be null")
    private ShortUrlType shortUrlType;

    @NotBlank(message = "originUrl must not be blank")
    @Size(max = 2048)
    private String originUrl;
    
}
