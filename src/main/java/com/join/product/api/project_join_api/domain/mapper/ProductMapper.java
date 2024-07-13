package com.join.product.api.project_join_api.domain.mapper;

import com.join.product.api.project_join_api.domain.dto.ProductDTO;
import com.join.product.api.project_join_api.domain.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "categoryDto", source = "category")
    })
    ProductDTO productToProductDto(Product product);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "price", source = "price"),
            @Mapping(target = "category", source = "categoryDto")
    })
    Product productDTOToProduct(ProductDTO productDTO);

}
