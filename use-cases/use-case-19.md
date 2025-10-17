# USE CASE: 19 Displaying the capital cities ordered by population

## CHARACTERISTIC INFORMATION

### Goal in Context

As a political analyst I want to see capital cities in specific regions ranked by population So that I can compare capital sizes within regions

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains regions population data.

### Success End Condition

The capital cities of specific regions ranked by population

### Failed End Condition

Population data is not displayed 

### Primary Actor

Political analyst

### Trigger

Political Analyst requests for the capital cities in a specific region to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Political Analyst
2. System attempts to find the population data requested
3. System runs a simple query (SELECT CapitalCity FROM world, WHERE Region = "", Order By population ASC )
4. Political Analyst receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

