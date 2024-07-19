package io.hhplus.server_construction.common.interceptor.query;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import java.sql.PreparedStatement;

@RequiredArgsConstructor
public class ConnectionMethodInterceptor implements MethodInterceptor {

    private final QueryCounter queryCounter;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        if (proceed instanceof PreparedStatement ps) {
            ProxyFactory proxyFactory = new ProxyFactory(ps);
            proxyFactory.addAdvice(new PreparedStatementMethodInterceptor(queryCounter));
            return proxyFactory.getProxy();
        }
        return proceed;
    }
}
