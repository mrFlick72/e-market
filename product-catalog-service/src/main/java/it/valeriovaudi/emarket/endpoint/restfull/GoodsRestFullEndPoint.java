package it.valeriovaudi.emarket.endpoint.restfull;

import it.valeriovaudi.emarket.hateoas.GoodsHateoasFactory;
import it.valeriovaudi.emarket.model.Goods;
import it.valeriovaudi.emarket.service.GoodsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

/**
 * Created by mrflick72 on 09/05/17.
 */

@RestController
@RequestMapping("/goods")
public class GoodsRestFullEndPoint {

    private final GoodsService goodsService;
    private final GoodsHateoasFactory goodsHateoasFactory;

    public GoodsRestFullEndPoint(GoodsService goodsService,
                                 GoodsHateoasFactory goodsHateoasFactory) {
        this.goodsService = goodsService;
        this.goodsHateoasFactory = goodsHateoasFactory;
    }

    @GetMapping
    public ResponseEntity findAllGoods(){
        return ResponseEntity.ok(goodsHateoasFactory.toResources(goodsService.findGoodsList()));
    }

    @GetMapping("/{idGoods}")
    public ResponseEntity findGoods(@PathVariable String idGoods){
        return ResponseEntity.ok(goodsHateoasFactory.toResource(goodsService.findGoods(idGoods)));
    }

    @PostMapping
    public ResponseEntity createGoods(@RequestBody Goods goods){
        Goods goodsAux = goodsService.createGoods(goods);
        URI findGoods = MvcUriComponentsBuilder.fromMethodName(GoodsRestFullEndPoint.class,
                "findGoods", goodsAux.getId()).build().toUri();
        return ResponseEntity.created(findGoods).build();
    }

    @PatchMapping("/{idGoods}/category-attribute")
    public ResponseEntity saveGoodsAttributeValue(@PathVariable String idGoods, @RequestBody HashMap<String,String> goods){
        goods.entrySet().forEach(entry -> goodsService.saveGoodsAttributeValue(idGoods,entry.getKey(), entry.getValue()));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idGoods}")
    public ResponseEntity updateGoods(@PathVariable String idGoods, @RequestBody Goods goods){
        goods.setId(idGoods);
        goodsService.updateGoods(goods);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idGoods}")
    public ResponseEntity deleteGoods(@PathVariable String idGoods){
        goodsService.deleteGoods(idGoods);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idGoods}/category-attribute")
    public ResponseEntity removeGoodsAttributeValue(@PathVariable String idGoods, @PathVariable String goodsAttributeKey){
        goodsService.removeGoodsAttributeValue(idGoods, goodsAttributeKey);
        return ResponseEntity.noContent().build();
    }
}
