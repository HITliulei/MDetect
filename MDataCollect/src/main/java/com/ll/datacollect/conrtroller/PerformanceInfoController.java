package com.ll.datacollect.conrtroller;

import com.ll.common.utils.MResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("getNodeInfo")
    public MResponse getNodeInfo(){
        return MResponse.successResponse();
    }

    @GetMapping("getNodeInfo/{nodeName}")
    public MResponse getNodeInfo(@PathVariable("nodeName")String nodeName){
        return MResponse.successResponse();
    }
}
