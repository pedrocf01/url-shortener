package com.pedrocf01.url_shortener.domain.models;

import java.io.Serializable;

/**
 * DTO for {@link com.pedrocf01.url_shortener.domain.entities.User}
 */
public record UserDto(Long id, String name) implements Serializable {
}