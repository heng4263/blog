package com.example.kkblog.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.kkblog.domain.dto.DynamicCommentDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Hyh
 * @Date 2024 04 12 23 29
 **/

public class ThJson {
    public Integer getLastCommentId(DynamicCommentDto dto) {
        if (dto.getChildren() == null || dto.getChildren().isEmpty()) {
            return -1;
        }
        return dto.getChildren().get(dto.getChildren().size() - 1).getId();
    }


    /**
     * 从jsonStr中获取key的值
     *
     * @param jsonStr json str
     * @param key     关键
     * @return {@link Object}
     */
    public Object getStrJson(String jsonStr, String key) {
        Object obj = null;

        if (!StringUtils.isEmpty(jsonStr) && !StringUtils.isEmpty(key)) {
            JSONObject json = JSONObject.parseObject(jsonStr);
            obj = json.get(key);
        }

        return obj;
    }

    /**
     *
     * @param arrayStr 数组str
     * @return {@link Object}
     */
    /**
     * json数组转成数组
     *
     * @param arrayStr 数组str
     * @return {@link List}<{@link Object}>
     */
    public List<String> parseStringArray(String arrayStr) {
        if (!StringUtils.isEmpty(arrayStr)) {
            try {
                return JSON.parseArray(arrayStr, String.class);
            } catch (Exception e) {
                return Arrays.stream(arrayStr.split(",")).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    /**
     * json字符串转成 JSONObject
     *
     * @param jsonStr json str
     * @return {@link JSONObject}
     */
    public JSONObject parseJson(String jsonStr) {
        JSONObject json = null;

        if (!StringUtils.isEmpty(jsonStr)) {
            json = JSONObject.parseObject(jsonStr);
        }

        return json;
    }
}