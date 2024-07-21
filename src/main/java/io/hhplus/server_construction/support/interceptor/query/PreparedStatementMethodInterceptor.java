package io.hhplus.server_construction.support.interceptor.query;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.Objects;

@RequiredArgsConstructor
public class PreparedStatementMethodInterceptor implements MethodInterceptor {

    private final QueryCounter queryCounter;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (isExecuteQuery(invocation.getMethod()) && isInRequestScope()) {
            queryCounter.increase();
        }
        return invocation.proceed();
    }

    private boolean isExecuteQuery(Method method) {
        String methodName = method.getName();
        return methodName.equals("executeQuery") || methodName.equals("execute") || methodName.equals("executeUpdate");
    }

    private boolean isInRequestScope() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }
}
