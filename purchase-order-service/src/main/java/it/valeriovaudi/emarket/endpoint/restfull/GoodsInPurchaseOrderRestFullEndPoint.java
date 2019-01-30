package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.GoodsInPurchaseOrderHateoasFactory;
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
public class GoodsInPurchaseOrderRestFullEndPoint extends AbstractPurchaseOrderRestFullEndPoint {

    private final GoodsInPurchaseOrderHateoasFactory goodsInPriceListHateoasFactory;

    public GoodsInPurchaseOrderRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                                SecurityUtils securityUtils,
                                                GoodsInPurchaseOrderHateoasFactory goodsInPriceListHateoasFactory) {
        super(purchaseOrderService, securityUtils);
        this.goodsInPriceListHateoasFactory = goodsInPriceListHateoasFactory;
    }

    @GetMapping("/{orderNumber}/goods")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity getGoodsDataInPuchaseOrder(@PathVariable String orderNumber){
        PurchaseOrder purchaseOrder =
                purchaseOrderService.findPurchaseOrder(securityUtils.getPrincipalUserName(), orderNumber);
        return ResponseEntity.ok(goodsInPriceListHateoasFactory.toResources(orderNumber, purchaseOrder.getGoodsList()));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/{orderNumber}/goods/{goods}/price-list/{priceList}")
    public ResponseEntity saveGoodsDataInPuchaseOrder(@PathVariable String orderNumber, @PathVariable String priceList,
                                                      @PathVariable String goods, @RequestBody Integer quantity){
        purchaseOrderService.saveGoodsInPurchaseOrder(orderNumber, priceList, goods, quantity);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{orderNumber}/goods/{goods}/price-list/{priceList}")
    public ResponseEntity removeGoodsDataInPuchaseOrder(@PathVariable String orderNumber, @PathVariable String priceList,
                                                        @PathVariable String goods){
        purchaseOrderService.removeGoodsInPurchaseOrder(orderNumber, priceList,goods);
        return ResponseEntity.noContent().build();
    }

}
