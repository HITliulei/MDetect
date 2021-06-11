package com.ll.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ll.common.bean.system.MSystemAllInfo;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */
public class ServiceConfig {

    // 源代码下载路径
    public final static String CODE_DIWNLOAD_PATH  = "/tmp/MServiceAnalyzer";


    public static final MSystemAllInfo M_SYSTEM_ALL_INFO = new MSystemAllInfo();


    public static final String[] SERVICE_LIST = new String[]{
            "ts-voucher-service", "ts-verification-code-service", "ts-travel-service", "ts-travel-plan-service", "ts-travel2-service", "ts-train-service",
            "ts-ticketinfo-service", "ts-ticket-office-service", "ts-station-service", "ts-auth-service", "ts-security-service", "ts-seat-service", "ts-route-service",
            "ts-route-plan-service", "ts-rebook-service", "ts-price-service", "ts-preserve-service", "ts-preserve-other-service",
            "ts-payment-service", "ts-order-service", "ts-order-other-service",
            "ts-news-service", "ts-notification-service", "ts-user-service", "ts-inside-payment-service", "ts-food-service",
            "ts-food-map-service", "ts-execute-service", "ts-contacts-service", "ts-consign-service", "ts-consign-price-service",
            "ts-config-service", "ts-cancel-service", "ts-basic-service", "ts-assurance-service", "ts-admin-user-service", "ts-admin-travel-service",
            "ts-admin-route-service", "ts-admin-order-service", "ts-admin-basic-info-service"};


    public static final String SYSTEM_SERVICE_INFO = "{ \"ts-voucher-service\" : 16101,\"ts-verification-code-service\" : 15678,\"ts-travel-service\" : 12346,\"ts-travel-plan-service\" : 14322,\"ts-travel2-service\" : 16346,\"ts-train-service\" : 14567,\"ts-ticketinfo-service\" : 15681,\"ts-ticket-office-service\" : 16108,\"ts-station-service\" : 12345,\"ts-auth-service\" : 12340,\"ts-security-service\" : 11188,\"ts-seat-service\" : 18898,\"ts-route-service\" : 11178,\"ts-route-plan-service\" : 14578,\"ts-rebook-service\" : 18886,\"ts-price-service\" : 16579,\"ts-preserve-service\" : 14568,\"ts-preserve-other-service\" : 14569,\"ts-payment-service\" : 19001,\"ts-order-service\" : 12031,\"ts-order-other-service\" : 12032,\"ts-news-service\" : 12862,\"ts-notification-service\" : 17853,\"ts-user-service\" : 12342,\"ts-inside-payment-service\" : 18673,\"ts-food-service\" : 18856,\"ts-food-map-service\" : 18855,\"ts-execute-service\" : 12386,\"ts-contacts-service\" : 12347,\"ts-consign-service\" : 16111,\"ts-consign-price-service\" : 16110,\"ts-config-service\" : 15679,\"ts-cancel-service\" : 18885,\"ts-basic-service\" : 15680,\"ts-assurance-service\" : 18888,\"ts-admin-user-service\" : 16115,\"ts-admin-travel-service\" : 16114,\"ts-admin-route-service\" : 16113,\"ts-admin-order-service\" : 16112,\"ts-admin-basic-info-service\" : 18767}";


    public static void main(String[] args) {
        JSONObject jsonObject = JSON.parseObject(SYSTEM_SERVICE_INFO);
        System.out.println(jsonObject);
//        System.out.println(SYSTEM_SERVICE_INFO);
    }


    //保存路径信息
    public final static String TRACEINFO_PATH = "/tmp/data/traceData";


    //本地路径
    public final static String TACELOCAL_PATH = "/data/liulei/traceInfo";




}
