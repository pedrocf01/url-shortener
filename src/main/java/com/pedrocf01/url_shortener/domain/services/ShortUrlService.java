package com.pedrocf01.url_shortener.domain.services;

import com.pedrocf01.url_shortener.ApplicationProperties;
import com.pedrocf01.url_shortener.domain.entities.ShortUrl;
import com.pedrocf01.url_shortener.domain.exceptions.ShortUrlNotFoundException;
import com.pedrocf01.url_shortener.domain.models.CreateShortUrlCmd;
import com.pedrocf01.url_shortener.domain.models.ShortUrlDto;
import com.pedrocf01.url_shortener.domain.repositories.ShortUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final EntityMapper entityMapper;
    private final ApplicationProperties properties;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, ApplicationProperties properties) {
        this.shortUrlRepository = shortUrlRepository;
        this.entityMapper = new EntityMapper();
        this.properties = properties;
    }

    public List<ShortUrlDto> findAllPublicShortUrls() {

        return shortUrlRepository.findPublicShortUrls()
                                 .stream().map(entityMapper::toShortUrlDto).toList();
    }


    @Transactional
    public ShortUrlDto createShortUrl(CreateShortUrlCmd cmd) {
        if(properties.validateOriginalUrl()){
            boolean urlExists = UrlExistenceValidator.isUrlExists(cmd.originalUrl());
            if(!urlExists)
                throw new RuntimeException("Invalid URL" + cmd.originalUrl());
        }
        var shortKey = generateUniqueShortKey();
        var shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(cmd.originalUrl());
        shortUrl.setShortKey(shortKey);
        shortUrl.setCreatedBy(null);
        shortUrl.setIsPrivate(false);
        shortUrl.setClickCount(0L);
        shortUrl.setExpiresAt(Instant.now().plus(properties.defaultExpiryInDays(), java.time.temporal.ChronoUnit.DAYS));
        shortUrl.setCreatedAt(Instant.now());
        shortUrlRepository.save(shortUrl);
        return entityMapper.toShortUrlDto(shortUrl);
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            shortKey = generateRandomShortKey();
        } while (shortUrlRepository.existsByShortKey(shortKey));
        return shortKey;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_KEY_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomShortKey() {
        StringBuilder sb = new StringBuilder(SHORT_KEY_LENGTH);
        for (int i = 0; i < SHORT_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Transactional
    public Optional<ShortUrlDto> accessShortUrl(String shortKey) {
        Optional<ShortUrl> shortUrlOptional = shortUrlRepository.findByShortKey(shortKey);

        if(shortUrlOptional.isEmpty())
            return Optional.empty();

        ShortUrl shortUrl = shortUrlOptional.get();
        if(shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(Instant.now()))
            return Optional.empty();

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
        return shortUrlOptional.map(entityMapper::toShortUrlDto);
    }
}
