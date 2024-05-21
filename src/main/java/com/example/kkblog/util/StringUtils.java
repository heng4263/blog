package com.example.kkblog.util;

import com.example.kkblog.config.exception.KKBlogException;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xiaoke
 * @Date 2024 04 07 23 50
 **/
public class StringUtils {
    public static boolean isEmpty(String s) {
        return isNull(s) || s.isEmpty();
    }

    public static boolean isNull(Object s) {
        return s == null;
    }

    // trim对象的字符串属性
    public static void trimObject(Object object) throws KKBlogException {
        try {
            Class<?> clazz = object.getClass();
            // 通过属性名获取Field对象
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.getType() == String.class) {
                    String value = (String) declaredField.get(object);
                    if (!isEmpty(value)) {
                        declaredField.set(object, value.trim());
                    }
                }
            }
        } catch (Exception e) {
            throw new KKBlogException("trim对象异常");
        }
    }

    public static boolean checkEmail(String email){
        String regex = "^[A-Za-z0-9\u4e00-\u9fa5]+(\\.[A-Za-z0-9\u4e00-\u9fa5]+)*@[A-Za-z0-9\u4e00-\u9fa5]+(\\.[A-Za-z0-9\u4e00-\u9fa5]+)*(\\.[A-Za-z\u4e00-\u9fa5]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
