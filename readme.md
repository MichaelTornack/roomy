# Roomy application

## Setup
- Download repository
- Install docker / docker - compose and have the deamon running
- execute in the project folder:
 
```
docker-compose build
docker-compose up
```

Wait for db and api container to fully boot up

## Use Api
- go to http://localhost:8080/swagger-ui/index.html
- Login with default user admin account:

````
POST /api/auth/login

{
  "email": "default_admin@gmail.com",
  "password": "abcd123"
}
````

- use access token for further authentication.

## Integration Tests
- uses TestContainer for MySql database
- shows how to setup test with different test users


## Security
- jwt security key is currently hard coded in properties. When deployed it should be injected securely

## Next steps
- integrate kafka messaging

