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
- Note: This demo uses Windows, adapt commands to Linux/Mac accordingly
- Install
  - IDE of choice + git (which you surely have)
  - Java (should be latest LTS release, but here 11 is used)
  - Docker (Docker Desktop for Windows)
 - Clone this repo
 - Open folder in IDE
 - Start Docker
 - Open 5 terminals + git bash
 - Open browser

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
- show generated application
  - show `pom.xml`
  - show `GreetingResource.java`
  - show `GreetingResourceTest`
- run in terminal: `.\mvnw quarkus:dev`
  - show running Docker images
  - resume test: `r`
  - open `http://localhost:8080/hello`
  - open `http://localhost:8080/q/dev`

### Step 11: Villains Service - Implement service and DTO
> `step_11_serviceAndDtoImplemented`
- remove generated files
  - remove class `GreetingResource`
  - remove test classes `GreetingResourceTest` and `NativeGreetingResourceIT`
  - remove folder `src\main\resources\META-INF`
- create DTO and REST resource
  - create class `Villain` with public name, otherName, level,  picture, powers[]
  - create class `VillainResource` returning empty Villain on path `/villains`
  - create test class `VillainsResourceTest`
  - modify `application.properties` by setting port to `9000`
- run in terminal: `.\mvnw quarkus:dev`
  - resume test: `r`
  - open `http://localhost:9000/`

### Step 12: Villains Service - Connect database
> `step_12_databaseConnected`
- use Entity instead of DTO
  - convert class `Villain` to Panache Entitiy and select randomly from static `findAll` method
  - use new method in class `VillainResource`
- add testdata
  - add file `import.sql` in `src\main\resources`
  - add `drop-and-create` behavior to `application.properties`
- run in terminal: `.\mvnw quarkus:dev`
  - resume test: `r`
  - `curl -v http://localhost:9000/`

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
- run in terminal: `.\mvnw quarkus:dev`

### Step 21: Heroes Service - Implement DTO, Repository and API
> `step_21_apiAndRepositoryAndDtoImplemented`
- create DTO and API resource
  - create class `Hero` with public name, longName, image, powers[], level
  - create class `HeroRepository` as in-memory datastore and provider for random hero
  - create class `HeroesAPI` as `GraphQLApi` returning random hero
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9001/q/dev`
  - click GraphQL Schema
  - click GraphQL UI
    - query for `query {randomHero {name, picture:image, level}}`

### Step 22: Heroes Service - Fill repository
> `step_22_repositoryFilled`
- create many heroes in `HeroRepository`'s list
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
- run in terminal: `.\mvnw quarkus:dev`

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
    - test FightService -> but does not work (show dev console)
  - run in terminal (git bash): `./grpcurl.exe -plaintext -d '{"hero":{"name":"neo","level":10},"villain":{"name":"clement","level":11}}' localhost:9003 fight.FightService/fight`
  
## API Gateway: Reactive
### Step 40: API Gateway - Create
> `step_40_apiGatewayCreated`
> - Note: commited tag contains RESTEasy Reactive and REST Client Reactive without Jackson, will be corrected in step 44
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
- run in terminal: `.\mvnw quarkus:dev`
  - open `http://localhost:9999/q/dev`

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
  - execute in additional terminal: `curl -kv http://localhost:9999/api`
