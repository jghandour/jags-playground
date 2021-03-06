[![Build Status](https://travis-ci.org/jghandour/jags-playground.svg?branch=master)](https://travis-ci.org/jghandour/jags-playground)

# JAG's Playground
Playground for learning various technologies.

# Maven commands to get you started:

## Running
```
# Run Application
mvn clean spring-boot:run
```

## Testing
```
# Unit Test
mvn clean test -o
# Integration Test
mvn -DforkCount=0 clean verify -o
# Static code analysis with Sonar
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Pcoverage && mvn sonar:sonar
```

## Updating Dependencies
```
# Dependency Updates
mvn versions:display-dependency-updates -U
# Plugin Updates
mvn versions:display-plugin-updates -U
# Front-end Library Updates
mvn com.github.eirslett:frontend-maven-plugin:gulp@gulp-ncu
```