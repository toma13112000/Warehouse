package org.example.services;

import org.example.model.Brand;
import org.example.repository.BrandRepository;
import org.example.repository.ProductRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class BrandService {

    @EJB
    private BrandRepository brandRepository;

    @EJB
    private ProductRepository productRepository;

    public boolean isBrandNameExists(String brandName) {
        return brandRepository.existsByName(brandName);
    }

    public Brand addBrand(Brand brand) {
        validateBrandData(brand);
        return brandRepository.save(brand);
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + id + " not found"));
    }

    public Brand updateBrand(Long id, Brand updatedBrand) {
        return brandRepository.findById(id)
                .map(existingBrand -> {
                    if (updatedBrand.getName() != null) {
                        existingBrand.setName(updatedBrand.getName());
                    }
                    return existingBrand;
                })
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + id + " not found"));
    }

    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }

    private void validateBrandData(Brand brand) {
        if (brand.getName() == null || brand.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty");
        }
    }
    public boolean isProductNotFound(Long id) {
        return !productRepository.findById(id).isPresent();
    }


    private static class BrandNotFoundException extends RuntimeException {
        public BrandNotFoundException(String message) {
            super(message);
        }
    }
}