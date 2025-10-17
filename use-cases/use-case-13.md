# USE CASE: 13 Getting population breakdown by country

## CHARACTERISTIC INFORMATION

### Goal in Context

As a demographic analyst I want to see population breakdown by country (total, urban, rural) So that I can analyse urbanization patterns by country

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains country population data.

### Success End Condition

Country Population is displayed and broke down correctly

### Failed End Condition

Population data is not displayed 

### Primary Actor

Demographic Analyst

### Trigger

Demographic Analyst requests for the breakdown of population data

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Demographic Analyst
2. System attempts to find the population data requested
3. System runs a simple query (SELECT country, total, urban, rural FROM world )
4. Demographic Analyst receives the breakdown of population data

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

