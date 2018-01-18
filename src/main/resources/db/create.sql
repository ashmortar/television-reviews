SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS reviews (
id int PRIMARY KEY auto_increment,
content VARCHAR,
showId INTEGER,
reviewerName VARCHAR,
rating INTEGER
);

CREATE TABLE IF NOT EXISTS shows (
id int PRIMARY KEY auto_increment,
title VARCHAR,
summary VARCHAR,
releaseDate VARCHAR,
networkId INTEGER,
seasons INTEGER
);

CREATE TABLE IF NOT EXISTS networks (
id int PRIMARY KEY auto_increment,
name VARCHAR,
availability VARCHAR
);

