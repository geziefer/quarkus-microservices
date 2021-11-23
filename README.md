# Demo of microservice architecture with Quarkus
Based on https://github.com/cescoffier/microservice-demo

## Goal
We want to build a microservice application, consisting of several services, all using [Quarkus](https://quarkus.io/), but each one using different technologies.

The application should provide the following:
- **Villains Service**: Manage a set of villains
- **Heroes Service**: Manage a set of super heroes
- **Fight Service**: Simulate a fight between a villain and a hero
- **API Gateway**: Provide API and GUI for the previous services
- **Stats Service**: Collect statistics about the fights

Here's an overview:
![architecture](https://github.com/geziefer/quarkus-microservices/blob/main/architecture.png)

## Demo Application Development
This repo contains the application as described above.

In order to understand the single steps of the development, you'll find several tags of the following scheme:
> `step_XY_Z`
*where X = service no., Y = step in service development, Z = textual description of step*

So after cloning the repo you have the final version. Each step is then accessible by `git checkout -f <tagname>`.

## Prerequisites
> `step_00_initial`
- note: This demo uses Windows, adapt commands to Linux/Mac accordingly
- install
  - IDE of choice + git (which you surely have)
  - Java (should be latest LTS release, but here 11 is used)
  - Docker (Docker Desktop for Windows)
- clone this repo
 - open folder in IDE
 - start Docker
 - open 7 terminals git bash in project root
 - open browser

## Villains Service: REST
### Step 10: Villains Service - Create
> `step_10_villainsServiceCreated`
- goto https://code.quarkus.io/
  - fill header
    - Group: de.syrocon.cajee
    - Artifact: villains-service
    - Build Tool: *Maven*
    - Version: *1.0.0-SNAPSHOT*
    - Starter Code: *yes*
  - choose extensions
    - RESTEasy Jackson
    - JDBC Driver - PostgreSQL
    - Hibernate ORM with Panache
  - generate application, download zip and extract
- show generated application (pom, resource, test)
- run in terminal: `.\mvnw quarkus:dev`
  - show running Docker images
  - resume test: `r`
  - open `http://localhost:8080/hello`
  - open `http://localhost:8080/q/dev`

### Step 11: Villains Service - Implement service and DTO
> `step_11_serviceAndDtoImplemented`
- remove class `GreetingResource`
- remove test classes `GreetingResourceTest` and `NativeGreetingResourceIT`
- remove folder `src\main\resources\META-INF`
- create class `Villain` with public name, otherName, level,  picture, powers[]
- create class `VillainResource` returning empty Villain on path `/villains`
- create test class `VillainsResourceTest`
- modify `application.properties` by setting port to `9000`
- run in terminal: `.\mvnw quarkus:dev`
  - resume test: `r`
  - open `http://localhost:9000/`

### Step 12: Villains Service - Connect database
> `step_12_databaseConnected`
- modify class `Villain` to use Panache Entitiy and static `findAll` method
- modify class `VillainResource`by using new method
- add testdata file `import.sql` in `src\main\resources`
- modify `application.properties` with `drop-and-create` behavior
- run in terminal: `.\mvnw quarkus:dev`
  - resume test: `r`
  - run in terminal: `curl -v http://localhost:9000/`

## Heroes Service: GraphQL
### Step 20: Heroes Service - Create
> `step_20_heroesServiceCreated`
- goto https://code.quarkus.io/
  - fill header
    - Group: de.syrocon.cajee
    - Artifact: heroes-service
    - Build Tool: *Maven*
    - Version: *1.0.0-SNAPSHOT*
    - Starter Code: no
  - choose extensions
    - SmallRye GraphQL
  - generate application, download zip and extract
- modify `application.properties` by setting port to `9001`

### Step 21: Heroes Service - Implement DTO, Repository and API
> `step_21_apiAndRepositoryAndDtoImplemented`
- create class `Hero` with public name, longName, image, powers[], level
- create class `HeroRepository` as in-memory datastore and provider for random hero
- create class `HeroesAPI` as `GraphQLApi` returning random hero
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9001/q/dev`
  - click GraphQL Schema

### Step 22: Heroes Service - Fill repository
> `step_22_repositoryFilled`
- modify class `HeroRepository` by adding many heroes
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9001/q/graphql-ui/`
    - query several times for `query {randomHero {name, picture:image, level}}`

## Fight Service: gRPC
### Step 30: Fight Service - Create
> `step_30_fightServiceCreated`
- goto https://code.quarkus.io/
  - fill header
    - Group: de.syrocon.cajee
    - Artifact: fight-service
    - Build Tool: *Maven*
    - Version: *1.0.0-SNAPSHOT*
    - Starter Code: no
  - choose extensions
    - gRPC
    - RESTEasy Reactive
  - generate application, download zip and extract
- modify `application.properties` by setting http port to `9002` and grpc server port to `9003`

### Step 31: Fight Service - Proto
> `step_31_fightProtoCreated`
- create file `fight-service.proto` in `src\main\proto`
  - implement package fight, service FightService and messages Fighters, Hero, Villain, Fight
- run in terminal: `.\mvnw compile`
  - show files in `target\generated-sources\grpc\de\syrocon\cajee`

### Step 32: Fight Service - Service and Logic
> `step_32_fightLogicAndServiceImplemented`
- create fights and fight service implementation
  - create class `Figths` as business logig for fight hero vs. villain depending on their level
  - create class `FightServiceImpl` as implementation of generated FightService
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9002/q/dev`
  - click gRPC Services
    - test FightService with `{"hero":{"name":"neo","level":10},"villain":{"name":"clement","level":11}}`
    - note: does not work (see dev console)
  - instead: run in terminal (git bash): `./grpcurl.exe -plaintext -d '{"hero":{"name":"neo","level":10},"villain":{"name":"clement","level":11}}' localhost:9003 fight.FightService/fight`
  
## API Gateway: Reactive
### Step 40: API Gateway - Create
> `step_40_apiGatewayCreated`
> - note: commited tag contains RESTEasy Reactive and REST Client Reactive without Jackson, will be corrected in step 44
- goto https://code.quarkus.io/
  - fill header
    - Group: de.syrocon.cajee
    - Artifact: api-gateway
    - Build Tool: *Maven*
    - Version: *1.0.0-SNAPSHOT*
    - Starter Code: no
  - choose extensions
    - RESTEasy Reactive Jackson
    - REST Client Reactive Jackson
    - SmallRye GraphQL Client
    - SmallRye Reactive Messaging - Kafka Connector
    - Eclipse Vert.x
    - SmallRye OpenAPI
    - SmallRye Fault Tolerance
    - gRPC
  - generate application, download zip and extract
- modify `application.properties` by setting http port to `9999`

### Step 41: API Gateway - Implement Villains REST Client
> `step_41_villainsClientImplemented`
- create class `Villain` with public name, picture, level
- create interface `VillainClient` for calling villain-service
- modify `application.properties` by setting url from villain-service

### Step 42: API Gateway - Implement Heroes GraphQL Client
> `step_42_heroesClientImplemented`
- create class `Hero` with public name, picture, level
- create class `HeroesClient` for calling heroes-service
- modify `application.properties` by setting url from villain-service

### Step 43: API Gateway - Create Fight gRPC Client
> `step_43_fightClientCreated`
- copy file `fight-service.proto` to `src\main\proto` from fight-service
- modify `application.properties` by setting host and port for fight-service
- run in terminal: `.\mvnw compile`
  - show files in `target\generated-sources\grpc\de\syrocon\cajee`

### Step 44: API Gateway - Implement API
> `step_44_apiImplemented`
> - correct `pom.xml` to use Jackson
- extend classes `Villain` and `Hero`by methods `toGrpc`and `fromGrpc`
- create class `APi` with public name, picture, level
- modify `application.properties` by setting url from villain-service and and grpc server port to `9005`
- run in terminal: `.\mvnw quarkus:dev`
  - start villains-service, hero-service, fight-service in separate terminals with `.\mvnw quarkus:dev`
  - execute in additional terminal several times: `curl -kv http://localhost:9999/api`

### Step 45: API Gateway - Implement Webpage
> `step_45_webpageImplemented`
- create graphic `vs.png` in `src\main\resources\META-INF\resources`
- create webpage `index.html` in `src\main\resources\META-INF\resources`
  - create 2 `div` for displaying villain picture and hero pciture with vs graphic in between
  - create button which calls JS function
  - implement function which calls `/api`, extract results from JSON and fills villain and hero and shows winner
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9999/q/dev`
    - show OpenAPI

## Stats Service: Kafka
### Step 50: Stats Service - Create
> `step_50_statsServiceCreated`
- goto https://code.quarkus.io/
  - fill header
    - Group: de.syrocon.cajee
    - Artifact: stats-service
    - Build Tool: *Maven*
    - Version: *1.0.0-SNAPSHOT*
    - Starter Code: no
  - choose extensions
    - RESTEasy Reactive Jackson
    - SmallRye Reactive Messaging - Kafka Connector
  - generate application, download zip and extract
- modify `application.properties` by setting http port to `9006`

### Step 51: Stats Service - Implement
> `step_51_statsServiceImplemented`
- create class `Villain` with public name, picture, level
- create class `Hero` with public name, picture, level
- create class `Fight` with public villain, hero, winner and constructor
- create class `WinRatio` with privat count, villain, hero and getters and method `accumulate` to extract data from `Fight`
- create class `FightProcessor` with method `ratio` which consumes `fights` and produces `ratio` as broadcast
- create class `FightDeserializer` as `ObjectMapperDeserializer`for `Fight`
- create class `StatsResource` with `ratio` channel and producer for Server Sent Events

### Step 52: API Gateway - Send Events
> `step_52_apiGatewayEventsSent`
- extend class `Api` emitting `Fight` to channel
- implement class `FightSerializer` as `ObjectMapperSerializer`for `Fight`
- modify `application.properties` by adding `smallrye-kafka` as connector for `flights`
- run in separate terminal: `.\mvnw quarkus:dev`
  - start stats-service with `.\mvnw quarkus:dev`
  - open `http://localhost:9006/q/dev`
    - show Kafka channel

### Step 53: Fault Tolerance & Logging
> `step_53_faulttoleranceAndLoggingAdded`
- API Gateway: extend class `VillainClient` with `NonBlocking` and `CircuitBreaker`
- API Gateway: extend class `Api` with `NonBlocking` and `Retry` and logging fight and winner
- Villains Service: extend class `VillainResource` with logging villain
- Hero Service: extend class `HeroesAPI` with logging hero
- Fight Service: extend class `Fights` with logging fight
- Stats Service: extend class `StatsResource` with logging event
- start villains-service, hero-service, fight-service, api-gateway, stats-service in separate terminals with `.\mvnw quarkus:dev`
- run in additional terminal: `curl -N --http2 -H "Accept:text/event-stream" http://localhost:9006/stats`
  - open `http://localhost:9999/`
  - play several times
  - show terminal logs
