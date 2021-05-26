package com.ll.kubernetes.controller;

import com.ll.common.utils.MResponse;
import com.ll.kubernetes.bean.Deployinfo;
import com.ll.kubernetes.bean.MDockerInfoBean;
import com.ll.kubernetes.bean.MPodDockerInfo;
import com.ll.kubernetes.bean.Node.NodeList;
import com.ll.kubernetes.bean.datacollect.ResultDeploy;
import com.ll.kubernetes.utils.K8sutils;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/3
 */

@RestController
@RequestMapping("/api/v1/controlk8s")
public class k8scontrol {

    private static Logger log = LogManager.getLogger(k8scontrol.class);

    @Autowired
    private K8sutils k8sutils;
    /**
     * 根据ip的得到docker信息
     * @param ip pods' ip
     * @return MDockerInfoBean
     */
    @GetMapping("/getDockerInfoByip/{ip}")
    public MResponse<MDockerInfoBean> getDockerinfoByip(@PathVariable("ip") String ip){
        log.info("ip address : " + ip);

        return new MResponse<MDockerInfoBean>().data(this.k8sutils.getDockerInfoByIpAddr(ip));
    }

    @GetMapping("/getDockerInfoByPodName/{name}")
    public MResponse<MDockerInfoBean> getDockerInfoByPodName(@PathVariable("name") String name){
        log.info("pod name : " + name);
        return new MResponse<MDockerInfoBean>().data(this.k8sutils.getDockerInfoByPodName(name));
    }

    @GetMapping("/getAllPodinfoWithoutDocker")
    public MResponse<List<MDockerInfoBean>> getAllPodinfoWithoutDocker(){
        MResponse<List<MDockerInfoBean>> MResponse = new MResponse<>();
        MResponse.setStatus("success");
        MResponse.setData(this.k8sutils.getAllPodinfoWItoutDocker());
        return MResponse;
    }

    @GetMapping("/getPodDockerInfoByPodName/{podname}")
    public MResponse<MPodDockerInfo> getPodDockerInfoByPodName(@PathVariable("podname") String podname){
        log.info("pod name: " + podname);
        return new MResponse<MPodDockerInfo>().data(this.k8sutils.getPodDockerInfoByName(podname));
    }

    @GetMapping("/getPodDockerInfoByPodIp/{podIp}")
    public MResponse<MPodDockerInfo> getPodDockerInfoByPodIp(@PathVariable("podIp") String podIp){
        log.info("pod Ip: " + podIp);
        return new MResponse<MPodDockerInfo>().data(this.k8sutils.getPodDockerInfoByIp(podIp));
    }

    @GetMapping("/getAllPodDockerInfo")
    public MResponse<List<MPodDockerInfo>> getAllPodDockerInfo(){
        log.info("get all pod info");
        return this.k8sutils.getAllPodDockerInfo();
    }

    @GetMapping("/getAllpodsName")
    public MResponse<List<String>> getAllpodsName(){
        log.info("得到所有的podName");
        return this.k8sutils.getAllpodsName();
    }
    /**
     * 得到所有的mongo pod信息
     */
    @GetMapping("/getAllMongoInfo")
    public MResponse<List<V1Pod>> getALlPodOfMongo(){
        log.info("get mongo pods");
        List<V1Pod> list = this.k8sutils.getALlPod();
        List<V1Pod> result = new ArrayList<>();
        for (V1Pod v1Pod: list){
            if (v1Pod.getMetadata().getName().contains("mongo")){
                result.add(v1Pod);
            }
        }
        return new MResponse<List<V1Pod>>().status("success").data(result);
    }

    /**
     * 得到所有的节点node 及其label
     * @return
     */
    @GetMapping("/getAllNodeLable")
    public MResponse<Map<String, String>> getallNodeLabel(){
        return new MResponse<Map<String, String>>().data(this.k8sutils.getAllnode());
    }

