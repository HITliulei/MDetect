package com.ll.monnitor.controller;

import com.ll.monnitor.bean.TimeMemAndCpu;
import io.kubernetes.client.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ll.monnitor.utils.*;

import java.io.IOException;
import java.util.Date;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

@RequestMapping("/api/v1/metrics")
@RestController
public class metrics {

    @Autowired
    private metricsutils metricsutils;


    @GetMapping("/topNode")
    public TimeMemAndCpu getNode() throws IOException, ApiException {
        TimeMemAndCpu timeMemAndCpu = new TimeMemAndCpu();
        timeMemAndCpu.setDate(new Date());
        timeMemAndCpu.setMap(this.metricsutils.getNode());
        return timeMemAndCpu;
    }


    @GetMapping("topPod/{podname}/{namespace}")
    public TimeMemAndCpu getPodBypodName(@PathVariable("podname") String podename, @PathVariable("namespace") String namespace){
        TimeMemAndCpu timeMemAndCpu = new TimeMemAndCpu();
        timeMemAndCpu.setDate(new Date());
        timeMemAndCpu.setMap(this.metricsutils.getPod(podename, namespace));
        return timeMemAndCpu;
    }

    @GetMapping("topPod/{podname}")
    public TimeMemAndCpu getPodBypodNameDefault(@PathVariable("podname") String podname){
        TimeMemAndCpu timeMemAndCpu = new TimeMemAndCpu();
        timeMemAndCpu.setDate(new Date());
        timeMemAndCpu.setMap(this.metricsutils.getPod(podname, "default"));
        return timeMemAndCpu;
    }


    @GetMapping("topPods/{namespace}")
    public TimeMemAndCpu getPodsByNameSpace(@PathVariable("namespace") String namespace){
        TimeMemAndCpu timeMemAndCpu = new TimeMemAndCpu();
        timeMemAndCpu.setDate(new Date());
        timeMemAndCpu.setMap(this.metricsutils.getPods(namespace));
        return timeMemAndCpu;
    }

    @GetMapping("topPods")
    public TimeMemAndCpu getPodsByNameSpacedefault(){
        TimeMemAndCpu timeMemAndCpu = new TimeMemAndCpu();
        timeMemAndCpu.setDate(new Date());
        timeMemAndCpu.setMap(this.metricsutils.getPods("default"));
        return timeMemAndCpu;
    }
}
