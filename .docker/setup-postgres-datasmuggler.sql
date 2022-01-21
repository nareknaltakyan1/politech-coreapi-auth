create user politech_coreapi_datasmuggler with encrypted password 'politech_coreapi_datasmuggler';
grant select on all tables in schema public TO politech_coreapi_datasmuggler;
alter default privileges in schema public grant select on tables to politech_coreapi_datasmuggler;