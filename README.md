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

This project involves designing and implementing a new system to provide easy access to population information for an organization. The system aims to streamline the process of retrieving, analyzing, and reporting population data to support organizational decision-making and reporting requirements.

## Objectives

- Design a user-friendly interface for accessing population information
- Implement efficient data retrieval and processing mechanisms
- Ensure data accuracy and reliability
- Provide comprehensive reporting capabilities
- Meet organizational requirements for population data access

## System Features

### Core Functionality
- [ ] Population data search and filtering
- [ ] Data visualization and charts
- [ ] Report generation
- [ ] User authentication and authorization
- [ ] Data export capabilities

### Technical Requirements
- [ ] Database design and implementation
- [ ] API development for data access
- [ ] User interface development
- [ ] Testing and quality assurance
- [ ] Documentation and user guides

## Technology Stack

**Backend:** Java 17  
**Build Tool:** Apache Maven  
**CI/CD:** GitHub Actions  
**Containerization:** Docker  
**Development Environment:** IntelliJ IDEA

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
│                       └── Main.java    # Main application class
├── target/                    # Maven build output
├── .gitignore                # Git ignore rules
├── Dockerfile                # Docker container configuration
├── pom.xml                   # Maven project configuration
├── Code of Conduct.docx      # Project guidelines
└── README.md                 # This file
```

## Development Methodology

This project follows agile development principles with:

- **Version Control:** Git with GitHub
- **CI/CD Pipeline:** GitHub Actions for automated building and testing
- **Containerization:** Docker for consistent deployment environments
- **Build Management:** Maven for dependency management and build automation
- **Code Quality:** Regular code reviews and collaborative development

### Current Implementation Status

✅ **Completed:**
- Initial project setup with Maven
- Basic Java application structure (Main.java)
- GitHub Actions CI/CD pipeline configuration
- Docker containerization with Dockerfile
- Git version control with proper .gitignore
- Docker container entrypoint configured for Main class

🔄 **In Progress:**
- Population data management system design
- Database integration
- User interface development

📋 **Planned:**
- Database schema implementation
- API development for data access
- Frontend user interface
- Comprehensive testing suite

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

2. **Compile the project**
   ```bash
   mvn compile
   ```

3. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.napier.sem.Main"
   ```

4. **Build and run with Docker**
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

Currently, the application runs a basic Java program that outputs "Boo yah!" to demonstrate the build system is working correctly.

### Running Locally:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.napier.sem.Main"
```

### Running with Docker:
```bash
# Compile and build
mvn compile
docker build -t devopsimage .

# Run container
docker run --name devopscontainer devopsimage

# Or run in detached mode and check logs
docker run --name devopscontainer -d devopsimage
docker logs devopscontainer
```

*Additional functionality will be documented as features are implemented.*

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

## Documentation

- [System Design Document]
- [User Manual]
- [API Documentation]
- [Database Schema]

## Contributing

Please refer to our `Code of Conduct.docx` for guidelines on contributing to this project.

### Development Workflow

1. Create a feature branch from `master`
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

*Last updated: October 1, 2025*