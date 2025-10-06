# Demo Migration Tests

This project demonstrates a Quarkus 3 application with complex web services and a separate Groovy/Spock test module, prepared for migration to Java/JUnit/Quarkus.

## Project Structure

```
demo-migration-tests/
├── quarkus-webservice/          # Quarkus 3 REST API module
│   ├── src/main/java/
│   │   └── com/example/demo/quarkus/
│   │       ├── model/           # Complex DTOs
│   │       │   ├── ComplexGreetingRequest.java
│   │       │   └── ComplexGreetingResponse.java
│   │       └── resource/        # REST endpoints
│   │           └── GreetingResource.java
│   └── src/test/java/           # Quarkus tests
│       └── GreetingResourceTest.java
│
├── groovy-spock-tests/          # Groovy/Spock integration tests module
│   └── src/test/groovy/
│       └── com/example/demo/tests/
│           ├── UserBuilderSpec.groovy      # Builder pattern tests with closures
│           └── ProductCatalogSpec.groovy   # DSL and collection tests
│
├── MIGRATION_PROMPT.md          # Migration guide (DO NOT EXECUTE)
└── pom.xml                      # Parent POM
```

## Features

### Quarkus Web Service Module

A Quarkus 3 application with unnecessarily complex web services demonstrating:

- **Complex Request/Response DTOs**: Multi-level nested objects with various data types
- **RESTful API**: POST and GET endpoints
- **Business Logic**: Language translation, text transformations, metadata processing
- **Quarkus Features**: REST, Jackson JSON, CDI

#### API Endpoints

1. **POST /api/greetings/complex**
   - Accepts a complex greeting request with user context, configuration, tags, metadata
   - Returns a complex response with message, processing metadata, and transformations

2. **GET /api/greetings/simple/{name}**
   - Simple endpoint that still returns a complex response structure

### Groovy/Spock Tests Module

Independent test module showcasing Groovy and Spock features:

- **Closure-based Builders**: DSL-style object construction using Groovy closures
- **Builder Pattern**: UserBuilder and ProductBuilder with nested configurations
- **Spock Specifications**: BDD-style tests with given/when/then
- **Parameterized Tests**: Using @Unroll and where: blocks
- **Groovy Collections**: Heavy use of findAll, collect, inject, groupBy
- **Functional Programming**: Closures for filtering, mapping, reducing

**Total Tests**: 17 tests across 2 specification files

## Prerequisites

- Java 17 or higher
- Maven 3.8+

## Building the Project

```bash
# Build everything
mvn clean install

# Build and run tests
mvn clean test

# Run only Quarkus tests
cd quarkus-webservice && mvn test

# Run only Groovy/Spock tests
cd groovy-spock-tests && mvn test
```

## Running the Quarkus Application

```bash
cd quarkus-webservice
mvn quarkus:dev
```

The application will start on http://localhost:8080

### Testing the API

**Simple Greeting:**
```bash
curl http://localhost:8080/api/greetings/simple/World
```

**Complex Greeting:**
```bash
curl -X POST http://localhost:8080/api/greetings/complex \
  -H "Content-Type: application/json" \
  -d '{
    "userContext": {
      "userId": "user123",
      "username": "John",
      "email": "john@example.com",
      "roles": ["USER"],
      "preferences": {}
    },
    "configuration": {
      "language": "fr",
      "includeTimestamp": false,
      "uppercase": false,
      "prefix": "Hey",
      "suffix": "!",
      "maxLength": 100
    },
    "tags": ["greeting", "test"],
    "metadata": {"source": "curl"},
    "priority": "HIGH",
    "timestamp": "2024-01-01T10:00:00"
  }'
```

## Test Coverage

### Quarkus Module Tests
- ✅ Simple greeting endpoint test
- ✅ Complex greeting endpoint test

### Groovy/Spock Tests

**UserBuilderSpec (5 tests)**:
- ✅ Build user with complete data using closures
- ✅ Build user with minimal data
- ✅ Build multiple users with different configurations
- ✅ Configure nested objects using closure delegation
- ✅ Transform user data with closures

**ProductCatalogSpec (12 tests)**:
- ✅ Create catalog using DSL
- ✅ Filter products with closure predicates
- ✅ Transform products with map closures
- ✅ Find products by tag (parameterized: 4 variants)
- ✅ Calculate total using closure reduction
- ✅ Group products by category
- ✅ Chain multiple closure operations
- ✅ Configure objects with with() closure

## Migration Plan

See [MIGRATION_PROMPT.md](MIGRATION_PROMPT.md) for a comprehensive guide on migrating the Groovy/Spock tests to Java/JUnit/Quarkus.

**⚠️ IMPORTANT**: The migration prompt is prepared but **NOT EXECUTED**. The current state shows both implementations side by side.

## Key Technologies

- **Quarkus 3.6.4**: Supersonic Subatomic Java
- **Groovy 4.0.15**: Dynamic language for the JVM
- **Spock 2.4-M1**: BDD testing framework
- **JUnit 5**: Jupiter testing framework (for Quarkus tests)
- **RESTEasy Reactive**: Reactive REST implementation
- **Jackson**: JSON processing

## Project Goals

1. ✅ Create a Quarkus 3 application with complex web services
2. ✅ Implement unnecessarily complex request/response structures
3. ✅ Create independent Groovy/Spock tests using closures and builders
4. ✅ Prepare comprehensive migration prompt to Java/JUnit
5. ⏸️ DO NOT execute the migration (as requested)

## Future Enhancements

When the migration is executed, consider:
- Adding database integration with Hibernate/Panache
- Adding messaging with Kafka or AMQP
- Adding security with JWT/OIDC
- Adding observability with Micrometer
- Implementing the migration to Java/JUnit
- Adding integration tests with TestContainers

## License

This is a demonstration project for testing and migration purposes.

## Author

Demo project for Groovy/Spock to Java/JUnit/Quarkus migration.
