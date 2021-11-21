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

### Step 00: Prerequisites
> `step_00_initial`
- Note: This demo uses Windows, adapt commands to Linux/Mac accordingly
- Install
  - IDE of choice + git (which you surely have)
  - Java (can be latest or last LTS release, here 17 is used)
  - Docker (Docker Desktop for Windows)
 - Clone this repo
 - Open folder in IDE
 - Start Docker
 - Open 6 terminals
 - Open browser

### Step 10: Villains Service - Create
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
  - show IDE
    - show `pom.xml`
    - show `GreetingResource.java`
    - show `GreetingResourceTest`
  - run in terminal: `.\mvnw quarkus:dev`
    - show running Docker images
    - resume test: `r`
    - open `http://localhost:8080/hello`
    - open `http://localhost:8080/q/dev`
> `step_10_villainsServiceCreated`
