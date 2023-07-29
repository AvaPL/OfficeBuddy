#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE DATABASE $POSTGRES_OFFICE_BUDDY_DATABASE;
	CREATE USER $POSTGRES_OFFICE_BUDDY_USER WITH PASSWORD '$POSTGRES_OFFICE_BUDDY_PASSWORD';
	GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_OFFICE_BUDDY_DATABASE TO $POSTGRES_OFFICE_BUDDY_USER;

	CREATE DATABASE $POSTGRES_ZITADEL_DATABASE;
	CREATE USER $POSTGRES_ZITADEL_USER WITH PASSWORD '$POSTGRES_ZITADEL_PASSWORD';
	GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_ZITADEL_DATABASE TO $POSTGRES_ZITADEL_USER;
EOSQL