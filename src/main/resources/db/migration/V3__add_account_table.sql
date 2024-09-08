CREATE TABLE account(
  id                 uuid    PRIMARY KEY,
  first_name         varchar,
  last_name          varchar,
  email              varchar UNIQUE,
  is_archived        bool,

  type               varchar,

  assigned_office_id uuid    REFERENCES office(id),
  managed_office_ids uuid[] -- TODO: Move to separate table to support FK constraint
);
