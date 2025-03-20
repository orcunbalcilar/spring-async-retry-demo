package io.github.orcunbalcilar.springcurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CurrencyCalculator {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyCalculator.class);
    private static final int MAX_RETRIES = 5;
    private final CalculationService calculationService;

    public CurrencyCalculator(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    public CompletableFuture<Double> calculateConversion(double amount, double exchangeRate) {
        CompletableFuture<Double> resultFuture = new CompletableFuture<>();
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        List<CompletableFuture<?>> attempts = new ArrayList<>();

        // Start all calculation attempts concurrently
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            final int currentAttempt = attempt;
            CompletableFuture<?> attemptFuture = CompletableFuture.runAsync(() -> {
                if (isCompleted.get()) {
                    logger.info("Skipping attempt {} as result already obtained", currentAttempt);
                    return;
                }

                try {
                    logger.info("Starting calculation attempt {} for amount {} with rate {}",
                        currentAttempt, amount, exchangeRate);

                    calculationService.calculate(amount, exchangeRate)
                        .thenAccept(optionalResult -> {
                            if (optionalResult.isPresent() && isCompleted.compareAndSet(false,
                                true)) {
                                double result = optionalResult.get();
                                logger.info("Calculation succeeded on attempt {}: {} * {} = {}",
                                    currentAttempt, amount, exchangeRate, result);
                                resultFuture.complete(result);
                                attempts.forEach(f -> f.cancel(true));
                            } else if (!optionalResult.isPresent()) {
                                logger.warn("Attempt {} failed: calculation returned no result",
                                    currentAttempt);
                                if (currentAttempt == MAX_RETRIES && !isCompleted.get()) {
                                    resultFuture.completeExceptionally(
                                        new RuntimeException("All calculation attempts failed"));
                                }
                            }
                        });

                } catch (Exception e) {
                    logger.warn("Attempt {} failed with exception: {}", currentAttempt,
                        e.getMessage());
                    if (currentAttempt == MAX_RETRIES && !isCompleted.get()) {
                        resultFuture.completeExceptionally(
                            new RuntimeException("All calculation attempts failed", e));
                    }
                }
            });
            attempts.add(attemptFuture);
        }

        return resultFuture;
    }
}
