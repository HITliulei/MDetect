package com.ll.kubernetes.bean;

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

    private String status;

    private T data;


    public MResponse<T> data(T t){
        this.data = t;
        return this;
    }

    public MResponse<T> status(String s){
        this.status = s;
        return this;
    }
}
