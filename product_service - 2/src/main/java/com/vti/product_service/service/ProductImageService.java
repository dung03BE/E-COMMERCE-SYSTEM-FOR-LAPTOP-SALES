package com.vti.product_service.service;

import com.vti.product_service.entity.ProductImage;
import com.vti.product_service.repository.IProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService implements IProductImageService{
    @Autowired
    private IProductImageRepository productImageRepository;
    @Override
    public ProductImage getProductImageById(int imageId) {
        return productImageRepository.findById(imageId).get();
    }

    @Override
    public void updateProductImage(ProductImage existingImage) {
        productImageRepository.save(existingImage);
    }
}
