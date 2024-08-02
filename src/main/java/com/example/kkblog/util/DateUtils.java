package com.example.kkblog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hyh
 * @date 2024/5/13
 */
public class DateUtils {
    /**
     * 对日期进行格式化
     * @param date
     *     要转换的日期
     * @param pattern
     *     希望转换成什么样的格式
     * @return str
     *     格式化后的日期字符串
     */
    public static String dateFormat(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 根据初始日期推算期望（向前/向后）日期
     * @param initTime
     *     初始日期（initTime可以是null、Date、String数据类型）
     * @param n
     *     向前/向后推算n天（n可以是正整数、0、负整数）
     * @return
     *     推算后的日期字符串
     */
    public static String getCalculateDay(Object initTime,int n){
        // 返回推算后的日期
        String calculateDay = "";
        try {
            // 实例化日历类Calendar
            Calendar calendar = Calendar.getInstance();
            // 定义日期格式化样式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 初始日期
            Date initDate = null;
            // 判断参数类型
            if (null == initTime) {
                // 取系统当前时间
                initDate = new Date();
            } else if (initTime instanceof Date) {
                initDate = (Date)initTime;
            } else {
                // 日期格式字符串转换成日期类Date
                initDate = sdf.parse((String)initTime);
            }
            // 设置日历时间
            calendar.setTime(initDate);
            // 设置推算后的日历时间
            calendar.add(Calendar.DATE, n);
            // 获取推算后的日期
            Date calculateDate = calendar.getTime();
            // 进行格式化
            calculateDay = sdf.format(calculateDate);
        } catch(ParseException e) {
            return calculateDay;
        }
        return calculateDay;
    }
}