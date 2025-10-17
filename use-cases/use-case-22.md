# USE CASE: 22 Displaying the cities ordered by population from specific districts

## CHARACTERISTIC INFORMATION

### Goal in Context

As a local urban planner I want to see the top N populated cities in a specific district So that I can identify major urban centers within districts

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains districts population data.

### Success End Condition

The cities of specific districts ranked by population

### Failed End Condition

Population data is not displayed

### Primary Actor

local urban planner

### Trigger

local urban planner requests for the cities in a specific districts to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a local urban planner
2. System attempts to find the population data requested
3. System runs a simple query (SELECT City FROM world, WHERE District = "", Order By population ASC )
4. Local urban planner receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

