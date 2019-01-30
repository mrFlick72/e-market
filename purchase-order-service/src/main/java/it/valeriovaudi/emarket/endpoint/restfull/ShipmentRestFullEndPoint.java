package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.ShipmentHateoasFactory;
import it.valeriovaudi.emarket.model.PurchaseOrder;
import it.valeriovaudi.emarket.model.Shipment;
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
public class ShipmentRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final ShipmentHateoasFactory shipmentHateoasFactory;

    public ShipmentRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                    SecurityUtils securityUtils,
                                    ShipmentHateoasFactory shipmentHateoasFactory) {
        super(purchaseOrderService, securityUtils);
        this.shipmentHateoasFactory = shipmentHateoasFactory;
    }

    @GetMapping("/{orderNumber}/shipment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getShipmentDataPuchaseOrder(@PathVariable String orderNumber){
        PurchaseOrder purchaseOrder =
                purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber);
        return ResponseEntity.ok(shipmentHateoasFactory.toResource(orderNumber, purchaseOrder.getShipment()));
    }
    @PutMapping("/{orderNumber}/shipment")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity shipmentDataPuchaseOrder(@PathVariable String orderNumber, @RequestBody Shipment shipment){
        purchaseOrderService.withShipment(orderNumber, shipment);
        return ResponseEntity.noContent().build();
    }
}
