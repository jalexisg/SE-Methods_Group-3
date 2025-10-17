# USE CASE: 11 Getting a population of a specific Continent

## CHARACTERISTIC INFORMATION

### Goal in Context

As a researcher I want to get the population of a specific continent So that I can analyse continental demographics

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains Continent population data.

### Success End Condition

Population of a specific Continent is displayed correctly

### Failed End Condition

Population data is not displayed 

### Primary Actor

Researcher

### Trigger

Researcher requests for the  population data of a specific Continent

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Researcher
2. System attempts to find the population data requested
3. System runs a simple query (SELECT FROM _Continent_)
4. Researcher receives the population data of a chosen Continent

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no Continent data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

