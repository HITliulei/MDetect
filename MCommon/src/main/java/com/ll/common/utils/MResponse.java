package com.ll.common.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/10
 */
@Getter
@Setter
@ToString
public class MResponse<T> {

    private int code;

    private String status;

    private T data;

    public MResponse<T> code(int code){
        this.code = code;
        return this;
    }


    public MResponse<T> data(T t){
        this.data = t;
        return this;
    }

    public MResponse<T> status(String s){
        this.status = s;
        return this;
    }


    public static MResponse successResponse(){
        return new MResponse().status("success");
    }


    public static MResponse failedResponse(){
        return new MResponse().status("failed");
    }
}
