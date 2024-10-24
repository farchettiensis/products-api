package tech.ada.products_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.products_api.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    static List<ProductDTO> products = new ArrayList<>();

    //    @RequestMapping(method = RequestMethod.POST, value = "/")
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        // validar
        // gravar na db
        products.add(productDTO);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDTO> listAll() {
        return products;
    }

    @PutMapping
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO) {
        ProductDTO productDB = products.stream()
                .filter(p -> productDTO.getSku().equalsIgnoreCase(p.getSku()))
                .findFirst()
                .orElseThrow();

        int index = products.indexOf(productDB);
        productDB.setPrice(productDTO.getPrice());

        products.set(index, productDB);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productDB);
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> delete(@PathVariable("sku") String sku) {
        ProductDTO productDB = products.stream()
                .filter(p -> sku.equalsIgnoreCase(p.getSku()))
                .findFirst()
                .orElseThrow();

        products.remove(productDB);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
