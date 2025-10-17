# USE CASE: 25 Displaying the cities ordered by population from specific continent

## CHARACTERISTIC INFORMATION

### Goal in Context

As an International urban planner I want to see the top N populated cities in a specific continent So that I can identify major urban centers within continents

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains continent population data.

### Success End Condition

The cities of specific continent ranked by population

### Failed End Condition

Population data is not displayed

### Primary Actor

International urban planner

### Trigger

International urban planner requests for the cities in a specific continent to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by an International urban planner
2. System attempts to find the population data requested
3. System runs a simple query (SELECT City FROM world, WHERE Continent = "", Order By population ASC )
4. International urban planner receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

