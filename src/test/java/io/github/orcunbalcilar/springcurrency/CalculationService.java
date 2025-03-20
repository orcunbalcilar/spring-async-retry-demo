package io.github.orcunbalcilar.springcurrency;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);

    public CompletableFuture<Optional<Double>> calculate(double amount, double exchangeRate) {
        logger.info("Performing calculation: {} * {}", amount, exchangeRate);

        // Intentionally fail to simulate error scenarios
        if (Math.random() < 0.6) { // 60% chance of failure
            logger.error("Simulating calculation failure");
            return CompletableFuture.completedFuture(Optional.empty());
        }

        return CompletableFuture.completedFuture(Optional.of(amount * exchangeRate));
    }
}