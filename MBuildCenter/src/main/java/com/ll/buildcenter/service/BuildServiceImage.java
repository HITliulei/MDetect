package com.ll.buildcenter.service;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/7
 */


public interface BuildServiceImage {

    public String buildServiceImage(String branch, String serviceName, String serviceVersion);


    public boolean compile(String branch);

    public boolean pushImage(String imagesName);

}
