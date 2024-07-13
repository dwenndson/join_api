package com.join.product.api.project_join_api.domain.exception;

public class ExpiredJwtException  extends RuntimeException{

    public ExpiredJwtException(String message){
        super(message);
    }

}
