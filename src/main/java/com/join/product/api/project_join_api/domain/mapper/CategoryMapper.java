package com.join.product.api.project_join_api.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.join.product.api.project_join_api.domain.dto.CategoryDTO;
import com.join.product.api.project_join_api.domain.entity.Category;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mappings({
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "status", source = "status")
    })
    CategoryDTO categoryToCategoryDto(Category category);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "products", ignore = true)
    })
    Category categoryDtoToCategory(CategoryDTO categoryDTO);
}
