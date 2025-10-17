# USE CASE: 16 Displaying the most populated capital cities in a region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a political analyst I want to see the top N populated capital cities in a specific region So that I can identify major capitals within regions

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains region population data.

### Success End Condition

The top N populated Cities in a region are displayed correctly

### Failed End Condition

Population data is not displayed 

### Primary Actor

Political analyst

### Trigger

Political Analyst requests for the most populated cities in a region to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Political Analyst
2. System attempts to find the population data requested
3. System runs a simple query (SELECT CapitalCity FROM world, WHERE region = "", Order By population ASC )
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

