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
- now cli hast to be installed (install with `npm i -g now`)
- You have access to the `strydal` team in Zeit

```shell
./deploy.sh
now -T strydal alias rm strydal-backend
now -T strydal alias [new-url] strydal-backend
now -T strydal scale strydal-backend.now.sh 1
now -T strydal scale [prev-url] 0
```