# Export and import Zitadel resources

Import and export has to be done via Zitadel API. This means that initialization of the Zitadel instance has to be done
partially manually. There is no feature similar to Keycloak realm import on instance startup.

### Export

1. Login as Zitadel admin.
1. Find `adminServiceUrl` and save it as to `ZITADEL_ADMIN_API_ENDPOINT` environment variable. It can be found in one of
   the predefined ZITADEL project applications' URLs.
1. In ZITADEL project, create a new service user `export_user` with bearer access token.
1. Create a [personal access token](https://zitadel.com/docs/guides/integrate/pat). Save it to `PAT_EXPORT_TOKEN`
   environment variable.
1. Go to global instance settings
   and [add the `export_user` as IAM owner](https://zitadel.com/docs/guides/manage/console/managers).
1. Run the following curl to export the instance:

```shell
curl --request POST \
  --url $ZITADEL_ADMIN_API_ENDPOINT/export \
  --header "Authorization: Bearer $PAT_EXPORT_TOKEN" \
  --header 'Content-Type: application/json' \
  --data '{ 
    "org_ids": [ ], 
    "excluded_org_ids": [ ],
    "with_passwords": true,
    "with_otp": true,
    "timeout": "60s",
    "response_output": true
}' -o export.json
```

7. Remove `export_user` service user.

### Import

1. Login as Zitadel admin.
1. Find `adminServiceUrl` and save it as to `ZITADEL_ADMIN_API_ENDPOINT` environment variable. It can be found in one of
   the predefined ZITADEL project applications' URLs.
1. In ZITADEL project, create a new service user `import_user` with bearer access token.
1. Create a [personal access token](https://zitadel.com/docs/guides/integrate/pat). Save it to `PAT_IMPORT_TOKEN`
   environment variable.
1. Go to global instance settings
   and [add the `import_user` as IAM owner](https://zitadel.com/docs/guides/manage/console/managers).
1. Run the following curl to import the instance:

```shell
curl --request POST \
--url $ZITADEL_ADMIN_API_ENDPOINT/import \
--header "Authorization: Bearer $PAT_IMPORT_TOKEN" \
--header 'Content-Type: application/json' \
--data '{
"timeout": "10m",
"data_orgs": '"$(cat export.json)"'
}'
```

Note: import operation isn't idempotent and may fail on importing some entities despite the whole operation being
successful.

7. Regenerate all client secrets manually for the defined applications - the import does not include them. Additionally,
   the client IDs will be regenerated so make sure that you are using the proper ones.
7. Remove `import_user` service user.
