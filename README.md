# Politech Auth API

This API provides functionalities to lookup and manage user accounts. Any human or computer system that will interact with any of the API's requires being authenticated as a user. The API allows for common functionalities such as creating a new user account, resetting passwords and generating JWT tokens.

## Running the API

### Environment variables

| Variable            | Description                                             | Default       |
|---------------------|---------------------------------------------------------|---------------|
| PORT                | The port on which the API will respond to HTTP requests | 8090          |
| DB_HOST             | The hostname or IP address of the database              | localhost     |
| DB_USER             | The username with which to connect to the database      | politech_coreapi_users |
| DB_PASSWORD         | The password with which to connect to the database      | politech_coreapi_users |
| DB_DB               | The name of the database (schema)                       | politech_coreapi_users |
| DB_PORT             | The port on which the database runs                     | 3306          |

### Building & running

To run the API as a standalone application, simply run the following command:
```
mvn package -DskipTests
docker-compose up
```

To test the application is running, issue the following command:
```
curl http://localhost:8080/actuator/health
```

## Local development
Developers using Eclipse will need to have the m2e-apt plugin installed in the IDE. Follow the directions on https://marketplace.eclipse.org/content/m2e-apt. After importing the project use the "Run As" -> "Maven generate-sources" to compile the necessary classes.

Local development requires an installation of the Postgres database. Download it from https://www.postgresql.org/download/ and override the relevant environment settings in your development environment if you deviate from the defaults (i.e. localhost:5432). You will need to run [setup-postgres.sql](.docker/setup-postgres.sql) with administrator commands before running the project.


