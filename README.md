# Spring Async Retry Demo

A demonstration project showcasing Spring Boot's asynchronous execution capabilities with thread pool management and retry patterns. This project implements a currency conversion scenario to illustrate how to handle concurrent operations with controlled thread pools and retry mechanisms in Spring applications.

## Overview

This project demonstrates:
- Thread pool configuration and management in Spring Boot
- Asynchronous method execution with `@Async`
- Retry patterns for handling transient failures
- Concurrent execution with controlled parallelism
- Integration testing with Cucumber

## Technical Stack

- Java 17
- Spring Boot 3.4.3
- Cucumber 7.14.0
- JUnit 5
- Project Lombok

## Key Features

### 1. Async Configuration
- Configurable thread pool with core and max pool sizes
- Custom queue capacity for managing load
- Rejection handling policy for queue overflow

### 2. Retry Mechanism
- Concurrent retry attempts for failed operations
- Configurable maximum retry attempts
- Smart cancellation of pending retries upon success

### 3. Thread Pool Management
- Controlled concurrency with fixed thread pool
- Queue capacity management
- Parallel test execution support

## Project Structure

```
src/
├── test/
    ├── java/
    │   └── io/github/orcunbalcilar/
    │       ├── springcurrency/
    │       │   ├── AsyncConfiguration.java     # Thread pool configuration
    │       │   ├── CalculationService.java     # Simulated service with failures
    │       │   ├── CurrencyCalculator.java     # Retry logic implementation
    │       │   └── CurrencyConversion.java     # Async conversion interface
    │       └── glue/
    │           └── CurrencyConversionSteps.java # Cucumber test steps
    └── resources/
        ├── application.properties              # Configuration properties
        └── conversion.feature                  # Cucumber test scenarios
```

## Configuration

### Thread Pool Configuration (application.properties)
```properties
currency.task.executor.core-pool-size=5
currency.task.executor.queue-capacity=100
```

### Parallel Test Execution (junit-platform.properties)
```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=fixed
cucumber.execution.parallel.config.fixed.parallelism=40
```

## Key Components

1. **AsyncConfiguration**: Configures the thread pool executor with specific settings for concurrent operations.
2. **CurrencyCalculator**: Implements the retry mechanism with concurrent attempts.
3. **CalculationService**: Simulates a service with potential failures (60% failure rate for testing).
4. **CurrencyConversion**: Provides the async interface for currency conversion operations.

## Usage Example

```java
@Autowired
private CurrencyConversion currencyConversion;

// Async conversion with retry
CompletableFuture<Double> result = currencyConversion.convert(100.0, 0.85);
Double convertedAmount = result.get(); // Will retry up to 5 times if needed
```

## Testing

The project includes comprehensive test coverage using Cucumber for behavior-driven development:

```gherkin
Feature: Currency Conversion
  Scenario: Convert USD to EUR
    Given the exchange rate from USD to EUR is 0.85
    When I convert 100 USD to EUR
    Then I should receive 85 EUR
```

## Building and Running

```bash
# Clone the repository
git clone https://github.com/yourusername/spring-async-retry-demo.git

# Build the project
./mvnw clean install

# Run tests
./mvnw test
```

## Best Practices Demonstrated

1. Thread Pool Management
   - Controlled concurrency with fixed pool size
   - Proper queue capacity configuration
   - Rejection handling policy

2. Async Operation Handling
   - CompletableFuture for async operations
   - Proper exception handling
   - Cancellation of unnecessary operations

3. Retry Pattern
   - Concurrent retry attempts
   - Atomic operation handling
   - Smart cancellation of pending retries

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.