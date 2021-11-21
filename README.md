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

### Step 0: Prerequisite and Setup
> `step_00_initial`
