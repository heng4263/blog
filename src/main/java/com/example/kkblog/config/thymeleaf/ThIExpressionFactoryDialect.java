package com.example.kkblog.config.thymeleaf;

import com.example.kkblog.util.ThJson;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author Hyh
 * @Date 2024 04 12 23 34
 **/
@Component
public class ThIExpressionFactoryDialect extends AbstractDialect implements IExpressionObjectDialect {

    private final IExpressionObjectFactory iExpressionObjectFactory;

    protected ThIExpressionFactoryDialect() {
        super("DarrenExpression");
        //map中可以多个
        Map<String, Object> map = new HashMap<>();

        //配置我们刚才写的工具类，key为页面上引用的名字
        map.put("thJson", new ThJson());

        iExpressionObjectFactory = new ThIExpressionObjectFactory(map);
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return iExpressionObjectFactory;
    }
}