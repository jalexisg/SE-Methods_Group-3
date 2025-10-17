# USE CASE: 5 Population Data Integration

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *External system* I want to access population data via REST API So that I can integrate with other applications

### Scope

World population database (World DB).

### Level

Primary task / configuration.

### Preconditions

Database contains current population data

### Success End Condition

Population data is displayed for integration with other applications

### Failed End Condition

Population data is not displayed 

### Primary Actor

External System

### Trigger

A request for population data is sent to external system

## MAIN SUCCESS SCENARIO

1. A request for access to population data is sent by an external system
2. System attempts to find the population data requested
3. System runs a simple query (SELECT * FROM)
4. External System receives the data and integrates it with other applications

## EXTENSIONS

2a. **Database connection fails**:
1. System informs the user the database is unavailable and logs the error.

3a. **Query returns no data**:
1. System informs the user that no country data was found.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 0.1.0

