# Migration Prompt: Groovy/Spock to Java/JUnit/Quarkus

## Context

This document provides a detailed migration prompt for converting the Groovy/Spock integration tests in the `groovy-spock-tests` module to Java/JUnit tests integrated with Quarkus.

**DO NOT EXECUTE THIS MIGRATION YET** - This is a prepared prompt for future use.

---

## Overview

The `groovy-spock-tests` module currently contains:
- 2 Spock specification files (17 total tests)
- Heavy use of Groovy closures for DSL-style builders
- Builder pattern implementations using Groovy's delegate features
- Parameterized tests using Spock's @Unroll
- Data-driven tests with where: blocks
- Groovy collection methods (findAll, collect, inject, groupBy)

---

## Migration Goals

1. **Convert Groovy to Java**: Translate all Groovy code to Java 17
2. **Replace Spock with JUnit 5**: Use JUnit Jupiter for test framework
3. **Maintain Test Coverage**: Preserve all existing test scenarios
4. **Integrate with Quarkus**: Make tests compatible with Quarkus testing framework
5. **Preserve Intent**: Keep the same testing logic and assertions

---

## Key Conversion Patterns

### 1. Closure-Based Builders → Java Builders

**Groovy (current):**
```groovy
def user = UserBuilder.build {
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    address {
        street = "123 Main St"
        city = "Springfield"
    }
}
```

**Java (target):**
```java
User user = UserBuilder.builder()
    .firstName("John")
    .lastName("Doe")
    .email("john.doe@example.com")
    .address(Address.builder()
        .street("123 Main St")
        .city("Springfield")
        .build())
    .build();
```

### 2. Spock Specifications → JUnit 5 Tests

**Groovy (current):**
```groovy
class UserBuilderSpec extends Specification {
    def "should build a user with builder pattern"() {
        when:
        def user = UserBuilder.build { ... }
        
        then:
        user.firstName == "John"
        user.lastName == "Doe"
    }
}
```

**Java (target):**
```java
@QuarkusTest
class UserBuilderTest {
    @Test
    @DisplayName("should build a user with builder pattern")
    void shouldBuildUserWithBuilderPattern() {
        // when
        User user = UserBuilder.builder()...build();
        
        // then
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }
}
```

### 3. Parameterized Tests (@Unroll → @ParameterizedTest)

**Groovy (current):**
```groovy
@Unroll
def "should find products by tag '#tag'"() {
    given: "a product catalog"
    def catalog = createSampleCatalog()
    
    when: "we search by tag"
    def found = catalog.findByTag(tag)
    
    then: "we should find the expected products"
    found*.name == expectedNames
    
    where:
    tag         || expectedNames
    "computer"  || ["Laptop", "Mouse"]
    "accessory" || ["Mouse"]
}
```

**Java (target):**
```java
@ParameterizedTest(name = "should find products by tag ''{0}''")
@CsvSource({
    "computer, 'Laptop,Mouse'",
    "accessory, 'Mouse'",
    "office, 'Desk'"
})
void shouldFindProductsByTag(String tag, String expectedNamesStr) {
    // given
    ProductCatalog catalog = createSampleCatalog();
    
    // when
    List<Product> found = catalog.findByTag(tag);
    
    // then
    List<String> expectedNames = Arrays.asList(expectedNamesStr.split(","));
    List<String> actualNames = found.stream()
        .map(Product::getName)
        .collect(Collectors.toList());
    assertEquals(expectedNames, actualNames);
}
```

### 4. Groovy Collections → Java Streams

**Groovy (current):**
```groovy
def electronics = catalog.findProducts { it.category == "Electronics" }
def names = electronics*.name
def total = products.inject(0.0) { acc, p -> acc + p.price }
def grouped = products.groupBy { it.category }
```

**Java (target):**
```java
List<Product> electronics = catalog.findProducts(
    p -> p.getCategory().equals("Electronics")
);

List<String> names = electronics.stream()
    .map(Product::getName)
    .collect(Collectors.toList());

BigDecimal total = products.stream()
    .map(Product::getPrice)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

Map<String, List<Product>> grouped = products.stream()
    .collect(Collectors.groupingBy(Product::getCategory));
```

### 5. Closure Parameters → Functional Interfaces

**Groovy (current):**
```groovy
class ProductCatalog {
    List<Product> findProducts(Closure predicate) {
        products.findAll(predicate)
    }
    
    ProductCatalog applyDiscount(Closure discountFunction) {
        // ...
    }
}
```

**Java (target):**
```java
class ProductCatalog {
    public List<Product> findProducts(Predicate<Product> predicate) {
        return products.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    public ProductCatalog applyDiscount(Function<BigDecimal, BigDecimal> discountFunction) {
        // ...
    }
}
```

---

## Step-by-Step Migration Plan

### Phase 1: Setup Java Test Infrastructure
1. Create new test directory structure in `quarkus-webservice/src/test/java`
2. Add JUnit 5 dependencies (already present via Quarkus)
3. Set up test base classes if needed

### Phase 2: Convert Model Classes
1. Convert `User` class to Java with proper getters/setters
2. Convert `Address` class to Java
3. Convert `Product` class to Java with BigDecimal for price
4. Add Lombok if desired for reducing boilerplate

