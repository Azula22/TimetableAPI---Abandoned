# Points schema

# --- !Ups

CREATE TABLE Points (
    id BIGINT NOT NULL AUTO_INCREMENT,
    subject TEXT NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Points;