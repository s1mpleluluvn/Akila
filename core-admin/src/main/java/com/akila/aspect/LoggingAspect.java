/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.aspect;

import com.akila.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

@Aspect
@Component
@Log4j2
@RequiredArgsConstructor
public class LoggingAspect {

    @Autowired(required = false)
    private HttpServletRequest request;

    // Pointcut that matches all Mobile Controller endpoints
    @Pointcut("execution(* com.akila.controller..*(..))")
    public void akilaController() {
    }

    @Around("akilaController()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String uuid = UUID.randomUUID().toString();
        request.setAttribute("uuid", uuid);
        String api = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();
        String className = joinPoint.getTarget().getClass().getName();
        Object data = null, dataParams = null;
        Map<String, String> returnObjectParam = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        List<String> list = Collections.list(parameterNames);
        if (joinPoint.getArgs().length == 1) {
            var classNameJoin = joinPoint.getArgs()[0].getClass().toString();
            if (classNameJoin.equals("class org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile")) {
                MultipartFile vars = (MultipartFile) joinPoint.getArgs()[0];
                data = vars.getOriginalFilename();
            } else {
                if (!classNameJoin.contains("org.springframework")) {
                    data = joinPoint.getArgs()[0];
                }
            }
        } else if (joinPoint.getArgs().length > 1) {
            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                if (joinPoint.getArgs()[i] == null) {
                    continue;
                }
                var classNameJoin = joinPoint.getArgs()[i].getClass().toString();
                var paramName = codeSignature.getParameterNames()[i];
                if ("identifyDeviceToken".equals(paramName) || "httpServletRequest".equals(paramName)) {
                    continue;
                }
                if (!classNameJoin.equals("class org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile")) {
                    if (classNameJoin.contains("org.springframework")) {
                        continue;
                    }
                }
                if (list.contains(codeSignature.getParameterNames()[i])) {
                    returnObjectParam.put(codeSignature.getParameterNames()[i], joinPoint.getArgs()[i].toString());
                } else {
                    switch (classNameJoin) {
                        case "class org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile":
                            MultipartFile vars = (MultipartFile) joinPoint.getArgs()[i];
                            returnObjectParam.put(codeSignature.getParameterNames()[i], vars.getOriginalFilename());
                            break;
                        case "class java.lang.String":
                            returnObjectParam.put(codeSignature.getParameterNames()[i], classNameJoin);
                            break;
                        default:
                            data = joinPoint.getArgs()[i];
                            break;
                    }
                }
            }
            if (!returnObjectParam.isEmpty()) {
                dataParams = returnObjectParam;
            }
        }
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        LoggerJson loggerJsonInput = new LoggerJson(userName, uuid, "INPUT", api, ipAddress, data,
                dataParams, className, null);
        log.info(JsonUtil.toJson(loggerJsonInput));
        try {
            Long timeStart = System.currentTimeMillis();

            Long timeExec = System.currentTimeMillis() - timeStart;
            Object result = joinPoint.proceed();
            if (result == null) {
                LoggerJson loggerJsonOutput = new LoggerJson(userName, uuid, "OUTPUT", api, ipAddress,
                        result, null, className, timeExec);
                log.info(JsonUtil.toJson(loggerJsonOutput));
                return result;
            }
            if (result.getClass() == ResponseEntity.class) {
                LoggerJson loggerJsonOutput = new LoggerJson(userName, uuid, "OUTPUT", api, ipAddress,
                        ((ResponseEntity) result).getStatusCode().toString(), null, className, timeExec);
                log.info(JsonUtil.toJson(loggerJsonOutput));
            } else {
                LoggerJson loggerJsonOutput = new LoggerJson(userName, uuid, "OUTPUT", api, ipAddress,
                        result, null, className, timeExec);
                log.info(JsonUtil.toJson(loggerJsonOutput));
            }
            return result;
        } catch (Exception ex) {
            LoggerJson loggerJsonError = new LoggerJson(userName, uuid, "ERROR", api, ipAddress,
                    ex.toString(), null, className, null);
            log.error(JsonUtil.toJson(loggerJsonError));
            throw ex;
        }
    }
}
