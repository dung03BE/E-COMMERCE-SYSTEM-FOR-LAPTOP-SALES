package com.vti.product_service.specification;

import com.vti.product_service.entity.Product;
import com.vti.product_service.form.ProductFilterForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;


public class ProductSpecification {
    private static  final String SEARCH ="search";
    private static final String MAX_PRICE ="max_price";
    private static  final String MIN_PRICE = "min_price";
    public static Specification<Product> buildWhere(ProductFilterForm form)
    {
        if(form==null)
        {
            return null;
        }
        Specification<Product> whereProductName = new SpecificationIml(SEARCH,form.getSearch());
        Specification<Product> whereMinPrice=new SpecificationIml(MIN_PRICE,form.getMinPrice());
        Specification<Product> whereMaxPrice = new SpecificationIml(MAX_PRICE,form.getMaxPrice());
        return Specification.where(whereProductName).and(whereMinPrice).and(whereMaxPrice);
    }

    @AllArgsConstructor
    public static  class SpecificationIml implements  Specification<Product>
    {
        private String key;
        private Object value;

        @Override
        public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            if(value==null)
            {
                return  null;
            }
            switch (key)
            {
                case SEARCH:
                    return criteriaBuilder.like(root.get("name"),"%"+value+"%");
                case MIN_PRICE:
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("price"),value.toString());
                case MAX_PRICE:
                    return criteriaBuilder.lessThanOrEqualTo(root.get("price"),value.toString());
            }
            return null;
        }
    }
}
