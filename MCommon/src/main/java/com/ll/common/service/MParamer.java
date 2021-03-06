package com.ll.common.service;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Created by Lei on 2020/12/16 15:32
 */


@Getter
@Setter
@ToString
public class MParamer {
    // 参数名称
    private String name;
    // 请求的名称  若为requestBody 则为类名
    private String requestname;
    // 默认值
    private String defaultObject;
    // 参数类型
    private String type;
    // 参数的请求方式   path / Paramer / requestBody
    private String method;

    public MParamer() {

    }

    public MParamer(MParamer other) {
        this.name = other.name;
        this.requestname = other.requestname;
        this.defaultObject = other.defaultObject;
        this.type = other.type;
        this.method = other.method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MParamer paramer = (MParamer) o;
        return Objects.equals(name, paramer.name) &&
                Objects.equals(requestname, paramer.requestname) &&
                Objects.equals(defaultObject, paramer.defaultObject) &&
                Objects.equals(type, paramer.type) &&
                Objects.equals(method, paramer.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, requestname, defaultObject, type, method);
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
