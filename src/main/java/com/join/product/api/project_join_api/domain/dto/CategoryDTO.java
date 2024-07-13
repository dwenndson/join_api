package com.join.product.api.project_join_api.domain.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(String name, String description, boolean status) {}
