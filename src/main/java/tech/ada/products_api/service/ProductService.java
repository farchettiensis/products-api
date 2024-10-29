package tech.ada.products_api.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.products_api.dto.ProductDTO;
import tech.ada.products_api.model.Product;
import tech.ada.products_api.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO create(ProductDTO productDTO) {
        Product product = new Product();
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());

        productRepository.save(product);

        return productDTO;
    }

    public List<ProductDTO> listAll() {
        return productRepository.findAll().stream()
                .map(product -> {
                    return toProductDTO(product);
                })
                .collect(Collectors.toList());
    }

    private ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSku(product.getSku());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        return productDTO;
    }

    public ProductDTO getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .map(this::toProductDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + sku));
    }

    public List<ProductDTO> findByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO update(ProductDTO productDTO) {
        Product product = productRepository
                .findBySku(productDTO.getSku())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + productDTO.getSku()));

        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());

        productRepository.save(product);

        return productDTO;
    }

    public void delete(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + sku));

        productRepository.delete(product);
    }
}
