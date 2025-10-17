# USE CASE: 21 Displaying the capital cities ordered from largest to smallest

## CHARACTERISTIC INFORMATION

### Goal in Context

As a political analyst I want to see all capital cities ranked by population from largest to smallest So that I can analyze the size of world capitals

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains continents population data.

### Success End Condition

The capital cities ranked from largest to smallest

### Failed End Condition

Population data is not displayed 

### Primary Actor

Political analyst

### Trigger

Political Analyst requests for the capital cities to be displayed in a specific order.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Political Analyst
2. System attempts to find the population data requested
3. System runs a simple query (SELECT CapitalCity FROM world, Order By population DESC )
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

