CREATE EXTENSION btree_gist; -- adds more options for exclusion operators

CREATE TABLE reservation(
  id             uuid      PRIMARY KEY,
  user_id        uuid      REFERENCES user_account(id),
  created_at     timestamp,
  reserved_from  timestamp,
  reserved_to    timestamp,
  state          varchar,
  notes          varchar,

  -- TODO: Add integration tests for the possible types
  type           varchar,

  -- desk
  desk_id        uuid      REFERENCES desk(id),
  -- TODO: Add columns for parking spots and meeting rooms

  CONSTRAINT reservation_overlap_excl EXCLUDE USING gist(
    desk_id WITH =, tsrange(reserved_from, reserved_to) WITH &&
  ) WHERE (state IN ('PENDING', 'CONFIRMED'))
);
