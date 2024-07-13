package com.join.product.api.project_join_api.domain.bussines;

import java.util.List;

import com.join.product.api.project_join_api.domain.dto.CategoryDTO;
public interface CategoryService {

    CategoryDTO getCategoryByName(String name);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> findAll();
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    
}
