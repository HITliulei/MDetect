package com.ll.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */
public class MJSONUtils {

    private static final JSONObject JSON_OBJECT = new JSONObject();


    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    /**
     * 将JSON转化为toString
     * @param data
     * @return
     */
    public static String toJSONString(Object data) {
        try {
            return JSON_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回值
     * @param data
     * @return
     */
    public static <T> T getJSONObject(String data, Class<T> objectClass) {
        try {
            return JSON_MAPPER.readValue(data, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成list
     * @param jsonData json 数据
     * @param beanType List下的类型
     * @return
     */
    public static <T> List<T> parseArray(String jsonData, Class<T> beanType) {
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return JSON_MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
