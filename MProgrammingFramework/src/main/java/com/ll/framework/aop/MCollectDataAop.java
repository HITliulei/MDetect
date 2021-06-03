package com.ll.framework.aop;

import com.ll.common.fudan.Response;
import com.ll.common.trace.Result;
import com.ll.common.trace.Trace;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/2
 */


@Aspect
@Component
public class MCollectDataAop {


    @Autowired
    @Qualifier("serviceId")
    private String serviceId;


    private static long startTime;

    private static ThreadLocal<String> startTimes = new ThreadLocal<String>();

    @Pointcut("execution(* *..controller.*.*(..))")
    public void mCollectDataAop() { }
    @Before("mCollectDataAop()")
    public void mCollectDataBefore(JoinPoint joinPoint){
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DateTime startTime = DateTime.now();
        Object[] args = joinPoint.getArgs();
        HttpHeaders httpHeaders = null;

        startTimes.set(startTime.toString());


        if (args.length !=0){
            if (args[args.length - 1] instanceof HttpHeaders){
                httpHeaders = (HttpHeaders)args[args.length-1];
                httpHeaders.set("startTime",startTime.toString());
            }else{
                System.out.println("不存在http");
                httpHeaders = new HttpHeaders();
                httpHeaders.set("startTime",startTime.toString());
            }
        }else{
            System.out.println("不存在http");
            httpHeaders = new HttpHeaders();
            httpHeaders.set("startTime",startTime.toString());
        }
        if(httpHeaders.get("traceId") == null){
            System.out.println("不存在traceId");
            String traceId = UUID.randomUUID().toString();
            httpHeaders.set("traceId", traceId);
        }
    }
    @AfterReturning(value = "mCollectDataAop()", returning = "object")
    public void mCollectDataAfterReturning(JoinPoint joinPoint, Object object){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DateTime endTime = DateTime.now();
        Object[] args = joinPoint.getArgs();

        // 获取返回的结果
        Result result = Result.SUCCESS;
        if (object.getClass().equals(HttpEntity.class)){
            HttpEntity httpEntity = (HttpEntity)object;
            Response response = (Response) httpEntity.getBody();
            result = response.getStatus()==1?Result.SUCCESS:Result.FAILED;
        }

        HttpHeaders httpHeaders = null;
        if (args.length !=0){
            if (args[args.length-1] instanceof HttpHeaders){
                httpHeaders = (HttpHeaders)args[args.length-1];
            }else{
                System.out.println("不存在http");
                httpHeaders = new HttpHeaders();
            }
        }else {
            System.out.println("不存在http");
            httpHeaders = new HttpHeaders();
        }
        String ex = "wrong_name_time";
        if (httpHeaders.get("ex_name") != null && httpHeaders.get("ex_times") != null){
            ex = httpHeaders.get("ex_name").toString() +"_" + httpHeaders.get("ex_times").toString();
        }


        // 保存到本地日志信息
        Trace trace = new Trace(ex,
                httpHeaders.get("traceId").toString(),
                request.getLocalAddr(),
                request.getRequestURL().toString(),
                request.getRemoteAddr(),
                httpHeaders.get("startTime").toString(),
                endTime.toString(),
                request.getMethod(),
                result.toString());
    }

}
