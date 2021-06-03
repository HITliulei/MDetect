package com.ll.framework.ano;

import com.ll.framework.config.MClientAutoComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/27
 */
@Target(ElementType.TYPE) // 注解在spring的启动类， 对controller对外开放的接口进行 检索
@Retention(RetentionPolicy.RUNTIME) // 运行时启动
@Import(MClientAutoComponentScan.class)
@Documented
public @interface MClient {
}
