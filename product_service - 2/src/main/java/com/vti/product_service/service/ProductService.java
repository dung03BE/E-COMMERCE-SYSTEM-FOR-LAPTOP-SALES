package com.vti.product_service.service;

import com.vti.product_service.config.RabbitMQConfig;
import com.vti.product_service.dto.ProductImageDto;
import com.vti.product_service.entity.Product;
import com.vti.product_service.entity.ProductCreatedEvent;
import com.vti.product_service.entity.ProductImage;
import com.vti.product_service.exception.DataNotFoundException;
import com.vti.product_service.form.CreatingProductForm;
import com.vti.product_service.form.ProductFilterForm;
import com.vti.product_service.repository.IProductImageRepository;
import com.vti.product_service.repository.IProductRepository;
import com.vti.product_service.specification.ProductSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductImageRepository productImageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductFilterForm form) {
        Specification<Product> where = ProductSpecification.buildWhere(form);
        return productRepository.findAll(where,pageable);
    }

    @Override
    public Product createProduct(CreatingProductForm form) {
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setDescription(form.getDescription());

        productRepository.save(product);
        ProductCreatedEvent event  = new ProductCreatedEvent(product.getId());
        rabbitTemplate.convertAndSend("productExchange", "product.created", event);
        return product;
    }

    @Override
    public Product getProductById(int id) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Dont find Product with Id="+id)
        );
        return existingProduct ;
    }

    @Override
    public ProductImage createProductImage(int id, ProductImageDto productImageDto) {
        Product existingProduct =productRepository.findById(id).get();
        ProductImage productImage =ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDto.getImageUrl())
                .build();
        int size =productImageRepository.findByProductId(id).size();
        if(size>=ProductImage.Maximum_Images_Per_Product)
        {
            throw new InvalidParameterException("Number of images must be <="+
                    ProductImage.Maximum_Images_Per_Product);
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public void updateProduct(int id, CreatingProductForm form) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Dont find Product with id="+id)
        );
        form.setId(id);
        Product product =modelMapper.map(form,Product.class);
        productRepository.save(product);
    }

    @Override
    public ProductImage updateProductImage(int id, ProductImageDto productImageDto) {
        Product existingProduct =productRepository.findById(id).get();
        ProductImage productImage =ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDto.getImageUrl())
                .build();
        int size =productImageRepository.findByProductId(id).size();
        if(size>=ProductImage.Maximum_Images_Per_Product)
        {
            throw new InvalidParameterException("Number of images must be <="+
                    ProductImage.Maximum_Images_Per_Product);
        }
        return productImageRepository.save(productImage);
    }

    @Override
    public void deleteProduct(int id) throws DataNotFoundException {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Dont find product with id:"+id)
        );
        productRepository.deleteById(id);
    }
}
