package it.valeriovaudi.emarket.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;


@ToString
@EqualsAndHashCode
public class Price {
    private final BigDecimal value;
    private final String currency;

    public Price(BigDecimal value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
