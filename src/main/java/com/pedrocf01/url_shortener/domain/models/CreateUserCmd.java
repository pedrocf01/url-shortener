package com.pedrocf01.url_shortener.domain.models;

public record CreateUserCmd(
        String email,
        String password,
        String name,
        Role role) {
}