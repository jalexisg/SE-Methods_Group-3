# World Database

## Overview

This directory contains the World Database provided for the Population Information System project. The World Database is a sample database that contains information about countries, cities, and languages of the world.

## Database Structure

The World Database typically contains the following tables:

### Tables
- **country**: Information about countries including population, area, government form, etc.
- **city**: Information about cities including population and country
- **countrylanguage**: Information about languages spoken in each country

### Key Fields
- Population data for countries and cities
- Geographic information (continent, region)
- Political information (government form, head of state)
- Economic data (GNP, life expectancy)
- Language information (languages spoken, percentage)

## Setup Instructions

1. **Copy database files from Downloads**
   ```bash
   cp /Users/Alexis/Downloads/world-db/* ./database/
   ```

2. **Start MySQL Server** (using Docker)
   ```bash
   docker run --name mysql-world \
     -e MYSQL_ROOT_PASSWORD=example \
     -e MYSQL_DATABASE=world \
     -p 3306:3306 \
     -d mysql:8.0
   ```

3. **Import the database**
   ```bash
   # Wait for MySQL to start, then import
   mysql -h localhost -P 3306 -u root -pexample world < world.sql
   ```

## Connection Details

- **Host**: localhost
- **Port**: 3306
- **Database**: world
- **Username**: root
- **Password**: example (for development)

## Usage in Java Application

The database will be accessed through JDBC connections in the Java application to provide population information queries and reports.

## Security Note

The connection credentials shown here are for development purposes only. In production, use secure credential management.