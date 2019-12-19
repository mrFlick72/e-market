package it.valeriovaudi.emarket.web.endpoint.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRepresentation {
    private BigDecimal value;
    private String currency;
}
