create database politech_coreapi_auth;
create user 'politech_coreapi_auth'@'%' identified by 'politech_coreapi_auth';
grant usage on *.* to 'politech_coreapi_auth'@'%';
grant select, alter, create, delete, drop, index, insert, references, update, lock tables on `coreapi\_auth`.* to 'politech_coreapi_auth'@'%';
flush privileges;