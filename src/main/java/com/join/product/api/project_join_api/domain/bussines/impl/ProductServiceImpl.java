package com.join.product.api.project_join_api.domain.bussines.impl;

import com.join.product.api.project_join_api.domain.bussines.ProductService;
import com.join.product.api.project_join_api.domain.dto.ProductDTO;
import com.join.product.api.project_join_api.domain.entity.Category;
import com.join.product.api.project_join_api.domain.entity.Product;
import com.join.product.api.project_join_api.domain.exception.CategoryException;
import com.join.product.api.project_join_api.domain.exception.ProductException;
import com.join.product.api.project_join_api.domain.mapper.ProductMapper;
import com.join.product.api.project_join_api.infra.repository.CategoryRepository;
import com.join.product.api.project_join_api.infra.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ProductDTO getProductByName(String name) throws ProductException {
        Optional<Product> productOptional = productRepository.findByName(name);
        Product product = productOptional.orElseThrow(() -> new ProductException("Produto não encontrado com nome: " + name));
        return ProductMapper.INSTANCE.productToProductDto(product);    }

    @Override
    public ProductDTO getProductById(Long id) throws ProductException {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new ProductException("Produto não encontrado com id: " + id));
        return ProductMapper.INSTANCE.productToProductDto(product);    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper.INSTANCE::productToProductDto)
                .toList();    }

    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            Product product = ProductMapper.INSTANCE.productDTOToProduct(productDTO);

            Product savedProduct = productRepository.save(product);

            return ProductMapper.INSTANCE.productToProductDto(savedProduct);
        } catch (CategoryException e) {
            logger.error("Erro ao validar a categoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar o produto", e);
            throw new RuntimeException("Erro inesperado ao criar o produto");
        }    }

    @Transactional
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) throws ProductException {
        try {
            logger.info("Iniciando atualização do produto com id: {}", id);
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                logger.warn("Produto com id: {} não encontrado", id);
                throw new ProductException("Produto não encontrado com id: " + id);
            }

            Product product = productOptional.get();
            product.setName(productDTO.name());
            product.setDescription(productDTO.description());
            product.setPrice(productDTO.price());

            Optional<Category> categoryOptional = categoryRepository.findByName(productDTO.categoryDto().name());
            if (categoryOptional.isEmpty()) {
                throw new CategoryException("Categoria não encontrada com id: " + productDTO.categoryDto().name());
            }
            product.setCategory(categoryOptional.get());

            Product updatedProduct = productRepository.save(product);
            logger.info("Produto com id: {} atualizado com sucesso", id);

            return ProductMapper.INSTANCE.productToProductDto(updatedProduct);
        } catch (ProductException | CategoryException e) {
            logger.error("Erro ao validar o produto ou categoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar o produto com id: {}", id, e);
            throw new RuntimeException("Erro inesperado ao atualizar o produto com id: " + id);
        }    }

    @Transactional
    @Override
    public void deleteProduct(Long id) throws ProductException {
        try {
            logger.info("Iniciando deleção do produto com id: {}", id);
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                logger.warn("Produto com id: {} não encontrado", id);
                throw new ProductException("Produto não encontrado com id: " + id);
            }

            productRepository.deleteById(id);
            logger.info("Produto com id: {} deletado com sucesso", id);
        } catch (ProductException e) {
            logger.error("Erro ao validar o produto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar o produto com id: {}", id, e);
            throw new RuntimeException("Erro inesperado ao deletar o produto com id: " + id);
        }
    }
}
