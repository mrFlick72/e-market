package it.valeriovaudi.emarket.anticorruptation;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by vvaudi on 02/06/17.
 */


public abstract class AbstractAntiCorruptionLayerService {

    protected final ObjectMapper objectMapper;

    protected AbstractAntiCorruptionLayerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
