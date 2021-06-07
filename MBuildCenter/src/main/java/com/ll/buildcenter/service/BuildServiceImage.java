package com.ll.buildcenter.service;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/7
 */


public interface BuildServiceImage {

    public String buildServiceImage(String serviceName, String serviceVersion);


    public boolean compile();

    public boolean pushImage(String imagesName);

}
