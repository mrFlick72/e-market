package it.valeriovaudi.emarket.anticorruptation.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.emarket.anticorruptation.AbstractAntiCorruptionLayerService;
import it.valeriovaudi.emarket.anticorruptation.AntiCorruptionLayerStrategy;
import it.valeriovaudi.emarket.model.Customer;
import it.valeriovaudi.emarket.model.CustomerContact;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrflick72 on 30/05/17.
 */


@Service
public class AccountAntiCorruptionLayerService extends AbstractAntiCorruptionLayerService {

    private Map<String, AntiCorruptionLayerStrategy> customerAntiCorruptationRegistry;
    private Map<String, AntiCorruptionLayerStrategy> customerContactAnticorruptationRegistry;

    private final AccountToCustomerAntiCorruptionLayerServiceHalJsonStrategy accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy;
    private final AccountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy;

    public AccountAntiCorruptionLayerService(ObjectMapper objectMapper,
                                             AccountToCustomerAntiCorruptionLayerServiceHalJsonStrategy accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy,
                                             AccountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy) {
        super(objectMapper);
        this.accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy = accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy;
        this.accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy = accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy;
    }

    @PostConstruct
    public void init(){
        customerAntiCorruptationRegistry = new ConcurrentHashMap<>();
        customerAntiCorruptationRegistry.put(MediaType.APPLICATION_JSON_VALUE, accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy);
        customerAntiCorruptationRegistry.put(MediaType.APPLICATION_JSON_UTF8_VALUE, accountToCustomerAntiCorruptionLayerServiceHalJsonStrategy);

        customerContactAnticorruptationRegistry = new ConcurrentHashMap<>();
        customerContactAnticorruptationRegistry.put(MediaType.APPLICATION_JSON_VALUE, accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy);
        customerContactAnticorruptationRegistry.put(MediaType.APPLICATION_JSON_UTF8_VALUE, accountToCustomerContactAntiCorruptionLayerServiceHalJsonStrategy);
    }

    public Customer newCustomer(String customer, String mediaType){
        return (Customer) Optional.ofNullable(customerAntiCorruptationRegistry.get(mediaType))
                .map(antiCorruptationLayerStrategy -> antiCorruptationLayerStrategy.traslate(customer))
        .orElse(null);
    }

    public CustomerContact newCustomerContact(String gustomerContact, String mediaType){
        return (CustomerContact) Optional.ofNullable(customerContactAnticorruptationRegistry.get(mediaType))
                .map(antiCorruptationLayerStrategy -> antiCorruptationLayerStrategy.traslate(gustomerContact))
                .orElse(null);
    }
}
