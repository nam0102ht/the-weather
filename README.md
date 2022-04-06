# The weather

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [User Stories](#user-stories)
* [Weather API provider](#weather-api-provider)
* [Requirements](#requirements)
* [Notes](#notes)

## General info
A simple weather checking API service.

## Technologies
* Java 11
* Spring Boot 2.4.3
* MySQL 5.7
* Docker
* Redis
* Sl4j

## Setup
**Prerequisites:**

Running command before starting project
```
./gradlew startTestEnvironment
```

**Run:**
```
$ ./gradlew bootRun
```
**Test:**
```
$ ./gradlew test
```

**ComponentTest:**
```
$ ./gradlew componentTest
```

**BlackboxTest:**
```
$ ./gradlew blackboxTest
```

**Coverage:**
```
$ ./gradlew clean coverage
```

## User stories
###Requirements

|Requirements|User Story|Priority|
|---|---|---|
|Search for today weather of a specific city|As an API consumer, I should be able to search for today's weather by inputting the city name.|High|
||||
|Save weather data|As an API consumer, I should be able to save weather data for retrieval|High|
||||
|Get historical weather data|As an API consumer,  I should be able to look for weather data from past periods|High
||||
|Delete historical weather data|As an API consumer,  I should be able to delete an existing weather record|Medium
||||
|Update historical weather data|As an API consumer, I should be able to update an existing weather record|Medium
||||
|Able to ensure existing function is working|As an API contributor, I should be able to make sure my contribution won’t break existing function|Low

###Postman document
we can test all of api by document:
https://documenter.getpostman.com/view/9719621/UVXjLGdN

## Weather API Provider

https://openweathermap.org/current
> Access current weather data for any location on Earth including over 200,000 cities! We collect and process weather data from different sources such as global and local weather models, satellites, radars and vast network of weather stations. Data is available in JSON, XML, or HTML format.
