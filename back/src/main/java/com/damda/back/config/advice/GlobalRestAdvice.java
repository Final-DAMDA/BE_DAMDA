package com.damda.back.config.advice;


import com.damda.back.data.common.CommonResponse;
import com.damda.back.exception.CommonException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestAdvice {

        @ExceptionHandler(CommonException.class)
        public ResponseEntity<CommonResponse<?>> commonExceptionResponseEntity(CommonException e){

            CommonResponse<?> commonResponse = new CommonResponse<>(e.getErrorCode());

            return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                    .body(commonResponse);
        }
}
