package com.example.demo.tests

import spock.lang.Specification
import spock.lang.Subject

/**
 * Spock specification for testing a User builder pattern with closures
 * This test is completely independent of Quarkus
 */
class UserBuilderSpec extends Specification {

    def "should build a user with builder pattern using closures"() {
        when: "we build a user using the builder with closures"
        def user = UserBuilder.build {
            firstName = "John"
            lastName = "Doe"
            email = "john.doe@example.com"
            age = 30
            address {
                street = "123 Main St"
                city = "Springfield"
                zipCode = "12345"
            }
            roles = ["ADMIN", "USER"]
        }

        then: "the user should have all properties set correctly"
        user.firstName == "John"
        user.lastName == "Doe"
        user.email == "john.doe@example.com"
        user.age == 30
        user.address.street == "123 Main St"
        user.address.city == "Springfield"
        user.address.zipCode == "12345"
        user.roles.size() == 2
        user.roles.contains("ADMIN")
        user.roles.contains("USER")
    }

    def "should build user with minimal data"() {
        when: "we build a user with only required fields"
        def user = UserBuilder.build {
            firstName = "Jane"
            lastName = "Smith"
        }

        then: "the user should have minimal data set"
        user.firstName == "Jane"
        user.lastName == "Smith"
        user.email == null
        user.age == 0
        user.address == null
        user.roles == []
    }

    def "should handle multiple users with different configurations"() {
        given: "a list of user configurations"
        def configs = [
            [firstName: "Alice", lastName: "Brown", age: 25],
            [firstName: "Bob", lastName: "Green", age: 35],
            [firstName: "Charlie", lastName: "White", age: 45]
        ]

        when: "we build users using closure configuration"
        def users = configs.collect { config ->
            UserBuilder.build {
                firstName = config.firstName
                lastName = config.lastName
                age = config.age
            }
        }

        then: "all users should be created correctly"
        users.size() == 3
        users[0].firstName == "Alice"
        users[1].firstName == "Bob"
        users[2].firstName == "Charlie"
        users*.age == [25, 35, 45]
    }

    def "should use Groovy closure delegation to configure nested objects"() {
        when: "we configure a user with complex address using closure"
        def user = UserBuilder.build {
            firstName = "David"
            lastName = "Johnson"
            address {
                street = "456 Oak Ave"
                city = "Portland"
                zipCode = "97201"
                country = "USA"
            }
        }

        then: "nested address should be properly configured"
        user.address.street == "456 Oak Ave"
        user.address.city == "Portland"
        user.address.zipCode == "97201"
        user.address.country == "USA"
    }

    def "should transform user data using closures"() {
        given: "a user"
        def user = UserBuilder.build {
            firstName = "emma"
            lastName = "watson"
            email = "EMMA@EXAMPLE.COM"
        }

        when: "we apply transformation closures"
        def transformed = user.transform { u ->
            u.firstName = u.firstName.capitalize()
            u.lastName = u.lastName.capitalize()
            u.email = u.email.toLowerCase()
        }

        then: "user data should be transformed"
        transformed.firstName == "Emma"
        transformed.lastName == "Watson"
        transformed.email == "emma@example.com"
    }
}

/**
 * User model class
 */
class User {
    String firstName
    String lastName
    String email
    int age
    Address address
    List<String> roles = []

    User transform(Closure transformer) {
        transformer(this)
        return this
    }
}

/**
 * Address model class
 */
class Address {
    String street
    String city
    String zipCode
    String country
}

/**
 * Builder for User objects using Groovy closures
 */
class UserBuilder {
    private User user = new User()
    private Address address

    static User build(@DelegatesTo(UserBuilder) Closure closure) {
        def builder = new UserBuilder()
        closure.delegate = builder
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        return builder.user
    }

    void setFirstName(String firstName) {
        user.firstName = firstName
    }

    void setLastName(String lastName) {
        user.lastName = lastName
    }

    void setEmail(String email) {
        user.email = email
    }

    void setAge(int age) {
        user.age = age
    }

    void setRoles(List<String> roles) {
        user.roles = roles
    }

    void address(@DelegatesTo(AddressBuilder) Closure closure) {
        address = AddressBuilder.build(closure)
        user.address = address
    }
}

/**
 * Builder for Address objects using Groovy closures
 */
class AddressBuilder {
    private Address address = new Address()

    static Address build(@DelegatesTo(AddressBuilder) Closure closure) {
        def builder = new AddressBuilder()
        closure.delegate = builder
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        return builder.address
    }

    void setStreet(String street) {
        address.street = street
    }

    void setCity(String city) {
        address.city = city
    }

    void setZipCode(String zipCode) {
        address.zipCode = zipCode
    }

    void setCountry(String country) {
        address.country = country
    }
}
