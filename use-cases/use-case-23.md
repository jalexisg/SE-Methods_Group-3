# USE CASE: 23 Displaying the cities ordered by population from specific country

## CHARACTERISTIC INFORMATION

### Goal in Context

As a national urban planner I want to see the top N populated cities in a specific country So that I can identify major urban centers within countries

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains country population data.

### Success End Condition

The cities of specific country ranked by population

### Failed End Condition

Population data is not displayed

### Primary Actor

National urban planner

### Trigger

National urban planner requests for the cities in a specific country to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a National urban planner
2. System attempts to find the population data requested
3. System runs a simple query (SELECT City FROM world, WHERE Country = "", Order By population ASC )
4. National urban planner receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

