#!/usr/bin/env bash

CONTAINER_NAME=office_buddy_keycloak
REALM_NAME=office-buddy

docker exec -it "$CONTAINER_NAME" /opt/keycloak/bin/kc.sh export \
  --file=/tmp/"$REALM_NAME"-realm.json \
  --realm "$REALM_NAME" \
  --users realm_file

docker cp "$CONTAINER_NAME":/tmp/"$REALM_NAME"-realm.json ./