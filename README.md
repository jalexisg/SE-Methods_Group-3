# Population Information System

## Course Information
**Module:** Software Engineering Methods  
**Group:** 3  
**Academic Year:** 2025/2026

## Contributors

| Name | Matriculation Number | Role |
|------|---------------------|------|
| Aleks Kozlowski | 40784155 | Student Developer |
| Brannon Napier | 40654741 | Student Developer |
| Jaime Garcia Garcia | 40730067 | Student Developer |
| Jonathan Henderson | 40714077 | Student Developer |

## Project Overview

This project involves designing and implementing a comprehensive Population Information System to provide easy access to population data for organizational decision-making and reporting requirements. The system generates 32 specific reports covering countries, cities, capital cities, population summaries, and language demographics.

### Database
The project uses the **World Database**, a comprehensive dataset containing:
- **239 countries** with population, area, government, and economic data
- **4,079 cities** with population statistics worldwide
- **Language information** for demographic analysis
- **Geographic and political data** for comprehensive reporting

This database serves as the primary data source for all population queries and reporting functionality.

## Project Requirements

### 32 Required Reports

#### **Country Reports (6 reports):**
- ✅ All countries in the world organised by largest population to smallest
- ✅ All countries in a continent organised by largest population to smallest  
- 📋 All countries in a region organised by largest population to smallest
- 📋 Top N populated countries in the world where N is provided by the user
- 📋 Top N populated countries in a continent where N is provided by the user
- 📋 Top N populated countries in a region where N is provided by the user

#### **City Reports (10 reports):**
- 📋 All cities in the world organised by largest population to smallest
- 📋 All cities in a continent organised by largest population to smallest
- 📋 All cities in a region organised by largest population to smallest
- 📋 All cities in a country organised by largest population to smallest
- 📋 All cities in a district organised by largest population to smallest
- 📋 Top N populated cities in the world where N is provided by the user
- 📋 Top N populated cities in a continent where N is provided by the user
- 📋 Top N populated cities in a region where N is provided by the user
- 📋 Top N populated cities in a country where N is provided by the user
- 📋 Top N populated cities in a district where N is provided by the user

#### **Capital City Reports (6 reports):**
- 📋 All capital cities in the world organised by largest population to smallest
- 📋 All capital cities in a continent organised by largest population to smallest
- 📋 All capital cities in a region organised by largest to smallest
- 📋 Top N populated capital cities in the world where N is provided by the user
- 📋 Top N populated capital cities in a continent where N is provided by the user
- 📋 Top N populated capital cities in a region where N is provided by the user

#### **Population Summary Reports (3 reports):**
- 📋 Population of people, people living in cities, and people not living in cities in each continent
- 📋 Population of people, people living in cities, and people not living in cities in each region
- 📋 Population of people, people living in cities, and people not living in cities in each country

#### **Basic Population Queries (6 queries):**
- ✅ Population of the world
- ✅ Population of a continent
- 📋 Population of a region
- ✅ Population of a country
- 📋 Population of a district
- 📋 Population of a city

#### **Language Demographics (1 report):**
- 📋 Number of people who speak Chinese, English, Hindi, Spanish, Arabic from greatest to smallest, including percentage of world population

### Report Format Requirements

#### **Country Report Columns:**
- Code, Name, Continent, Region, Population, Capital

#### **City Report Columns:**
- Name, Country, District, Population

#### **Capital City Report Columns:**
- Name, Country, Population

#### **Population Report Format:**
- Name of continent/region/country
- Total population 
- Total population living in cities (including %)
- Total population not living in cities (including %)

## Current Implementation Status

### ✅ **Completed (2/32 reports - 6.25%):**
- Global country population report with full formatting
- Continental country population reports for all continents
- World and continental population queries
- Individual country population queries

### 🔄 **In Progress:**
- Enhanced report formatting and error handling
- Docker deployment optimization

### 📋 **Next Sprint (Sprint 2):**
- Regional country population reports
- Top N country reports (global, continental, regional)
- Regional population queries

## Technology Stack

**Backend:** Java 17  
**Database:** MySQL (World Database with 239 countries, 4,079 cities)  
**Build Tool:** Apache Maven  
**CI/CD:** GitHub Actions  
**Containerization:** Docker (Multi-stage build)  
**Development Environment:** IntelliJ IDEA  
**Database Connectivity:** MySQL Connector/J 8.2.0  
**Architecture:** Multi-layer architecture with separate data access and presentation layers

## Project Structure

```
SE-Methods_Group-3/
├── .github/
│   └── workflows/
│       └── main.yml           # GitHub Actions CI/CD pipeline
├── .idea/                     # IntelliJ IDEA configuration
├── database/                  # Database files and scripts
│   ├── world.sql             # World database SQL file (4,079 cities, 239 countries)
│   └── README.md             # Database documentation
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── napier/
│                   └── sem/
│                       ├── Main.java      # Main application class with reports
│                       └── Country.java   # Country data model class
├── target/                    # Maven build output
├── .gitignore                # Git ignore rules
├── Dockerfile                # Multi-stage Docker container configuration
├── pom.xml                   # Maven project configuration with MySQL connector
├── PRODUCT_BACKLOG.md        # Comprehensive product backlog (32 reports)
├── Code of Conduct.docx      # Project guidelines
└── README.md                 # This file
```

## Installation and Setup

