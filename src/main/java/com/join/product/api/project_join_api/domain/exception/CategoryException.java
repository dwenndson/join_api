package com.join.product.api.project_join_api.domain.exception;

public class CategoryException extends RuntimeException{
    public CategoryException(String message){
        super(message);
    }

    public CategoryException() {
        super("Categoria não encontrada");
    }

}
