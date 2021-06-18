package com.ll.monnitor.utils;

import com.alibaba.fastjson.JSONObject;
import com.ll.common.utils.MRequestUtils;
import com.ll.common.utils.MResponse;
import com.ll.common.utils.MURIUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

/**
 * @author Lei
 * @version 1.0
 * @date 2020/8/30
 */
public class CAdvisorUtils {

    private static String API_PREFIX = "/api";
    private static String _VERSION = "/v1.3";
    private static String _MACHINE_STATE_URL = "/machine";

    private static String MACHINE_STATE_URL = API_PREFIX + _VERSION + _MACHINE_STATE_URL;

    public static MResponse<String> getMachineState(String ipAddr, Integer port) {
        URI uri = MURIUtils.getRemoteUri(ipAddr, port, MACHINE_STATE_URL);
        String result = MRequestUtils.sendRequest(uri, null, String.class, RequestMethod.GET);
        return new MResponse<String>().data(result);
    }
}
