package com.damda.back.config.advice;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.exception.CommonException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestAdvice {

        @ExceptionHandler(CommonException.class)
        public ResponseEntity<CommonResponse<?>> commonExceptionResponseEntity(CommonException e){

            CommonResponse<?> commonResponse = new CommonResponse<>(e.getErrorCode())
                    .data(false);

            return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                    .body(commonResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<CommonResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException e){
            CommonResponse<?> commonResponse =
                    CommonResponse.builder()
                            .data(e.getFieldError().getField() +"이(가) 이상한 값으로 들어왔습니다")
                            .codeEnum(CodeEnum.BAD_REQUEST)
                            .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(commonResponse);

        }
}
