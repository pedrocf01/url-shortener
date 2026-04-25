package com.pedrocf01.url_shortener.domain.models;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.pedrocf01.url_shortener.domain.entities.ShortUrl}
 */
public record ShortUrlDto(Long id, String shortKey, String originalUrl,
                          Boolean isPrivate, Instant expiresAt,
                          UserDto createdBy, Long clickCount,
                          Instant createdAt) implements Serializable {
}
