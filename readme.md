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

## How to run
```bash
mvn clean test

## Part 3 – Amazon tests
    - Added `AmazonUnitTest.java` (specification-based + structural-based)
    - Added `AmazonIntegrationTest.java` (specification-based + structural-based)
    - `mvn clean test` passes locally
    - GitHub Actions workflow: ✅
