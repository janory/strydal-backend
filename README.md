## strydal-backend
strydal-backend is the soul of Strydal.

## Testing
```shell
./gradlew test
```

## Dockerising strydal-backend locally
#### Pre requirements:
- Docker has to be installed
```shell
./gradlew docker
```


## Running strydal-backend locally in Docker
#### Pre requirements:
- strydal-backend is Dockerised (check with `docker images` command)
- There is a local PostgreSQL with an empty database `strydal`, with user and password `strydal`

### Running with DB setup (ONLY ONCE)
```shell
docker run --net=host -e "CREATE_DB=true" -p 8080:8080 -t com.strydal/strydal-backend
```

### Running after DB creation
```shell
docker run --net=host -p 8080:8080 -t com.strydal/strydal-backend
```

## Deploying strydal-backend to Zeit
#### Pre requirements:
- Docker has to be installed
- now cli has to be installed (install with `npm i -g now`)
- You have access to the `strydal` team in Zeit

```shell
./deploy.sh
```

## Setup Zeit for deployments (One time only)
#### Pre requirements:
- The `api.strydal.com` hostname is setup properly

```shell
now -T strydal secrets add db-host "databaseIPOrHostname"
now -T strydal secrets add db-port "5432"
now -T strydal secrets add db-user "strydal"
now -T strydal secrets add db-password "databasePassword"
now -T strydal secrets add spring-profile "prod"
now -T strydal secrets add jwt-secret-key "jwtSecretKey"
```

#### Scale down to 0 all the instances for the domain, except Brussels
On every new deployment the scale rules will be copied from the previous alias and the previous instance will scaled down.
```shell
now -T strydal scale api.strydal.com 0
now -T strydal scale api.strydal.com bru1 1
```