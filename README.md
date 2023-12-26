# IBKR-Dashboard-back-end

## Back-end implementation of the project

![Demo](demo/demo_2023-12-14.gif)
***
## Tech specs:
- Wildfly v.16.0
- Java EE 8 Web API v.8.0.1
- TWS API v.9.73.01
- JDK v.11.0.16
- Docker v.24.0.7

Project dependencies are available in pom.xml file

## Docker support

1. Add .env file with MySQL properties (host, root password, user and password). Docker compose reads environment's
   variables
   from that file.

In dockerfile I'm using [dockerize](https://github.com/jwilder/dockerize)* to wait on MySQL service to start and start
Wildfly

***Note****: If you are building in the container with ***aarch64 architecture***, then you should tweak a bit dockerize
via:

```shell
# clone to your local machine
git clone https://github.com/jwilder/dockerize.git
cd dockerize
GOOS=linux GOARCH=arm64 go build -ldflags="-X main.Version=$(git describe --tags)" -o dockerize .
```

2. Build image (execute in the app root):

```shell
docker build -t your-cool-image .
```

3. Start up wildfly and mysql services in detached mode (optionally)

```shell
docker-compose up -d
```

If you have an error 127 with dockerize, then you should start this container and copy tweaked dockerize file from your
local machine to `/usr/local/bin` and make it executable:

```shell
docker exec -it name-of-container chmod +x usr/local/bin/dockerize
```

4. Restart container, add admin user to access Wildfly administration console and add datasource 