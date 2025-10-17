# USE CASE: 26 Displaying the cities ordered by population from the world

## CHARACTERISTIC INFORMATION

### Goal in Context

As an urban researcher I want to see the top N populated cities in the world So that I can focus on major global urban centers

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains world population data.

### Success End Condition

The cities of the world ranked by population

### Failed End Condition

Population data is not displayed

### Primary Actor

urban researcher

### Trigger

urban researcher requests for the cities in the world to be displayed.

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by an urban researcher
2. System attempts to find the population data requested
3. System runs a simple query (SELECT City FROM world, Order By population ASC )
4. urban researcher receives the population data requested

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