    @GetMapping("/getNodeLabelByNode/{node}")
    public MResponse<String> getNOdeLabelByNode(@PathVariable("node") String node){
        return new MResponse<String>().data(this.k8sutils.getLabelByNode(node));
    }
    @GetMapping("/getNodeByNodeLabel/{nodeLabel}")
    public MResponse<String> getNodeByNodeLabel(@PathVariable("nodeLabel") String nodeLabel){
        return new MResponse<String>().data(this.k8sutils.getNodeBylabel(nodeLabel));
    }

    /**
     * get ALL node
     * @return
     */
    @GetMapping("/getAllNode")
    public MResponse<List<String>> getAllNode(){
        return new MResponse<List<String>>().data(new ArrayList<>(this.k8sutils.getAllnode().keySet()));
    }

    /**
     * get ALL node
     * @return
     */
    @GetMapping("/getNodeInfo")
    public MResponse<NodeList> getNodeInfo(){
        return new MResponse<NodeList>().data(this.k8sutils.getNodeInfo());
    }

    /**
     * 部署deployment 并且部署service服务
     * @param deployinfo
     * @return
     */
    @GetMapping("/onlydeploy")
    public void onlydeploy(@RequestBody Deployinfo deployinfo){
        log.info("only deploy, not check other thig and not check");
        this.k8sutils.onlydeployDeployment(deployinfo);
    }

    /**
     * deploy service with pod
     * @param deployinfo
     * @return
     */
    @GetMapping("/onlydeployServiceWithPod")
    public MResponse<ResultDeploy> onlydeployservicePod(@RequestBody Deployinfo deployinfo){
        log.info("the deploy info is: " + deployinfo);
        ResultDeploy a  = this.k8sutils.onlyDeployPod(deployinfo);
        return new MResponse<ResultDeploy>().data(a==null?new ResultDeploy():a);

    }

    @GetMapping("/onlydeployservice")
    public boolean onlydeployService(@RequestParam("serviceName") String serviceName, @RequestParam("port")int port){
        log.info("deploy service: " + serviceName);
        return this.k8sutils.onlydeployservice(serviceName, port);
    }

    /**
     * 查看某项服务是否存在，存在的话，返回这个服务，不存在的话，则返回null
     * @return
     */
   @GetMapping("/getService/{serviceName}")
    public MResponse<V1Service> getService(@PathVariable("serviceName") String serviceName){
       log.info("check the service : " + serviceName);
       return new MResponse<V1Service>().data(this.k8sutils.getService(serviceName));
   }

   @GetMapping("/checkoutIfAllPodsRunning")
    public MResponse<Boolean> checkoutIfAllPodsRunning(){
       log.info("checkout if all the pods is ready");
       return new MResponse<Boolean>().data(this.k8sutils.checkoutIfAllPodsRunning());
    }
    @GetMapping("/deletenotRun")
    public void deletenotRun(){
        log.info("checkout if all the pods is ready");
        this.k8sutils.deletenotRun();
    }

    // deploy give-command-service
    @GetMapping("/deployGiveCommand/{name}/{times}")
    public MResponse<ResultDeploy> onlydeployGiveCommandService(@PathVariable("name") String name, @PathVariable("times")int times){
        log.info("deploy give-command-service in every node");
        ResultDeploy resultDeploy = this.k8sutils.deployGive(name, times);
        return new MResponse<ResultDeploy>().data(resultDeploy==null?new ResultDeploy():resultDeploy);
    }


    @GetMapping("/deleteService/{serviceName}")
    public void deleteService(@PathVariable("serviceName") String serviceName){
       log.info("delete service : " + serviceName);
       this.k8sutils.deleteService(serviceName);
    }

    @GetMapping("/deleteServiceAndPods/{serviceName}")
    public void deleteServiceAndPods(@PathVariable("serviceName") String serviceName){
        log.info("delete service : " + serviceName);
        this.k8sutils.deleteServiceAndPods(serviceName);
    }
}
