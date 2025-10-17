# USE CASE: 24 Displaying the cities ordered by population from specific region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a regional urban planner I want to see the top N populated cities in a specific region So that I can identify major urban centers within regions

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains region population data.

### Success End Condition

The cities of specific region ranked by population

### Failed End Condition

Population data is not displayed

### Primary Actor

Regional urban planner

### Trigger

Regional urban planner requests for the cities in a specific region to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Regional urban planner
2. System attempts to find the population data requested
3. System runs a simple query (SELECT City FROM world, WHERE Region = "", Order By population ASC )
4. Regional urban planner receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

