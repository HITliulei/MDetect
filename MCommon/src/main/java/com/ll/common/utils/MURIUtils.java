package com.ll.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */
public class MURIUtils {

    private final static Logger logger = LogManager.getLogger(MURIUtils.class);

    public static URI getRemoteUri(String ipAddr, int port, String path) {
        URI uri = null;
        try {
            uri = new URI(
                    "http",
                    null,
                    ipAddr,
                    port,
                    path, null, null
            );
        } catch (URISyntaxException e) {
            logger.info(e);
        }
        logger.info(uri);
        return uri;
    }

    public static URI getRemoteUriWithQueries(URI oldUri, Map<String, String> paramMap) {
        URI uri = null;
        LinkedMultiValueMap<String, String> pMap = new LinkedMultiValueMap<>();
        for (String key : paramMap.keySet()) {
            List<String> vList = new ArrayList<>(1);
            vList.add(paramMap.get(key));
            pMap.put(key, vList);
        }
        try {
            uri = UriComponentsBuilder.fromUri(oldUri).queryParams(pMap).build().toUri();
        } catch (Exception e) {
            logger.info(e);
        }
        return uri;
    }

}