### Prerequisites
- Java 17 or later
- Apache Maven 3.6+
- MySQL Server 8.0+ or Docker MySQL
- Docker (for containerization)
- Git

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/jalexisg/SE-Methods_Group-3.git
   cd SE-Methods_Group-3
   ```

2. **Set up the database**
   ```bash
   # Copy the world database files to the database directory
   cp /Users/Alexis/Downloads/world-db/* ./database/
   
   # Start MySQL server (if using Docker)
   docker run --name mysql-world -e MYSQL_ROOT_PASSWORD=example -p 3306:3306 -d mysql:8.0
   
   # Import the world database
   mysql -h localhost -P 3306 -u root -p < database/world.sql
   ```

3. **Compile the project**
   ```bash
   mvn compile
   ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.napier.sem.Main"
   ```

5. **Build and run with Docker**
   ```bash
   # First compile the Java code
   mvn compile
   
   # Build Docker image
   docker build -t devopsimage .
   
   # Run the container
   docker run --name devopscontainer -d devopsimage
   
   # View the application output
   docker logs devopscontainer
   ```

## Usage

The Population Information System generates comprehensive reports about global population data. Currently implemented reports include:

### **Running the Application:**

#### **With Docker (Recommended):**
```bash
# Start MySQL database
docker run --name mysql-world -e MYSQL_ROOT_PASSWORD=example -e MYSQL_DATABASE=world -p 3306:3306 -d mysql:8.0

# Import World Database
docker exec -i mysql-world mysql -u root -pexample world < database/world.sql

# Build and run the application
docker build -t devopsimage .
docker run --network host --name devopscontainer devopsimage

# View generated reports
docker logs devopscontainer

# Cleanup
docker rm devopscontainer
```

#### **Local Development:**
```bash
# Compile the application
mvn clean package

# Run directly (requires local MySQL)
java -jar target/population-system-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### **Sample Report Output:**

```
=== ALL COUNTRIES BY POPULATION (LARGEST TO SMALLEST) ===

Code Name                                         Continent       Region                    Population Capital
==================================================================================================================
CHN  China                                        Asia            Eastern Asia              1,277,558,000 Peking
IND  India                                        Asia            Southern Asia             1,013,662,000 New Delhi
USA  United States                                North America   North America               278,357,000 Washington
IDN  Indonesia                                    Asia            Southeast Asia              212,107,000 Jakarta
BRA  Brazil                                       South America   South America               170,115,000 Brasília
...

Total countries: 239

=== COUNTRIES IN ASIA BY POPULATION (LARGEST TO SMALLEST) ===
[Similar formatted output for Asian countries only]
```

### **Implemented Features:**
- ✅ Global country ranking by population (239 countries)
- ✅ Continental country rankings (7 continents)
- ✅ Professional tabular formatting with thousands separators
- ✅ Capital city information included
- ✅ Total counts and summary statistics
- ✅ Error handling for database connectivity issues

*Additional reports and user input functionality will be documented as features are implemented.*

## Testing

The project uses Maven for build management and includes:

- **Build Verification:** Automated compilation through GitHub Actions
- **Container Testing:** Docker image building and execution verification through CI/CD
- **Continuous Integration:** Automated testing on every push to repository
- **Docker Integration:** Container builds and runs automatically in the pipeline

### Running Tests Locally:
```bash
mvn test  # (Once test cases are implemented)
```

### Testing Docker Container:
```bash
# Build and test the container locally
mvn compile
docker build -t devopsimage .
docker run --name testcontainer devopsimage
docker logs testcontainer
docker rm testcontainer
```

*Comprehensive testing documentation will be added as the test suite develops.*

## Development Methodology

This project follows agile development principles with:

- **Version Control:** Git with GitHub
- **CI/CD Pipeline:** GitHub Actions for automated building and testing
- **Containerization:** Docker for consistent deployment environments
- **Build Management:** Maven for dependency management and build automation
- **Code Quality:** Regular code reviews and collaborative development

### Current Implementation Status

✅ **Completed:**
- Java 17 application with Maven build system
- Multi-stage Docker containerization with automated builds
- MySQL database integration with connection pooling
- GitHub Actions CI/CD pipeline
- World Database imported (239 countries, 4,079 cities)
- Country data model class with proper formatting
- **2 of 32 required reports implemented:**
  - Global country population report (all countries by population)
  - Continental country population reports (countries by continent)
- Population query methods (world, continent, country)
- Professional report formatting with tabular output
- Error handling and connection retry mechanisms

🔄 **In Progress:**
- Regional country population reports
- Top N country filtering functionality  
- Enhanced user input handling

📋 **Planned Implementation (30 remaining reports):**
- **Sprint 2:** Regional country reports + Top N country variations (4 reports)
- **Sprint 3:** Global and continental city reports (5 reports)  
- **Sprint 4:** Regional, country, and district city reports (5 reports)
- **Sprint 5:** All capital city reports (6 reports)
- **Sprint 6:** Population summary reports (3 reports)
- **Sprint 7:** Basic population queries + Language demographics (7 reports)

📊 **Progress:** 2/32 reports completed (6.25%)

## Documentation

- [PRODUCT_BACKLOG.md](PRODUCT_BACKLOG.md) - Complete product backlog with all 32 required reports
- [System Design Document] - *To be added*
- [User Manual] - *To be added*
- [API Documentation] - *To be added*
- [Database Schema] - *To be added*

## Contributing

Please refer to our `Code of Conduct.docx` for guidelines on contributing to this project.

### Development Workflow

1. Create a feature branch from `develop`
2. Implement changes with appropriate tests
3. Submit a pull request for review
4. Merge after approval and testing

## Project Timeline

*[Key milestones and deadlines to be added]*

## Deliverables

- [ ] System requirements specification
- [ ] System design document
- [ ] Implementation code
- [ ] Testing documentation
- [ ] User documentation
- [ ] Final presentation

## Contact Information

For questions or concerns about this project, please contact any of the team members listed above.

## License

This project is developed as part of academic coursework. All rights reserved by the contributors and the educational institution.

---

*Last updated: October 2, 2025*