### Phase 3: Convert Builder Patterns
1. Implement Java builder pattern for `UserBuilder`
2. Implement Java builder pattern for `AddressBuilder`
3. Implement Java builder pattern for `ProductBuilder` and `CatalogBuilder`
4. Replace closure delegation with fluent API

### Phase 4: Convert UserBuilderSpec Tests
1. Convert test class structure (Specification → Test class)
2. Migrate test methods
   - Replace `def` with proper method signatures
   - Convert `when/then` to standard assertions
   - Update property access (dot notation → getter methods)
3. Update assertions (Groovy operators → JUnit assertions)

### Phase 5: Convert ProductCatalogSpec Tests
1. Convert test class structure
2. Migrate parameterized test (@Unroll → @ParameterizedTest)
3. Convert collection operations to streams
4. Implement functional interfaces for closure replacements

### Phase 6: Integration with Quarkus
1. Add `@QuarkusTest` annotation where appropriate
2. Consider if any tests should use `@QuarkusTestResource`
3. Add CDI injection if needed for future Quarkus integration
4. Update test configuration in application.properties

### Phase 7: Validation
1. Ensure all 17 tests are migrated and passing
2. Verify test coverage is maintained
3. Check that test execution time is comparable
4. Validate that tests can run in Quarkus dev mode

---

## Dependencies Changes

### Remove from POM:
```xml
<dependency>
    <groupId>org.apache.groovy</groupId>
    <artifactId>groovy</artifactId>
</dependency>
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
</dependency>
```

### Add to POM (if not already present):
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>
```

### Remove GMavenPlus Plugin:
```xml
<plugin>
    <groupId>org.codehaus.gmavenplus</groupId>
    <artifactId>gmavenplus-plugin</artifactId>
</plugin>
```

---

## Testing Strategy

1. **Incremental Migration**: Migrate one test class at a time
2. **Side-by-Side Running**: Keep both Groovy and Java tests running until migration is complete
3. **Test Name Consistency**: Keep similar test names for traceability
4. **Assertion Library**: Use AssertJ for more fluent assertions similar to Groovy

---

## Expected Challenges

### 1. Loss of Groovy's Conciseness
- **Challenge**: Java is more verbose than Groovy
- **Solution**: Use Lombok for model classes, AssertJ for assertions

### 2. Closure Expressiveness
- **Challenge**: Java lambdas are less flexible than Groovy closures
- **Solution**: Use functional interfaces (Predicate, Function, Consumer)

### 3. Builder Pattern Complexity
- **Challenge**: Groovy's delegate-based builders are very elegant
- **Solution**: Implement traditional fluent builders or use Lombok's @Builder

### 4. Collection Operations
- **Challenge**: Groovy's collection methods are very concise
- **Solution**: Use Java Streams API with static imports for readability

### 5. Dynamic Property Access
- **Challenge**: Groovy allows direct property access, Java requires getters
- **Solution**: Use proper getter methods or Lombok's @Data

---

## Optional Enhancements

After basic migration, consider:

1. **Add Quarkus Test Resources**: For database or messaging tests
2. **Add Mockito**: For mocking dependencies
3. **Add TestContainers**: For integration testing with real services
4. **Add Arquillian**: For advanced CDI testing scenarios
5. **Add RESTAssured**: Already available in Quarkus module for API testing

---

## Validation Criteria

The migration is complete when:
- [ ] All 17 tests are converted to Java/JUnit 5
- [ ] All tests pass with 100% success rate
- [ ] Test coverage is equal or better than original
- [ ] No Groovy dependencies remain
- [ ] Tests integrate with Quarkus test framework
- [ ] Build time is similar or better
- [ ] Documentation is updated

---

## Example Complete Conversion

### Before (Groovy/Spock):

```groovy
class UserBuilderSpec extends Specification {
    def "should build user with minimal data"() {
        when:
        def user = UserBuilder.build {
            firstName = "Jane"
            lastName = "Smith"
        }

        then:
        user.firstName == "Jane"
        user.lastName == "Smith"
        user.email == null
        user.roles == []
    }
}
```

### After (Java/JUnit/Quarkus):

```java
@QuarkusTest
class UserBuilderTest {
    
    @Test
    @DisplayName("should build user with minimal data")
    void shouldBuildUserWithMinimalData() {
        // when
        User user = UserBuilder.builder()
            .firstName("Jane")
            .lastName("Smith")
            .build();

        // then
        assertThat(user)
            .satisfies(u -> {
                assertThat(u.getFirstName()).isEqualTo("Jane");
                assertThat(u.getLastName()).isEqualTo("Smith");
                assertThat(u.getEmail()).isNull();
                assertThat(u.getRoles()).isEmpty();
            });
    }
}
```

---

## Notes

- **DO NOT EXECUTE THIS MIGRATION**: This is a prepared prompt only
- The migration should be done incrementally and tested thoroughly
- Consider the team's Java expertise when choosing patterns
- Maintain test readability as a top priority
- Document any significant deviations from the original test logic

---

## Conclusion

This migration will modernize the test suite while integrating it with Quarkus. The key is to maintain the same test coverage and intent while leveraging Java's type safety and Quarkus's testing capabilities.

The resulting test suite will:
- Be type-safe with compile-time checking
- Integrate better with Quarkus features
- Be more maintainable for Java developers
- Have better IDE support and tooling
- Be easier to debug and troubleshoot
