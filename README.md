# OfficeBuddy

## Initial setup

When the app is first run, it creates a superadmin user with the following credentials:
- username: `superadmin@officebuddy.com`
- password: `superadmin`

It then can be used to configure the app.

## API documentation

The API documentation can be found at `/docs` endpoint by default. 

To obtain bearer token for existing account, use the following curl command:

```shell
OB_KEYCLOAK_URL='http://localhost:8888'
OB_USERNAME='<username>'
OB_PASSWORD='<password>'
curl -s -L -X POST "$OB_KEYCLOAK_URL/realms/office-buddy/protocol/openid-connect/token" \
-H "Content-Type: application/x-www-form-urlencoded" \
--data-urlencode "client_id=office-buddy-ui" \
--data-urlencode "grant_type=password" \
--data-urlencode "username=$OB_USERNAME" \
--data-urlencode "password=$OB_PASSWORD" | jq
```