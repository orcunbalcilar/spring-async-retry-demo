Feature: Currency Conversion

  Scenario Outline: Convert USD to EUR
    Given the exchange rate from USD to EUR is <rate>
    When I convert <amount> USD to EUR
    Then I should receive <expected> EUR

    Examples:
      | rate | amount | expected |
      | 0.85 | 100    | 85       |
      | 0.85 | 200    | 170      |
      | 0.85 | 300    | 255      |
      | 0.85 | 400    | 340      |
      | 0.85 | 500    | 425      |
      | 0.85 | 600    | 510      |
      | 0.85 | 700    | 595      |
      | 0.85 | 800    | 680      |
      | 0.85 | 900    | 765      |
      | 0.85 | 1000   | 850      |