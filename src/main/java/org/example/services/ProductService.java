package org.example.services;

import org.example.model.Brand;
import org.example.model.Product;
import org.example.repository.BrandRepository;
import org.example.repository.ProductRepository;
import org.example.exceptions.BrandNotFoundException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class ProductService {

    @EJB
    private BrandRepository brandRepository;
    @EJB
    private ProductRepository productRepository;

    public boolean isProductNameExists(String productName) {
        return productRepository.existsByName(productName);
    }

    public Product addProduct(Product product) {
        validateProductData(product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    if (updatedProduct.getName() != null) {
                        existingProduct.setName(updatedProduct.getName());
                    }
                    if (updatedProduct.getBrand() != null && updatedProduct.getBrand().getId() != null) {
                        Brand brand = brandRepository.findById(updatedProduct.getBrand().getId())
                                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + updatedProduct.getBrand().getId() + " not found"));
                        existingProduct.setBrand(brand);
                    }
                    return existingProduct;
                })
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProductData(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getBrand() == null || product.getBrand().getId() == null) {
            throw new IllegalArgumentException("Product brand cannot be null");
        }
    }
    public boolean isBrandNotFound(Long id) {
        return !brandRepository.findById(id).isPresent();
    }

    private static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}