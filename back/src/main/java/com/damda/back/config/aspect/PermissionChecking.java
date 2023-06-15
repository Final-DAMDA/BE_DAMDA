package com.damda.back.config.aspect;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Slf4j
@Component
public class PermissionChecking {

    @Pointcut("@annotation(com.damda.back.config.annotation.Permission)")
    private void enablePermission() {}



    @Around("enablePermission()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object temp = request.getAttribute("role");
        String role = null;

        if(temp != null) role = temp.toString();

        log.info(" 해당 사용자 권한 ===> {}",role);
        if(role != null && (role.equals("\"ADMIN\""))){
            return proceedingJoinPoint.proceed();
        }else {
            HttpServletResponse response =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            //RequestContextHolder를 사용하면 현재 쓰레드에서 처리되고 있는 요청에 관한 HttpServletRequest와 HttpServletResponse를 가져올 수 있음
            Gson gson = new Gson();

            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .codeEnum(CodeEnum.NO_PERMISSIONS)
                    .build();
            String json = gson.toJson(commonResponse);

            log.warn("Attempt to access resources without permission {}", role);

            return ResponseEntity.status(403).body(json);
        }
    }

}
