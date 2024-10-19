CREATE TABLE parking_spot (
  id             uuid      PRIMARY KEY,
  name           varchar,
  is_available   bool,
  notes          varchar[],
  is_handicapped bool,
  is_underground bool,
  office_id      uuid      REFERENCES office(id),

  is_archived    bool      DEFAULT 'no',

  UNIQUE (name, office_id)
);