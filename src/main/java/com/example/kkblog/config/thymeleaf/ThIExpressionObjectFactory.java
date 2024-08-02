package com.example.kkblog.config.thymeleaf;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @Author Hyh
 * @Date 2024 04 12 23 33
 **/
public class ThIExpressionObjectFactory implements IExpressionObjectFactory {

    private Map<String, Object> map;

    public ThIExpressionObjectFactory(Map<String, Object> map) {
        this.map = new HashMap<>(map);
    }

    /**
     * 获取所有表达式对象名称
     *
     * @return {@link Set}<{@link String}>
     */
    @Override
    public Set<String> getAllExpressionObjectNames() {
        return map.keySet();
    }

    /**
     * 根据表达式名字获取表达式对象
     *
     * @param iExpressionContext 表达语境
     * @param s                  名称
     * @return {@link Object}
     */
    @Override
    public Object buildObject(IExpressionContext iExpressionContext, String s) {
        return map.get(s);
    }

    /**
     * 是否允许缓存
     *
     * @param s 名称
     * @return boolean
     */
    @Override
    public boolean isCacheable(String s) {
        return true;
    }
}