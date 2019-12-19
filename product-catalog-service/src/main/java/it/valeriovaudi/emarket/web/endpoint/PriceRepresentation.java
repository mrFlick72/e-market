package it.valeriovaudi.emarket.web.endpoint;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceRepresentation {
    private BigDecimal value;
    private String currency;
}
