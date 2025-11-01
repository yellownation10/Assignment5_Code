# Assignment 5

## Part 1 – Barnes & Noble tests
- Implemented JUnit 5 tests in `src/test/java/org/example/Barnes/BarnesAndNobleTest.java`.
- **Specification-based tests:**
    - `null order returns null`
    - `single known book totals price and buys it`
- **Structural-based tests:**
    - `if stock is 0, it records it as unavailable and buys 0`
    - `multiple ISBNs → DB and process called for each`
- Tests use Mockito to mock `BookDatabase` and `BuyBookProcess`.
## Part 3 – Amazon tests
- Created test package: `src/test/java/org/example/Amazon/`
- **Unit tests** (`AmazonUnitTest.java`):
  - specification-based: verifies `addToCart(...)` delegates to `ShoppingCart`
  - specification-based: verifies `calculate()` aggregates results from all `PriceRule`s
  - structural-based: empty rules → total = 0
  - structural-based: empty cart still calls rule
- **Integration tests** (`AmazonIntegrationTest.java`):
  - specification-based: uses real `Database` + `ShoppingCartAdaptor` + `Amazon` to add items and calculate a non-zero total
  - structural-based: multiple items in DB → all processed → total ≥ base price + delivery + electronic surcharge
- DB is reset in the integration test setup.
- `mvn clean test` (10 tests) passes locally.
- GitHub Actions (SE333_CI) runs successfully on push to `main`.



## How to run
```bash
mvn clean test

