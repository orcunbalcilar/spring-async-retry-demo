package io.github.orcunbalcilar.springcurrency;

import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConversion {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversion.class);

    private final CurrencyCalculator calculator;

    public CurrencyConversion(CurrencyCalculator calculator) {
        this.calculator = calculator;
    }

    @SneakyThrows
    @Async("currencyTaskExecutor")
    public CompletableFuture<Double> convert(double amount, double exchangeRate) {
        Thread.sleep(1000);
        logger.info("Starting conversion process for {} USD with rate {}", amount, exchangeRate);
        return calculator.calculateConversion(amount, exchangeRate).thenApply(result -> {
            logger.info("Conversion process completed for {} USD: {} EUR", amount, result);
            return result;
        }).exceptionally(ex -> {
            logger.error("Error during conversion", ex);
            throw new RuntimeException("Conversion failed", ex);
        });
    }
}