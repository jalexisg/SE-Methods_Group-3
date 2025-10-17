# USE CASE: 12 Getting total population

## CHARACTERISTIC INFORMATION

### Goal in Context

As a researcher I want to get the total world population So that I can use it as a baseline for analysis

### Scope

Main Application / Database

### Level

Primary task.

### Preconditions

Database contains total population data.

### Success End Condition

Total Population is displayed correctly

### Failed End Condition

Population data is not displayed 

### Primary Actor

Researcher

### Trigger

Researcher requests for the total population data

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by a Researcher
2. System attempts to find the population data requested
3. System runs a simple query (SELECT * FROM)
4. Researcher receives the total population data

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no population data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

