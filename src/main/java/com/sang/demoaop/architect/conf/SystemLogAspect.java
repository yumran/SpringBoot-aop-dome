package com.sang.demoaop.architect.conf;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Aspect
@Component
public class SystemLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal beginTime");

    /**
     * controller层注解，注解方式
     */
    @Pointcut("@annotation(com.sang.demoaop.architect.annotation.SystemControllerLog)")
    public void controllerAspect() {
        logger.info("==================controllerAspect==================");
    }

    /**
     * 前置条件（在方法执行之前返回）用于拦截controller层记录用户的操作的开始时间
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        //线程绑定变量（该数据只有当前请求的线程可见）
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
        logger.info("=====>>>> SystemLogAspect doBefore");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("====>>>> before request startTime:" + new Date());
        logger.info("====>>>> before request URL:" + request.getRequestURL().toString());
        logger.info("====>>>> before request method:" + request.getMethod());
        logger.info("====>>>> before request IP:" + request.getRemoteAddr());
        logger.info("====>>>> before request class methos={}", joinPoint.getSignature().getDeclaringTypeName()+"." + joinPoint.getSignature().getName());
        logger.info("====>>>> before request method paramter={}", joinPoint.getArgs());
    }

    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("====>>>> around startTime:" + new Date().getTime());
        Object object = joinPoint.proceed();
        logger.info("====>>>> around endTime:" + new Date().getTime());
        return object;
    }


    @After("controllerAspect()")
    public void doAfter(JoinPoint joinPoint) {
        String name = joinPoint.getSignature().getName();
        logger.info("====>>>> after name:" + name);
    }


    @AfterReturning(pointcut = "controllerAspect()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
        logger.info("====>>>> doAfterReturning response:" + ret);
        logger.info("====>>>> doAfterReturning response endTime:" + new Date());
        logger.info("====>>>> doAfterReturning spend time:" + (System.currentTimeMillis() - beginTimeThreadLocal.get().getTime()));
    }


    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        String name = joinPoint.getSignature().getName();
        logger.info("====>>>> afterThrowing name:" + name);
    }

}
