package com.vti.product_service.service;

import com.vti.product_service.entity.ProductImage;

public interface IProductImageService {
    ProductImage getProductImageById(int imageId);

    void updateProductImage(ProductImage existingImage);
}
