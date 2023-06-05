package com.damda.back.data.common;

import com.damda.back.exception.ErrorCode;
import com.damda.back.exception.TokenException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T data;


    @Builder
    public CommonResponse(CodeEnum codeEnum,T data){
        setData(data);
        setStatus(codeEnum.getCode());
        setMessage(codeEnum.getMessage());
    }


    public CommonResponse<?> tokenError(TokenException.TOKEN_ERROR tokenError){
        setStatus(tokenError.getStatus());
        setMessage(tokenError.getMsg());
        return this;
    }

    public CommonResponse<?> data(T data){
        this.data = data;
        return this;
    }


    public CommonResponse(ErrorCode errorCode){
        setStatus(errorCode.getHttpStatus().value());
        setMessage(errorCode.getMessage());
    }

}
