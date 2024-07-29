package io.hhplus.server_construction.support.aop;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomSpringELParser {

    public static List<String> getDynamicValue(String[] parameterNames, Object[] args, String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Object value = parser.parseExpression(key).getValue(context, Object.class);
        if (value == null) {
            return Collections.emptyList();
        } else if (value instanceof List<?> list) {
            List<String> stringList = new ArrayList<>(list.size());
            for (Object obj : list) {
                stringList.add(obj.toString());
            }
            return stringList;
        } else {
            return Collections.singletonList(value.toString());
        }
    }
}
