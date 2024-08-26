package com.vti.product_service.service;

import com.vti.product_service.dto.ProductImageDto;
import com.vti.product_service.entity.Product;
import com.vti.product_service.entity.ProductImage;
import com.vti.product_service.exception.DataNotFoundException;
import com.vti.product_service.form.CreatingProductForm;
import com.vti.product_service.form.ProductFilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {

    Page<Product> getAllProducts(Pageable pageable, ProductFilterForm form);

    Product createProduct(CreatingProductForm form);

    Product getProductById(int id) throws DataNotFoundException;

    ProductImage createProductImage(int id, ProductImageDto productImageDto);

    void updateProduct(int id, CreatingProductForm form) throws DataNotFoundException;

    ProductImage updateProductImage(int id, ProductImageDto build);

    void deleteProduct(int id) throws DataNotFoundException;
}
