CREATE TABLE account(
  id                 uuid    PRIMARY KEY,
  email              varchar UNIQUE,
  is_archived        bool,

  type               varchar,

  -- user
  assigned_office_id uuid    REFERENCES office(id)
);
