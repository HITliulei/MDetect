package com.ll.common.service;

import com.ll.common.base.MUniqueObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

/**
 * @author ll
 * @version 0.1
 * @date 2019/12/13
 */
@Getter
@Setter
@ToString
public class MSvcInterface extends MUniqueObject {
    private String patternUrl;
    private String functionName;
    private String requestMethod;
    private List<MParamer> params;
    private String returnType;
    private String serviceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSvcInterface that = (MSvcInterface) o;

        if (this.params.size() != that.params.size()) return false;
        for (int i = 0; i < this.params.size(); ++i) {
            if (!this.params.get(i).equals(that.params.get(i))) return false;
        }

        return Objects.equals(patternUrl, that.patternUrl) &&
                Objects.equals(functionName, that.functionName) &&
                Objects.equals(requestMethod, that.requestMethod) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(serviceId, that.serviceId);
    }

    public MSvcInterface() {}

    public MSvcInterface(MSvcInterface other) {
        this.patternUrl = other.patternUrl;
        this.functionName = other.functionName;
        this.requestMethod = other.requestMethod;
        this.params = new ArrayList<>(other.params);
        this.returnType = other.returnType;
        this.serviceId = other.serviceId;
    }
}