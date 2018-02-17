package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.security.SecurityUtils;
import it.valeriovaudi.emarket.service.PurchaseOrderService;

/**
 * Created by mrflick72 on 13/06/17.
 */
public abstract class AbstractPurchaseOrderRestFullEndPoint {

    protected final PurchaseOrderService purchaseOrderService;
    protected final SecurityUtils securityUtils;

    protected AbstractPurchaseOrderRestFullEndPoint(PurchaseOrderService purchaseOrderService,
                                                    SecurityUtils securityUtils) {
        this.purchaseOrderService = purchaseOrderService;
        this.securityUtils = securityUtils;
    }
}
