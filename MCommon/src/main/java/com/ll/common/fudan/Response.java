package com.ll.common.fudan;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    /**
     * 1 true, 0 false
     */
    Integer status;

    String msg;
    T data;


    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}

