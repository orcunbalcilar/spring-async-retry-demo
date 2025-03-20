package io.github.orcunbalcilar.glue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.orcunbalcilar.springcurrency.CurrencyConversion;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class CurrencyConversionSteps {

    @Autowired
    private CurrencyConversion currencyConversion;

    private double exchangeRate;
    private double convertedAmount;

    @Given("the exchange rate from USD to EUR is {double}")
    public void theExchangeRateFromUsdToEurIs(double rate) {
        this.exchangeRate = rate;
    }

    @When("I convert {double} USD to EUR")
    public void iConvertUsdToEur(double amount) throws ExecutionException, InterruptedException {
        this.convertedAmount = currencyConversion.convert(amount, exchangeRate).get();
    }

    @Then("I should receive {double} EUR")
    public void iShouldReceiveEur(double expectedAmount) {
        assertEquals(expectedAmount, convertedAmount, 0.001);
    }
}