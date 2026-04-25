INSERT INTO users (email, password, name, role)
VALUES ('admin@gmail.com', 'admin', 'Administrator', 'ROLE_ADMIN'),
       ('pedro@gmail.com', 'secret', 'Pedro', 'ROLE_USER');

INSERT INTO short_urls (short_key, original_url, created_by, created_at, expires_at, is_private, click_count)
VALUES ('rs1Aed', 'https://www.google.com', 1, TIMESTAMP '2026-03-01', NULL, FALSE, 0),
       ('hujfDf', 'https://www.youtube.com', 1, TIMESTAMP '2026-03-02', NULL, FALSE, 0),
       ('ertcbn', 'https://www.facebook.com', 1, TIMESTAMP '2026-03-03', NULL, FALSE, 0),
       ('edfrtg', 'https://www.instagram.com', 1, TIMESTAMP '2026-03-04', NULL, TRUE, 0),
       ('vbgtyh', 'https://www.twitter.com', 1, TIMESTAMP '2026-03-05', NULL, FALSE, 0),
       ('rtyfgb', 'https://www.linkedin.com', 1, TIMESTAMP '2026-03-06', NULL, FALSE, 0),
       ('rtvbop', 'https://www.wikipedia.org', 1, TIMESTAMP '2026-03-07', NULL, FALSE, 0),
       ('2wedfg', 'https://www.amazon.com', 1, TIMESTAMP '2026-03-08', NULL, TRUE, 0),
       ('6yfrd4', 'https://www.netflix.com', 1, TIMESTAMP '2026-03-09', NULL, FALSE, 0),
       ('r5t4tt', 'https://www.reddit.com', 1, TIMESTAMP '2026-03-10', NULL, FALSE, 0),

       ('ffr4rt', 'https://www.microsoft.com', 1, TIMESTAMP '2026-03-11', NULL, FALSE, 0),
       ('9oui7u', 'https://www.apple.com', 1, TIMESTAMP '2026-03-12', NULL, FALSE, 0),
       ('cvbg5t', 'https://www.github.com', 1, TIMESTAMP '2026-03-13', NULL, FALSE, 0),
       ('nm6ytf', 'https://www.stackoverflow.com', 1, TIMESTAMP '2026-03-14', NULL, FALSE, 0),

       ('tt5y6r', 'https://www.bbc.com', 1, TIMESTAMP '2026-03-15', NULL, FALSE, 0),
       ('fgghty', 'https://www.cnn.com', 1, TIMESTAMP '2026-03-16', NULL, FALSE, 0),
       ('f45tre', 'https://www.spotify.com', 1, TIMESTAMP '2026-03-17', NULL, FALSE, 0),
       ('54rt54', 'https://www.twitch.tv', 1, TIMESTAMP '2026-03-18', NULL, FALSE, 0);
;