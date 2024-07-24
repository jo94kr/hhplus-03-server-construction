package io.hhplus.server_construction.support.aop;

import io.hhplus.server_construction.support.aop.annotation.RedissonLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final AopTransaction aopTransaction;

    private static final String LOCK_PREFIX = ":";

    @Around("@annotation(io.hhplus.server_construction.support.aop.annotation.RedissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);

        // lock 키 : 메서드명 + 지정한 키값
        List<String> dynamicValue = CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.value());
        RLock lock;
        if (dynamicValue.size() > 1) {
            List<String> lockKeys = dynamicValue.stream()
                    .map(value -> method.getName() + LOCK_PREFIX + value)
                    .toList();
            lock = redissonClient.getMultiLock(lockKeys.stream()
                    .map(redissonClient::getLock)
                    .toArray(RLock[]::new));
        } else {
            lock = redissonClient.getLock(dynamicValue.get(0));
        }

        try {
            // 락 획득 여부 확인
            boolean isLocked = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
            if (!isLocked) {
                throw new IllegalStateException("failed to acquire lock");
            }
            return aopTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            // 락 해제
            lock.unlock();
        }
    }
}
