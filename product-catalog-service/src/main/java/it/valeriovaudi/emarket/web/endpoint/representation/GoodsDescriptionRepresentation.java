package it.valeriovaudi.emarket.web.endpoint.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDescriptionRepresentation {

    private String name;
    private String content;

}
