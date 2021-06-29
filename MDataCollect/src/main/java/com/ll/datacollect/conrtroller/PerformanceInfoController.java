package com.ll.datacollect.conrtroller;

import com.ll.common.utils.MResponse;
import com.ll.datacollect.utils.PerformanceSaveInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 *
 * 收集性能的信息
 */

@RequestMapping("/api/v1/getPerformance")
@RestController
public class PerformanceInfoController {
    private Logger logger = LogManager.getLogger(PerformanceInfoController.class);


    private static ExecutorService executorService_performance = Executors.newFixedThreadPool(10);

    @GetMapping("getNodeInfo")
    public MResponse getNodeInfo(){
        return MResponse.successResponse();
    }

    @GetMapping("getNodeInfo/{nodeName}/{name}/{times}")
    public MResponse getNodeInfo(@PathVariable("nodeName")String nodeName,
                                 @PathVariable("name")String name,
                                 @PathVariable("times")String times,
                                 @RequestHeader HttpHeaders httpHeaders){
        logger.info("每5秒获取node节点信息");
        executorService_performance.submit(() -> {
            PerformanceSaveInfo.saveNodePerformanceData(name, times);
        });
        return MResponse.successResponse();
    }

    @GetMapping("getAllPodInfo")
    public MResponse getAllPodsNodeInfo(){
        return MResponse.successResponse();
    }

    @GetMapping("getAllPodInfo/{nodeName}")
    public MResponse getNodeInfo(@PathVariable("nodeName")String nodeName){
        return MResponse.successResponse();
    }
}
