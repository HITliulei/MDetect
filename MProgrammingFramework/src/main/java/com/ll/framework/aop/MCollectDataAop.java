package com.ll.framework.aop;

import com.ll.common.config.ServiceConfig;
import com.ll.common.trace.Result;
import com.ll.common.trace.Trace;
import com.ll.common.utils.MFileUtils;
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
import java.io.File;
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
//                httpHeaders.set("startTime",startTime.toString());
            }else{
                httpHeaders = new HttpHeaders();
//                httpHeaders.set("startTime",startTime.toString());
            }
        }else{
            httpHeaders = new HttpHeaders();
//            httpHeaders.set("startTime",startTime.toString());
        }
        if(httpHeaders.get("traceId") == null){
            String traceId = UUID.randomUUID().toString();
            httpHeaders.set("traceId", traceId);
        }
    }
    @AfterReturning(value = "mCollectDataAop()", returning = "object")
    public void mCollectDataAfterReturning(JoinPoint joinPoint, Object object){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DateTime endTime = DateTime.now();
        Object[] args = joinPoint.getArgs();

        String class_name = joinPoint.getTarget().getClass().getName();  // ????????????
        String method_name = joinPoint.getSignature().getName(); // ???????????????


        // ?????????????????????
        Result result = Result.SUCCESS;
        if (object instanceof HttpEntity){
            HttpEntity httpEntity = (HttpEntity)object;
            System.out.println(" ?????????????????????????????? ??? " + httpEntity.getBody().toString().contains("status=1"));
            result = httpEntity.getBody().toString().contains("status=1")?Result.SUCCESS:Result.FAILED;
        }

        HttpHeaders httpHeaders = null;
        if (args.length !=0){
            if (args[args.length-1] instanceof HttpHeaders){
                httpHeaders = (HttpHeaders)args[args.length-1];
            }else{
                httpHeaders = new HttpHeaders();
            }
        }else {
            System.out.println("?????????http");
            httpHeaders = new HttpHeaders();
        }
        String ex = "wrong_name_time";
        if (httpHeaders.get("ex_name") != null && httpHeaders.get("ex_times") != null){
            ex = httpHeaders.get("ex_name").toString() +"_" + httpHeaders.get("ex_times").toString();
        }
        if (ex.equals("wrong_name_time")){
            // ??????????????????????????? ?????????ex???????????? ??????????????????
            return;
        }

        // ???????????????????????????
        Trace trace = new Trace(ex,
                httpHeaders.get("traceId").toString(),
                request.getLocalAddr(),
                serviceId,
                request.getRequestURL().toString(),
                String.format("%s_%s", serviceId, method_name),
                request.getRemoteAddr(),
                startTimes.get(),
                endTime.toString(),
                request.getMethod(),
                result.toString());
        System.out.println("??????????????????trace?????????: \n" + trace.toString());
        // ????????????????????????, ?????????????????????????????????logstash?????????????????? ???????????????
        MFileUtils.writeFile(new File(ServiceConfig.TRACEINFO_PATH +File.separator+ ex+ File.separator + serviceId), trace.toString());


    }


    public static void main(String[] args) {
        MFileUtils.writeFile(new File(ServiceConfig.TRACEINFO_PATH +File.separator+ "wrong_name_time" + File.separator + "microservice"), "qerqwerqwer");
    }

}
