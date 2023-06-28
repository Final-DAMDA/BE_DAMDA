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

    @Pointcut("@annotation(com.damda.back.config.annotation.AdminBlocking)")
    private void adminBlocking() {}

    @Pointcut("@annotation(com.damda.back.config.annotation.UserBlocking)")
    private void userBlocking() {}



    /**
     * @apiNote 권한이 어드민이 아닐 경우 통과 어드민이면 막힘
     * */
    @Around("adminBlocking()")
    public Object aroundAdminBlocking(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object temp = request.getAttribute("role");
        String role = null;
        if(temp != null) role = temp.toString();

        log.info(" 해당 사용자 권한 AOP ===> {}",role);
        if(role != null && role.equals("USER")){
            return proceedingJoinPoint.proceed();
        }else {
            HttpServletResponse response =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            Gson gson = new Gson();

            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .codeEnum(CodeEnum.NO_PERMISSIONS)
                    .build();
            String json = gson.toJson(commonResponse);

            log.warn("권한체크 포인트를 잘못요청함 {}", role);

            return ResponseEntity.status(403).body(json);
        }
    }


    @Around("userBlocking()")
    public Object aroundUserBlocking(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object temp = request.getAttribute("role");
        String role = null;
        if(temp != null) role = temp.toString();

        log.info(" 해당 사용자 권한 ===> {}",role);
        if(role != null && (!role.equals("USER"))){
            return proceedingJoinPoint.proceed();
        }else {
            HttpServletResponse response =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            Gson gson = new Gson();

            CommonResponse<?> commonResponse = CommonResponse.builder()
                    .codeEnum(CodeEnum.NO_PERMISSIONS)
                    .build();
            String json = gson.toJson(commonResponse);

            log.warn("권한체크 포인트를 잘못요청함 {}", role);

            return ResponseEntity.status(403).body(json);
        }


    }

}
