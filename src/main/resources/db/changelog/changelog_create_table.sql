CREATE TABLE requests
(
    id              SERIAL PRIMARY KEY,
    ip_address      INET NOT NULL,
    input_text      TEXT NOT NULL,
    translated_text TEXT
);