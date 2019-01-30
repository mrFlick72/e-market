package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.PurchaseOrderHateoasFactory;
import it.valeriovaudi.emarket.model.PurchaseOrder;
import it.valeriovaudi.emarket.model.PurchaseOrderStatusEnum;
import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.PurchaseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.security.Principal;
import java.util.List;

/**
 * Created by mrflick72 on 30/05/17.
 */


@Slf4j
@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final PurchaseOrderHateoasFactory purchaseOrderHateoasFactory;

    public PurchaseOrderRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                         SecurityUtils securityUtils,
                                         PurchaseOrderHateoasFactory purchaseOrderHateoasFactory) {
        super(purchaseOrderService, securityUtils);
        this.purchaseOrderHateoasFactory = purchaseOrderHateoasFactory;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity getPuchaseOrderList(@RequestParam(value = "withOnlyOrderId", defaultValue = "false") boolean withOnlyOrderId){

        Boolean role_user = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));

        List<PurchaseOrder> purchaseOrders = role_user ?
                purchaseOrderService.findPurchaseOrderList(securityUtils.getPrincipalUserName(), withOnlyOrderId) :
                purchaseOrderService.findPurchaseOrderList(withOnlyOrderId);

        return ResponseEntity.ok(purchaseOrderHateoasFactory.toResources(purchaseOrders));
    }

    @GetMapping("/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getPuchaseOrder(@PathVariable String orderNumber){
        return ResponseEntity.ok(purchaseOrderHateoasFactory.toResource(purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity createPuchaseOrder(Principal principal){
        PurchaseOrder purchaseOrder = purchaseOrderService.createPurchaseOrder();
        purchaseOrderService.withCustomerAndCustomerContact(purchaseOrder.getOrderNumber(), principal.getName(), null, null);

        return ResponseEntity.created(MvcUriComponentsBuilder.fromMethodName(PurchaseOrderRestFullEndPoint.class,
                "getPuchaseOrder",purchaseOrder.getOrderNumber()).build().toUri()).build();
    }

    @PatchMapping("/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity pathcPuchaseOrder(@PathVariable String orderNumber, @RequestBody String purchaseOrderStatusEnum){
        purchaseOrderService.changeStatus(orderNumber, PurchaseOrderStatusEnum.valueOf(purchaseOrderStatusEnum.toUpperCase()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{orderNumber}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity deletePuchaseOrder(@PathVariable String orderNumber){
        purchaseOrderService.deletePurchaseOrder(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
