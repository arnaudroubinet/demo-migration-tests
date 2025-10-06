package com.example.demo.tests

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Spock specification for testing a product catalog using closures and DSL
 * Demonstrates Groovy's powerful closure and collection features
 */
class ProductCatalogSpec extends Specification {

    def "should create product catalog using closure-based DSL"() {
        given: "a product catalog builder"
        def catalog = ProductCatalog.create {
            product {
                name = "Laptop"
                price = 999.99
                category = "Electronics"
                tags = ["computer", "portable"]
            }
            
            product {
                name = "Mouse"
                price = 29.99
                category = "Electronics"
                tags = ["computer", "accessory"]
            }
            
            product {
                name = "Desk"
                price = 199.99
                category = "Furniture"
                tags = ["office"]
            }
        }

        expect: "catalog should contain all products"
        catalog.products.size() == 3
        catalog.products[0].name == "Laptop"
        catalog.products[1].name == "Mouse"
        catalog.products[2].name == "Desk"
    }

    def "should filter products using closure predicates"() {
        given: "a product catalog"
        def catalog = createSampleCatalog()

        when: "we filter electronics"
        def electronics = catalog.findProducts { it.category == "Electronics" }

        then: "we should get only electronics"
        electronics.size() == 2
        electronics*.name == ["Laptop", "Mouse"]
    }

    def "should transform products using map closures"() {
        given: "a product catalog"
        def catalog = createSampleCatalog()

        when: "we apply discount closure"
        def discounted = catalog.applyDiscount { price -> price * 0.9 }

        then: "all prices should be discounted"
        discounted.products[0].price == 899.991 // 999.99 * 0.9
        discounted.products[1].price == 26.991  // 29.99 * 0.9
        discounted.products[2].price == 179.991 // 199.99 * 0.9
    }

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
        "office"    || ["Desk"]
        "portable"  || ["Laptop"]
    }

    def "should calculate total price using closure reduction"() {
        given: "a product catalog"
        def catalog = createSampleCatalog()

        when: "we calculate total price"
        def total = catalog.calculateTotal { acc, product -> acc + product.price }

        then: "total should be sum of all prices"
        total == 1229.97 // 999.99 + 29.99 + 199.99
    }

    def "should group products by category using closure"() {
        given: "a product catalog"
        def catalog = createSampleCatalog()

        when: "we group by category"
        def grouped = catalog.groupBy { it.category }

        then: "products should be grouped correctly"
        grouped["Electronics"].size() == 2
        grouped["Furniture"].size() == 1
        grouped["Electronics"]*.name.contains("Laptop")
        grouped["Electronics"]*.name.contains("Mouse")
    }

    def "should chain multiple closure operations"() {
        given: "a product catalog"
        def catalog = createSampleCatalog()

        when: "we chain filter, transform, and sort operations"
        def result = catalog
            .findProducts { it.category == "Electronics" }
            .sort { a, b -> a.price <=> b.price }

        then: "result should be filtered and sorted"
        result.size() == 2
        result[0].name == "Mouse"    // Cheaper item first
        result[1].name == "Laptop"   // More expensive item second
    }

    def "should use with() closure for object configuration"() {
        when: "we create and configure a product using with()"
        def product = new Product().with {
            name = "Keyboard"
            price = 79.99
            category = "Electronics"
            tags = ["computer", "input"]
            it // return the object
        }

        then: "product should be properly configured"
        product.name == "Keyboard"
        product.price == 79.99
        product.category == "Electronics"
        product.tags.size() == 2
    }

    private ProductCatalog createSampleCatalog() {
        ProductCatalog.create {
            product {
                name = "Laptop"
                price = 999.99
                category = "Electronics"
                tags = ["computer", "portable"]
            }
            
            product {
                name = "Mouse"
                price = 29.99
                category = "Electronics"
                tags = ["computer", "accessory"]
            }
            
            product {
                name = "Desk"
                price = 199.99
                category = "Furniture"
                tags = ["office"]
            }
        }
    }
}

/**
 * Product model
 */
class Product {
    String name
    BigDecimal price
    String category
    List<String> tags = []
}

/**
 * Product catalog with closure-based operations
 */
class ProductCatalog {
    List<Product> products = []

    static ProductCatalog create(@DelegatesTo(CatalogBuilder) Closure closure) {
        def builder = new CatalogBuilder()
        closure.delegate = builder
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        return builder.catalog
    }

    List<Product> findProducts(Closure predicate) {
        products.findAll(predicate)
    }

    List<Product> findByTag(String tag) {
        products.findAll { it.tags.contains(tag) }
    }

    ProductCatalog applyDiscount(Closure discountFunction) {
        def discounted = new ProductCatalog()
        discounted.products = products.collect { product ->
            new Product(
                name: product.name,
                price: discountFunction(product.price),
                category: product.category,
                tags: product.tags
            )
        }
        return discounted
    }

    BigDecimal calculateTotal(Closure reducer) {
        products.inject(0.0, reducer)
    }

    Map<String, List<Product>> groupBy(Closure classifier) {
        products.groupBy(classifier)
    }
}

/**
 * Builder for product catalog
 */
class CatalogBuilder {
    ProductCatalog catalog = new ProductCatalog()

    void product(@DelegatesTo(ProductBuilder) Closure closure) {
        def product = ProductBuilder.build(closure)
        catalog.products << product
    }
}

/**
 * Builder for products
 */
class ProductBuilder {
    private Product product = new Product()

    static Product build(@DelegatesTo(ProductBuilder) Closure closure) {
        def builder = new ProductBuilder()
        closure.delegate = builder
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        return builder.product
    }

    void setName(String name) {
        product.name = name
    }

    void setPrice(BigDecimal price) {
        product.price = price
    }

    void setCategory(String category) {
        product.category = category
    }

    void setTags(List<String> tags) {
        product.tags = tags
    }
}
