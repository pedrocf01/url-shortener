package com.pedrocf01.url_shortener.domain.repositories;

import com.pedrocf01.url_shortener.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
