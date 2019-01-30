package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.endpoint.response.CustomerDataResponseDTO;
import it.valeriovaudi.emarket.hateoas.CustomerHateoasFactory;
import it.valeriovaudi.emarket.model.PurchaseOrder;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by mrflick72 on 13/06/17.
 */

@RestController
@RequestMapping("/purchase-order")
public class CustomerRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final CustomerHateoasFactory customerHateoasFactory;

    public CustomerRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                    SecurityUtils securityUtils,
                                    CustomerHateoasFactory customerHateoasFactory) {
        super(purchaseOrderService, securityUtils);
        this.customerHateoasFactory = customerHateoasFactory;
    }

    @GetMapping("/{orderNumber}/customer")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getCustomerDataPuchaseOrder(@PathVariable String orderNumber){
        PurchaseOrder purchaseOrder = purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber);

        CustomerDataResponseDTO customerDataResponseDTO =
                CustomerDataResponseDTO.builder()
                        .customer(purchaseOrder.getCustomer())
                .customerContact(purchaseOrder.getCustomerContact()).build();

        return ResponseEntity.ok(customerHateoasFactory.toResource(orderNumber, customerDataResponseDTO));
    }

    @PatchMapping("/{orderNumber}/customer")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity customerDataPuchaseOrder(@PathVariable String orderNumber, Principal principal){
        purchaseOrderService.withCustomerAndCustomerContact(orderNumber, principal.getName(), null, null);
        return ResponseEntity.noContent().build();
    }

}
