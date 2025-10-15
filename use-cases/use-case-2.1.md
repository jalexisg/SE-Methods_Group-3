```markdown
# USE CASE: 2.1 Global Country Population Report

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *Data Analyst* I want *to see all countries ranked by population from largest to smallest* so that *I can analyze global population distribution.*

### Scope

World population database (World DB).

### Level

Primary task.

### Preconditions

- Application connects to MySQL database and credentials are configurable.
- `country` table is populated with population data.
- System supports local and Docker environments (report can be run in container or locally).

### Success End Condition

A global country population report is produced listing all countries ordered by descending population.

### Failed End Condition

No report is produced (database unavailable or query error).

### Primary Actor

Data Analyst.

### Trigger

A user requests the global country population report.

## MAIN SUCCESS SCENARIO

1. User requests the global country population report.
2. System connects to the World database.
3. System runs a query to select country code, name, continent, region, population and capital ordered by population descending.
4. System formats the result as a human-readable report (table or CSV) ordered by population.
5. System presents the report to the user (console, file, or UI download).

## EXTENSIONS

2a. **Database connection fails**:
   1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
   1. System informs the user that no country data was found.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.1.0

``` 
