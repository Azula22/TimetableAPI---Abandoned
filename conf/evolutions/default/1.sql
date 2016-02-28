# Points schema

# --- !Ups

CREATE TABLE Points (
    id BIGINT NOT NULL AUTO_INCREMENT,
    subject TEXT NOT NULL,
    day TEXT NOT NULL ,
    groupName TEXT NOT NULL,
    start TIME NOT NULL,
    ending TIME NOT NULL,
    kind TEXT NOT NULL,
    teacher TEXT NOT NULL,
    auditorium INTEGER NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE Points;