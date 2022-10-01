CREATE TABLE IF NOT EXISTS pages
(
    id        INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name      TEXT UNIQUE,
    content   BLOB    NOT NULL,
    protected INTEGER NOT NULL DEFAULT false
);


CREATE TABLE IF NOT EXISTS users
(
    id          INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL UNIQUE,
    auto_patrol INTEGER NOT NULL DEFAULT false,
    admin       INTEGER NOT NULL DEFAULT false
);


CREATE TABLE IF NOT EXISTS redirects
(
    "from" INTEGER NOT NULL UNIQUE,
    "to"   INTEGER NOT NULL,
    author INTEGER NOT NULL,

    FOREIGN KEY ("from") REFERENCES pages (id),
    FOREIGN KEY ("to") REFERENCES pages (id),
    FOREIGN KEY (author) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS files
(
    id     INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
    name   TEXT    NOT NULL UNIQUE,
    author INTEGER NOT NULL,

    FOREIGN KEY (author) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS edits
(
    page       INTEGER NOT NULL,
    ordinal    INTEGER NOT NULL,
    author     INTEGER NOT NULL,
    date       INTEGER NOT NULL,
    reviewer   INTEGER DEFAULT null,
    difference BLOB    NOT NULL,

    UNIQUE (page, ordinal),
    CHECK ( ordinal > 0 ),
    CHECK ( date > 0 ),
    FOREIGN KEY (page) REFERENCES pages (id),
    FOREIGN KEY (author) REFERENCES users (id),
    FOREIGN KEY (reviewer) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS sessions
(
    user  INTEGER NOT NULL UNIQUE,
    key   TEXT    NOT NULL UNIQUE,
    until INTEGER NOT NULL,

    FOREIGN KEY (user) REFERENCES users (id)
);
