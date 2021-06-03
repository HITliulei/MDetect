package com.ll.common.fudan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response<T> {

    /**
     * 1 true, 0 false
     */
    Integer status;

    String msg;
    T data;
}

