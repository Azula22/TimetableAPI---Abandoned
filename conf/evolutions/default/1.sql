# Points schema

# --- !Ups

CREATE TABLE Points (
    id BIGINT NOT NULL,
    subject TEXT NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Points;