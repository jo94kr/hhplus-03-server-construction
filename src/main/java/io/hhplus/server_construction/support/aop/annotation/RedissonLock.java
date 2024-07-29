package io.hhplus.server_construction.support.aop.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    // Lock 이름
    String value();

    // 락 획득 시간
    long waitTime() default 5000L;

    // 락 점유 시간
    long leaseTime() default 5000L;
}
