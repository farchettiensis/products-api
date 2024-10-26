package tech.ada.products_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.products_api.dto.ProductDTO;
import tech.ada.products_api.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "Product")
public class ProductController {
    static List<ProductDTO> products = new ArrayList<>();

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //    @RequestMapping(method = RequestMethod.POST, value = "/")
    @Operation(summary = "Create a new product", description = "Creates a new product and adds it to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDTO.class),
                    examples = @ExampleObject(
                            value = "{ \"name\": \"Sample Product\", \"price\": 99.99, \"quantity\": 10 }"
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productDTO));
    }

    @Operation(summary = "List all products", description = "Lists all products in the database.")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDTO> listAll() {
        return products;
    }

    @Operation(summary = "Update a product", description = "Updates a product in the database.")
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

    @Operation(summary = "Delete a product", description = "Deletes a product from the database.")
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
