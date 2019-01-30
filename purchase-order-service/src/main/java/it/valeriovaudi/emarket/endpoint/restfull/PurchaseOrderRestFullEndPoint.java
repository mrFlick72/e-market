package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.PurchaseOrderHateoasFactory;
import it.valeriovaudi.emarket.model.PurchaseOrder;
import it.valeriovaudi.emarket.model.PurchaseOrderStatusEnum;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.PurchaseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

/**
 * Created by mrflick72 on 30/05/17.
 */


@Slf4j
@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final PurchaseOrderHateoasFactory purchaseOrderHateoasFactory;
    private final SecurityUtils securityUtils;

    public PurchaseOrderRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                         SecurityUtils securityUtils,
                                         PurchaseOrderHateoasFactory purchaseOrderHateoasFactory, SecurityUtils securityUtils1) {
        super(purchaseOrderService, securityUtils);
        this.purchaseOrderHateoasFactory = purchaseOrderHateoasFactory;
        this.securityUtils = securityUtils1;
    }

    @GetMapping
    public ResponseEntity getPuchaseOrderList(@RequestParam(value = "withOnlyOrderId", defaultValue = "false") boolean withOnlyOrderId){

        Boolean role_user = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));

        List<PurchaseOrder> purchaseOrders = role_user ?
                purchaseOrderService.findPurchaseOrderList(securityUtils.getPrincipalUserName(), withOnlyOrderId) :
                purchaseOrderService.findPurchaseOrderList(withOnlyOrderId);

        return ResponseEntity.ok(purchaseOrderHateoasFactory.toResources(purchaseOrders));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity getPuchaseOrder(@PathVariable String orderNumber){
        return ResponseEntity.ok(purchaseOrderHateoasFactory.toResource(purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber)));
    }

    @PostMapping
    public ResponseEntity createPuchaseOrder(){
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder();
        purchaseOrderService.withCustomerAndCustomerContact(purchaseOrder.getOrderNumber(), securityUtils.getPrincipalUserName(), null, null);

        return ResponseEntity.created(MvcUriComponentsBuilder.fromMethodName(PurchaseOrderRestFullEndPoint.class,
                "getPuchaseOrder",purchaseOrder.getOrderNumber()).build().toUri()).build();
    }

    @PatchMapping("/{orderNumber}")
    public ResponseEntity pathcPuchaseOrder(@PathVariable String orderNumber, @RequestBody String purchaseOrderStatusEnum){
        purchaseOrderService.changeStatus(orderNumber, PurchaseOrderStatusEnum.valueOf(purchaseOrderStatusEnum.toUpperCase()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity deletePuchaseOrder(@PathVariable String orderNumber){
        purchaseOrderService.deletePurchaseOrder(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
