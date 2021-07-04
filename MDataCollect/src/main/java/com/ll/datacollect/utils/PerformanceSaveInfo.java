package com.ll.datacollect.utils;

import com.ll.common.config.IpConfig;
import com.ll.common.config.PathConfig;
import com.ll.common.utils.MFileUtils;
import com.ll.datacollect.bean.TimeMemAndCpu;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */

public class PerformanceSaveInfo {


    /**
     * 收集node 的节点信息， 每隔5秒发送一次
     * @param name
     * @param times
     */
    public static void saveNodePerformanceData(String name, String times){
        String filename = PathConfig.PERFORMANCE_DATA_NODE_PATH + "/" + name + "_" + times;
        File file = new File(filename);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            while (true){
                ResponseEntity<TimeMemAndCpu> response = new RestTemplate().exchange("http://"+ IpConfig.MMONITOR +":"+IpConfig.MMONITOR_PORT+"/api/v1/metrics/topNode", HttpMethod.GET, new HttpEntity<>(null ,new HttpHeaders()), TimeMemAndCpu.class);
                TimeMemAndCpu timeMemAndCpu = response.getBody();
                MFileUtils.writeFile(file, timeMemAndCpu.toString());
                Thread.sleep(5000);
            }
        }catch (IOException | InterruptedException ioException){
            ioException.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String [] s= new String[]{
                "dog", "lazy", "a", "over", "jumps", "fox", "brown", "quick", "A"
        };
        List<String> list = Arrays.asList(s);
        Collections.reverse(list);
        s=list.toArray(new String[1]);//没有指定类型的话会报错
        System.out.println(s[0]);
    }





}
