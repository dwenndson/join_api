package com.join.product.api.project_join_api.domain.bussines;

import com.join.product.api.project_join_api.domain.dto.ProductDTO;
import com.join.product.api.project_join_api.domain.exception.ProductException;

import java.util.List;

public interface ProductService {
    ProductDTO getProductByName(String name) throws ProductException;
    ProductDTO getProductById(Long id) throws ProductException;
    List<ProductDTO> findAll();
    ProductDTO createProduct(ProductDTO ProductDTO);
    ProductDTO updateProduct(Long id, ProductDTO ProductDTO) throws ProductException;
    void deleteProduct(Long id) throws ProductException;
}
