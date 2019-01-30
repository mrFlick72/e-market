package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.DeliveryHateoasFactory;
import it.valeriovaudi.emarket.model.Delivery;
import it.valeriovaudi.emarket.model.PurchaseOrder;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mrflick72 on 13/06/17.
 */

@RestController
@RequestMapping("/purchase-order")
public class DeliveryRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final DeliveryHateoasFactory deliveryHateoasFactory;

    public DeliveryRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                    SecurityUtils securityUtils, DeliveryHateoasFactory deliveryHateoasFactory) {
        super(purchaseOrderService, securityUtils);
        this.deliveryHateoasFactory = deliveryHateoasFactory;
    }

    @GetMapping("/{orderNumber}/delivery")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getDeliveryDataPuchaseOrder(@PathVariable String orderNumber){
        PurchaseOrder purchaseOrder =
                purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber);
        return ResponseEntity.ok(deliveryHateoasFactory.toResource(orderNumber, purchaseOrder.getDelivery()));
    }

    @PutMapping("/{orderNumber}/delivery")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity deliveryDataPuchaseOrder(@PathVariable String orderNumber, @RequestBody Delivery delivery){
        purchaseOrderService.withDelivery(orderNumber, delivery);
        return ResponseEntity.noContent().build();
    }

}
