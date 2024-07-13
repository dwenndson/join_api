package com.join.product.api.project_join_api.domain.bussines.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.join.product.api.project_join_api.domain.bussines.CategoryService;
import com.join.product.api.project_join_api.domain.dto.CategoryDTO;
import com.join.product.api.project_join_api.domain.entity.Category;
import com.join.product.api.project_join_api.domain.exception.CategoryException;
import com.join.product.api.project_join_api.domain.mapper.CategoryMapper;
import com.join.product.api.project_join_api.infra.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryServiceImpl implements CategoryService{

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public CategoryDTO getCategoryByName(String name) {
        logger.info("Iniciando buscar por categoria");
        Optional<Category> category = categoryRepository.findByName(name);
        
        valid_category_present(category.get());
        
        logger.info("Consulta efetuada com sucesso");
        
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category.get());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws CategoryException {
        logger.info("Iniciando buscar por id");
        Category category = categoryRepository.findById(id).orElseThrow(CategoryException::new);

        logger.info("Consulta efetuada com sucesso");

        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDTO> findAll() {
        logger.info("Iniciando buscar por categoria");

        List<Category> categories = categoryRepository.findAll();

        logger.info("Iniciando buscar por categoria");

        return categories.stream()
        .map(CategoryMapper.INSTANCE::categoryToCategoryDto)
        .toList();
    }

    @Transactional
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        try {
            validCategoryExist(categoryDTO.name());
            Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryDTO);

            Category savedCategory = categoryRepository.save(category);
            return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
        } catch (CategoryException e) {
            logger.error("Erro ao validar a categoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar a categoria", e);
            throw new RuntimeException("Erro inesperado ao criar a categoria");
        }
    }

    private void validCategoryExist(String name) {
        Optional<Category> validate = categoryRepository.findByName(name);
        if (validate.isPresent()){
            logger.warn("Categoria já cadastrada");
            throw new CategoryException("Categoria já existe na base de dados: " + name);
        }
    }

    @Transactional
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        try {
            logger.info("Iniciando atualização da categoria com id: {}", id);
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isEmpty()) {
                logger.warn("Categoria com id: {} não encontrada", id);
                throw new CategoryException("Categoria não encontrada com id: " + id);
            }

            Category category = categoryOptional.get();
            category.setName(categoryDTO.name());
            category.setDescription(categoryDTO.description());
            category.setStatus(true);

            Category updatedCategory = categoryRepository.save(category);

            logger.info("Categoria com id: {} atualizada com sucesso", id);

            return CategoryMapper.INSTANCE.categoryToCategoryDto(updatedCategory);
        } catch (CategoryException e) {
            logger.error("Erro ao validar a categoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar a categoria com id: {}", id, e);
            throw new RuntimeException("Erro inesperado ao atualizar a categoria com id: " + id);
        }
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        try {
            logger.info("Iniciando deleção da categoria com id: {}", id);
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isEmpty()) {
                logger.warn("Categoria com id: {} não encontrada", id);
                throw new CategoryException("Categoria não encontrada com id: " + id);
            }

            categoryRepository.deleteById(id);
            logger.info("Categoria com id: {} deletada com sucesso", id);
        } catch (CategoryException e) {
            logger.error("Erro ao validar a categoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar a categoria com id: {}", id, e);
            throw new RuntimeException("Erro inesperado ao deletar a categoria com id: " + id);
        }
    }

    private void valid_category_present(Category category) throws CategoryException {
        if (category != null) {
            logger.warn("Categoria não foi encontrar no banco de dados");
            throw new CategoryException("Categoria não encontrada " + category.getName());
        }
    }

}
