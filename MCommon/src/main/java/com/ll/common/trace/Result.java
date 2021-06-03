package com.ll.common.trace;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/3
 */
public enum Result {
    /**
     * 返回结果成功
     */
    SUCCESS("success"),

    /**
     * 返回结果失败
     */
    FAILED("failed");

    private String describe;


    Result(String describe) {
        this.describe = describe;
    }

    public String getDescribe(){
        return describe;
    }


    public static Result getInstance(String describe){
        Result[] results = Result.values();
        for (Result result: results){
            if (result.getDescribe().equals(describe)){
                return result;
            }
        }
        return results[0];
    }

}
