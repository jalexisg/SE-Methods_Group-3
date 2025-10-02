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







## Technology Stack

**Backend:** Java 17  
**Build Tool:** Apache Maven  
**CI/CD:** GitHub Actions  
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
├── target/                    # Maven build output
├── .gitignore                # Git ignore rules
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


```bash

docker build -t devopsimage .

docker logs devopscontainer
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
mvn compile
docker build -t devopsimage .
docker run --name testcontainer devopsimage
docker logs testcontainer
docker rm testcontainer
```

*Comprehensive testing documentation will be added as the test suite develops.*

## Documentation


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
