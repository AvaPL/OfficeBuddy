CREATE TABLE desk(
  id             uuid      PRIMARY KEY,
  name           varchar,
  is_available   bool,
  notes          varchar[],
  is_standing    bool,
  monitors_count int2,
  has_phone      bool,
  office_id      uuid      REFERENCES office(id),

  is_archived    bool      DEFAULT 'no',

  UNIQUE (name, office_id)
);
