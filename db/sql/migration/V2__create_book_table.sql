CREATE TABLE IF NOT EXISTS book
(
    aggregate_id uuid PRIMARY KEY,
    title        text,
    price        text,
    isbn         text,
    publisher    text
);
