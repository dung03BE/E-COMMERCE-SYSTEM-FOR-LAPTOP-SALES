package com.vti.product_service.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

}
