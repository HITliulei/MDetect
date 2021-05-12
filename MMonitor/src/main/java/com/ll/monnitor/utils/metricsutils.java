package com.ll.monnitor.utils;

import com.ll.monnitor.metrics.MetricsNodeList;
import com.ll.monnitor.metrics.MetricsPodList;
import com.ll.monnitor.metrics.MetricsServerApi;
import com.ll.monnitor.metrics.MetricsUsage;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

@Component
public class metricsutils {

    private ApiClient client;

    private MetricsServerApi metricsServerApi;

    private Logger logger = LogManager.getLogger(metricsutils.class);

    public metricsutils(String k8sClientUrl) {
        this.initConnection(k8sClientUrl);
    }

    public metricsutils() {
        this.initConnection(null);
    }

    private void initConnection(String k8sClientUrl) {
        try {
            if (k8sClientUrl == null) {
                this.client = Config.defaultClient();
            } else {
                this.client = Config.fromUrl(k8sClientUrl);
            }
            this.client.getHttpClient().setReadTimeout(0, TimeUnit.SECONDS);
            Configuration.setDefaultApiClient(client);

            this.metricsServerApi = new MetricsServerApi(this.client);
        } catch (IOException e) {
            logger.debug(e);
        }
    }


    public Map<String, MetricsUsage> getNode(){
        Map<String, MetricsUsage> map = new HashMap<>();
        try {
            MetricsNodeList metricsNodeList = this.metricsServerApi.getNodesAndUsage();
            logger.info("返回的信息为 ：" + metricsNodeList);
            for(MetricsNodeList.MetricsNode mn:metricsNodeList.getItems()){
                V1ObjectMeta v1ObjectMeta = mn.getMetadata();
                String string = v1ObjectMeta.getSelfLink();
                String[] strings = string.split("/");
                MetricsUsage mu = mn.getUsage();
                map.put(strings[strings.length-1], mu);
//                System.out.println(mu.getCpuInM());          //cpu单位转为m
//                System.out.println(mu.getMemoryInMi());      //内存单位转为Mi
            }
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
        return map;
    }


    public Map<String, MetricsUsage> getPod(String podname, String namespace){
        Map<String, MetricsUsage> map = new HashMap<>();
        try {
            MetricsPodList.MetricsPod metricsPod = this.metricsServerApi.getPodAndUsage(podname, namespace);
            MetricsPodList.MetricsPodContainer metricsPodContainer = metricsPod.getContainers().get(0);
            map.put(podname, metricsPodContainer.getUsage());
            logger.info("接受到的信息为： " + metricsPod);
        }catch (ApiException apiException){
            apiException.printStackTrace();
            return null;
        }
        return map;
    }


    public Map<String, MetricsUsage> getPods(String namespace){
        Map<String, MetricsUsage> map = new HashMap<>();
        try{
            MetricsPodList metricsPodList = this.metricsServerApi.getPodsAndUsage(namespace);
            for (MetricsPodList.MetricsPod  metricsPod: metricsPodList.getItems()){
                V1ObjectMeta v1ObjectMeta = metricsPod.getMetadata();
                String string = v1ObjectMeta.getSelfLink();
                String[] strings = string.split("/");
                String podsName = strings[strings.length - 1];
                map.put(podsName, metricsPod.getContainers().get(0).getUsage());
            }
//            logger.info("获取的信息为" + metricsPodList);
        }catch (ApiException apiException){
            apiException.printStackTrace();
            return null;
        }
        return map;
    }

}
