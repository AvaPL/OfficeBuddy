CREATE TABLE office(
  id             uuid      PRIMARY KEY,
  name           varchar   UNIQUE,
  notes          varchar[],
  address_line_1 varchar,
  address_line_2 varchar,
  postal_code    varchar,
  city           varchar,
  country        varchar,

  is_archived    bool      DEFAULT 'no'
);