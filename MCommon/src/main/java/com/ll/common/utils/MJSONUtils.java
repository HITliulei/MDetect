package com.ll.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */
public class MJSONUtils {


    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        //过滤类的属性id
        JSON_MAPPER.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
    }
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
     * 对象转换成格式化的json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objectToJsonPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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


    /**
     * 将字符穿转化为JSONObject
     * @param string
     * @return
     */
    public static JSONObject getJsonObject(String string){
        return JSONObject.parseObject(string);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class test{
        private String a;
        private String b;

        @Override
        public String toString() {
            return toJSONString(this);
        }
    }


    public static void main(String[] args) {

        String a  = new test("aqwe"," bxcv").toString();

        JSONObject J = JSONObject.parseObject(a);
        System.out.println(J);
    }
}
