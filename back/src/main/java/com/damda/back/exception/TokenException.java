package com.damda.back.exception;

import com.damda.back.data.common.CommonResponse;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenException extends RuntimeException{
    TOKEN_ERROR tokenError;


    @Getter
    public enum TOKEN_ERROR{
        UNACCEPT(401,"로그인을 해주세요 토큰이 없습니다."), //토큰이 없거나 토큰 길이가 짧거나
        BADTYPE(401,"쿠키 형식이 이상합니다."), //Bearer이 안 지켜졌을떄
        MALFORM(403,"토큰 형식이 위조되었습니다."), //형태가 이상한 토큰
        BADSIGN(403,"토큰을 누군가 위조했습니다."), //누군가 위조
        EXPIRED(403,"토큰이 만료되었습니다."); // 만료

        private int status;
        private String msg;

        TOKEN_ERROR(int status,String msg){
            this.status = status;
            this.msg = msg;
        }
    }
    public TokenException(TOKEN_ERROR tokenError){
        super(tokenError.msg);
        this.tokenError = tokenError;
    }
    public void sendResponseError(HttpServletResponse response){
        response.setStatus(tokenError.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        CommonResponse<?> responseDto = new CommonResponse()
                        .tokenError(tokenError)
                        .data(false);


        String result = gson.toJson(responseDto);

        try{
            response.getWriter().println(result);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
