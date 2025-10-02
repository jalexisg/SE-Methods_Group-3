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

The **Population Information System** is a Java-based application designed to provide comprehensive population reports using the MySQL World Database. This system connects to a database containing information about 239 countries and 4,079 cities worldwide, generating detailed population analysis reports.

### Key Features
- **Database Integration:** Connects to MySQL World Database with 239 countries and 4,079 cities
- **Population Reports:** Generates 32 different types of population reports
- **Containerized Deployment:** Docker support for easy deployment and scaling
- **CI/CD Pipeline:** Automated testing and deployment through GitHub Actions
- **Professional Output:** Formatted console output with proper alignment and styling

### Current Implementation Status
- ✅ **2/32 Reports Implemented** (6.25% complete)
  - Countries by population (globally)
  - Countries by population (by continent)
- ✅ **Database connectivity established**
- ✅ **Docker containerization completed**
- ✅ **CI/CD pipeline configured**
- 🔄 **30 additional reports pending implementation**

## Technology Stack

**Backend:** Java 17 with Eclipse Temurin JDK  
**Database:** MySQL World Database (239 countries, 4,079 cities)  
**Build Tool:** Apache Maven 3.9+  
**Containerization:** Docker with multi-stage builds  
**CI/CD:** GitHub Actions with automated testing  
**Dependencies:** MySQL Connector/J 8.1.0, JUnit 5.10.0  
**Development Environment:** IntelliJ IDEA  
**Version Control:** Git with Git Flow workflow

## Project Structure

```
SE-Methods_Group-3/
├── .github/
│   └── workflows/
│       └── main.yml           # GitHub Actions CI/CD pipeline
├── .idea/                     # IntelliJ IDEA configuration
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── napier/
│                   └── sem/
│                       ├── Main.java      # Main application class
│                       └── Country.java   # Country data model
├── target/                    # Maven build output
├── .gitignore                # Git ignore rules
├── Dockerfile                # Multi-stage Docker build
├── pom.xml                   # Maven project configuration
├── PRODUCT_BACKLOG.md        # Complete list of 32 required reports
├── Code of Conduct.docx      # Project guidelines
└── README.md                 # This file
```

## Installation and Setup

### Prerequisites
- Java 17 or later
- Apache Maven 3.6+
- Docker (for containerization)
- Git

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/jalexisg/SE-Methods_Group-3.git
   cd SE-Methods_Group-3
   ```

   ```bash
   mvn compile
   ```

   ```bash
   mvn exec:java -Dexec.mainClass="com.napier.sem.Main"
   ```

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

The Population Information System generates 32 different types of population reports. Currently implemented:

### Available Reports (2/32 implemented)

1. **All Countries by Population (Global)**
   - Displays all countries ordered by population from largest to smallest
   - Format: Rank, Country Code, Name, Continent, Region, Population, Capital

2. **Countries by Population (By Continent)**
   - Displays countries in a specific continent ordered by population
   - Format: Rank, Country Code, Name, Continent, Region, Population, Capital

### Running the Application

```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.napier.sem.Main"

# Using Docker (Recommended)
docker build -t population-info-system:latest .
docker run --name population-container population-info-system:latest
docker logs population-container
```

### Sample Output
```
=== All Countries by Population ===
Rank | Code | Name                  | Continent     | Region               | Population  | Capital
-----|------|-----------------------|---------------|----------------------|-------------|--------
1    | CHN  | China                 | Asia          | Eastern Asia         | 1,277,558,000| Beijing
2    | IND  | India                 | Asia          | Southern and Central | 1,013,662,000| New Delhi
3    | USA  | United States         | North America | North America        | 278,357,000 | Washington
...
```


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
mvn clean compile
docker build -t population-info-system:test .
docker run --name test-container population-info-system:test
docker logs test-container
docker rm test-container
```

*Comprehensive testing documentation will be added as the test suite develops.*

## Documentation

### Reports Specification
For a complete list of all 32 required population reports, see `PRODUCT_BACKLOG.md`.

### API Documentation
- `Main.java`: Main application entry point with database connection and report generation
- `Country.java`: Data model representing country information with formatting methods

### Database Schema
The application uses the MySQL World Database with the following key tables:
- `country`: Contains country information (239 records)
- `city`: Contains city information (4,079 records)
- `countrylanguage`: Contains language information by country

### CI/CD Pipeline
The GitHub Actions workflow includes:
- Java 17 compilation
- Maven dependency resolution
- Docker image building
- Automated testing
- Artifact generation

## Contributing

Please refer to our `Code of Conduct.docx` for guidelines on contributing to this project.

### Development Workflow

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
