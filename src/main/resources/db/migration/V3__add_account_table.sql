CREATE TABLE account
(
    id                 uuid PRIMARY KEY,
    first_name         varchar,
    last_name          varchar,
    email              varchar UNIQUE,
    is_archived        bool,

    type               varchar,

    assigned_office_id uuid REFERENCES office (id)
);

CREATE TABLE account_managed_office
(
    account_id uuid REFERENCES account (id),
    office_id  uuid REFERENCES office (id),
    PRIMARY KEY (account_id, office_id)
);