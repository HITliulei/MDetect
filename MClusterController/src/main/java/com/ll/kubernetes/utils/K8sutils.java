package com.ll.kubernetes.utils;


import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MResponse;
import com.ll.kubernetes.bean.Deployinfo;
import com.ll.kubernetes.bean.MDockerInfoBean;
import com.ll.kubernetes.bean.MPodDockerInfo;
import com.ll.kubernetes.bean.Node.Node;
import com.ll.kubernetes.bean.Node.NodeList;
import com.ll.common.bean.deployInfoCollect.PodInfo;
import com.ll.common.bean.deployInfoCollect.ResultDeploy;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/3
 */

@Component
public class K8sutils {

    private static String ip = "172.31.43.94:5000";
    private static String baseName = "framework";
//    private static String baseName = "leizipkin";
//    private static String baseName = "nocirclesmell";
//    private static String baseName = "abstract";
//    private static String baseName = "database";


    private static String base_path = "/home/ubuntu/data";

    private ApiClient client;
    private CoreV1Api coreV1Api;
    private AppsV1Api apiInstance;


    private ExtensionsV1beta1Api extensionsV1beta1Api;
    private static Logger logger = LogManager.getLogger(K8sutils.class);

    public K8sutils(String k8sClientUrl) {
        this.initConnection(k8sClientUrl);
    }

    public K8sutils() {
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
            this.coreV1Api = new CoreV1Api(this.client);
            this.apiInstance = new AppsV1Api(this.client);
            this.extensionsV1beta1Api = new ExtensionsV1beta1Api(this.client);
        } catch (IOException e) {
            logger.debug(e);
        }
    }


    public Map<String, String> getAllnode(){
        Map<String, String> map = new HashMap<>();
        V1NodeList v1NodeList = null;
        try {
            v1NodeList = this.coreV1Api.listNode(null,
                    null, null, null, null, null,
                    null, null, null);
            List<V1Node> items = v1NodeList.getItems();
            for(V1Node v1Node:items){
                Map<String, String> labels = v1Node.getMetadata().getLabels();
                for(String string: labels.keySet()){
                    if(string.equals("node")){
                        map.put(v1Node.getMetadata().getName(), labels.get(string));
                        break;
                    }
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return map;
    }

    public NodeList getNodeInfo(){
        List<Node> list = new ArrayList<>();
        try{
            V1NodeList  v1NodeList = this.coreV1Api.listNode(null,
                    null, null, null, null, null,
                    null, null, null);
            List<V1Node> items = v1NodeList.getItems();
            for(V1Node v1Node:items){
                Node node = new Node();
                node.setNode(v1Node.getMetadata().getName());
                List<V1NodeAddress> a = v1Node.getStatus().getAddresses();
                node.setIp(a.get(0).getAddress());
                node.setLabel(null);
                Map<String, String> labels = v1Node.getMetadata().getLabels();
                for(String string: labels.keySet()){
                    if(string.equals("node")){
                        node.setLabel(labels.get(string));
                        break;
                    }
                }
                list.add(node);
            }
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
        NodeList nodeList = new NodeList();
        nodeList.setList(list);
        return nodeList;
    }

    public String getNodeBylabel(String label){
        try{
            V1NodeList  v1NodeList = this.coreV1Api.listNode(null,
                    null, null, null, null, null,
                    null, null, null);
            List<V1Node> items = v1NodeList.getItems();
            for(V1Node v1Node:items){
                Map<String, String> labels = v1Node.getMetadata().getLabels();
                if (labels.keySet().contains("node") && labels.get("node").equals(label)){
                    return v1Node.getMetadata().getName();
                }
            }
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
        return null;
    }

    public String getLabelByNode(String node){
        try{
            V1NodeList  v1NodeList = this.coreV1Api.listNode(null,
                    null, null, null, null, null,
                    null, null, null);
            List<V1Node> items = v1NodeList.getItems();
            for(V1Node v1Node:items){
                if (v1Node.getMetadata().getName().equals(node)){
                    return v1Node.getMetadata().getLabels().get("node");
                }
            }
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
        return null;
    }

    public List<V1Pod> getALlPod(){
        try {
            return this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null).getItems();
        }catch (ApiException a){
            a.printStackTrace();
            return null;
        }
    }

    public MResponse<List<String>> getAllpodsName(){
        List<String> result = new ArrayList<>();
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                result.add(item.getMetadata().getName());
            }
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
        MResponse<List<String>> MResponse = new MResponse<>();
        MResponse.setStatus("success");
        MResponse.setData(result);
        return MResponse;
    }

    public MDockerInfoBean getDockerInfoByIpAddr(String ipAddr) {
        MDockerInfoBean infoBean = null;
        try {
            infoBean = new MDockerInfoBean();
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (item.getStatus() != null &&
                        "Running".equals(item.getStatus().getPhase()) && ipAddr.equals(item.getStatus().getPodIP())) {
                    logger.info("find the docker instance : " + item.toString());
                    infoBean.setPodIp(item.getStatus().getPodIP());
                    infoBean.setHostIp(item.getStatus().getHostIP());
                    infoBean.setInstanceId(item.getMetadata().getName());
                    infoBean.setServiceName(item.getMetadata().getLabels().get("app"));
                    Map<String, String> node = getAllnode();
                    infoBean.setNodeLabel(node.get(item.getSpec().getNodeName()));
                }
            }
        } catch (ApiException e) {
            logger.debug(e);
        }
        return infoBean;
    }

    public List<MDockerInfoBean> getAllPodinfoWItoutDocker(){
        List<MDockerInfoBean> result = new ArrayList<>();
        try {

            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (item.getStatus() != null && "Running".equals(item.getStatus().getPhase())) {
                    MDockerInfoBean infoBean = new MDockerInfoBean();
                    infoBean.setPodIp(item.getStatus().getPodIP());
                    infoBean.setHostIp(item.getStatus().getHostIP());
                    infoBean.setInstanceId(item.getMetadata().getName());
                    infoBean.setServiceName(item.getMetadata().getLabels().get("app"));
                    Map<String, String> node = getAllnode();
                    infoBean.setNodeLabel(node.get(item.getSpec().getNodeName()));
                    result.add(infoBean);
                }
            }
        } catch (ApiException e) {
            logger.debug(e);
        }
        return result;
    }

    public MDockerInfoBean getDockerInfoByPodName(String podname) {
        MDockerInfoBean infoBean = null;
        try {
            infoBean = new MDockerInfoBean();
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (item.getStatus() != null &&
                        "Running".equals(item.getStatus().getPhase()) && podname.equals(item.getMetadata().getName())) {
                    logger.info("find the docker instance : " + item.toString());
                    infoBean.setPodIp(item.getStatus().getPodIP());
                    infoBean.setHostIp(item.getStatus().getHostIP());
                    infoBean.setInstanceId(item.getMetadata().getName());
                    infoBean.setServiceName(item.getMetadata().getLabels().get("app"));
                    Map<String, String> node = getAllnode();
                    infoBean.setNodeLabel(node.get(item.getSpec().getNodeName()));
                }
            }
        } catch (ApiException e) {
            logger.debug(e);
        }
        return infoBean;
    }


    public MPodDockerInfo getPodDockerInfoByName(String podname){
        String bash = "kubectl describe pod "+podname+" | grep docker://";
        String result = Utils.runShellWithOne(bash).trim();
        String alldockerId = result.split("//")[1];
        String dockerId = alldockerId.substring(0, 12);
        MPodDockerInfo mPodDockerInfo = new MPodDockerInfo();
        MDockerInfoBean mDockerInfoBean = getDockerInfoByPodName(podname);
        mPodDockerInfo.setAllDockerId(alldockerId);
        mPodDockerInfo.setDockerId(dockerId);
        mPodDockerInfo.setMDockerInfoBean(mDockerInfoBean);
        Map<String, String> node = this.getAllnode();
        for (String string: node.keySet()){
            if (mDockerInfoBean.getNodeLabel().equals(node.get(string))){
                mPodDockerInfo.setNode(string);
                break;
            }
        }
        return mPodDockerInfo;
    }

    public MPodDockerInfo getPodDockerInfoByIp(String podIp){
        MDockerInfoBean mDockerInfoBean = this.getDockerInfoByIpAddr(podIp);
        String bash = "kubectl describe pod "+mDockerInfoBean.getInstanceId()+" | grep docker://";
        String result = Utils.runShellWithOne(bash).trim();
        String alldockerId = result.split("//")[1];
        String dockerId = alldockerId.substring(0, 12);

        MPodDockerInfo mPodDockerInfo = new MPodDockerInfo();
        mPodDockerInfo.setAllDockerId(alldockerId);
        mPodDockerInfo.setDockerId(dockerId);
        mPodDockerInfo.setMDockerInfoBean(mDockerInfoBean);
        Map<String, String> node = this.getAllnode();
        for (String string: node.keySet()){
            if (mDockerInfoBean.getNodeLabel().equals(node.get(string))){
                mPodDockerInfo.setNode(string);
                break;
            }
        }
        return mPodDockerInfo;
    }


    public MResponse<List<MPodDockerInfo>> getAllPodDockerInfo(){
        List<String> allpodName = this.getAllpodsName().getData();
        List<MPodDockerInfo> result = new ArrayList<>();
        MResponse<List<MPodDockerInfo>> MResponse = new MResponse<>();
        for (String podname: allpodName){
            result.add(this.getPodDockerInfoByName(podname));
        }
        MResponse.setData(result);
        MResponse.setStatus("success");
        return MResponse;
    }


    public V1Pod getPOdInfoByName(String podname) {
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (item.getStatus() != null &&
                        "Running".equals(item.getStatus().getPhase()) && podname.equals(item.getMetadata().getName())) {
                    logger.info("find the docker instance : " + item.toString());
                    return item;
                }
            }
        } catch (ApiException e) {
            logger.debug(e);
        }
        return null;
    }

    public boolean checkIfDockerRunning(String ipAddr) {
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (ipAddr.equals(item.getStatus().getPodIP())) {
                    if (item.getStatus().getPhase().equals("Running")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e);
        }
        return false;
    }

    // 只进行相应的部署，并不进行服务的发布（用于横向扩展）
    public Boolean onlydeployDeployment(Deployinfo deployinfo){
        V1Deployment v1Deployment = Utils.readDeploymentYaml("deployment");
        if (v1Deployment==null){
            logger.info("v1Deployment is null");
            return false;
        }
        String serviceName = deployinfo.getServiceName();
        Map<String, Integer> deploy = deployinfo.getMap();
        for (String nodeLabel: deploy.keySet()){
            if (deploy.get(nodeLabel) == 0){
                continue;
            }else{
                v1Deployment.getMetadata().setName(serviceName);
                v1Deployment.getSpec().getSelector().getMatchLabels().put("app",serviceName);
                v1Deployment.getSpec().setReplicas(deploy.get(nodeLabel));
                v1Deployment.getSpec().getTemplate().getMetadata().getLabels().put("app", serviceName);
                v1Deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setName(serviceName);
                v1Deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(ip + "/"+baseName+"_"+serviceName);
                v1Deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0).setContainerPort(deployinfo.getPort());
                v1Deployment.getSpec().getTemplate().getSpec().getNodeSelector().put("node", nodeLabel);
                v1Deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getReadinessProbe().getTcpSocket().setPort(new IntOrString(deployinfo.getPort()));
                V1Deployment resultDeployment = null;
                try {
                    resultDeployment = apiInstance.createNamespacedDeployment("default", v1Deployment, null,null,null);
                    logger.info("create the resultDeployment success : " + resultDeployment);
                }catch (ApiException e){
                    logger.debug(e);
                }
            }
        }
        return true;
    }
    /**
     * 部署的是pod， 不是deployment,并且不发布服务
     * @param deployinfo
     */
    public ResultDeploy onlyDeployPod(Deployinfo deployinfo){
        ResultDeploy resultDeploy = new ResultDeploy();
        V1Pod v1Pod = Utils.readPodYaml("pod");
        if (v1Pod==null){
            logger.info("v1Pod is null");
            return null;
        }
        String serviceName = deployinfo.getServiceName();
        String serviceVersion = MSvcVersion.fromStr(deployinfo.getServiceVersion()).toCommonStr();
        String serviceId = serviceName + "_" + serviceVersion;
        String serviceImageUrl = ip+"/"+serviceName + ":" + serviceVersion;

        resultDeploy.setServiceName(serviceName);
        resultDeploy.setServiceVersion(serviceVersion);
        resultDeploy.setPort(deployinfo.getPort());

        Map<String, List<PodInfo>> map = new HashMap<>();
        Map<String, Integer> deploy = deployinfo.getMap();
        int sum = 0;
        for (String nodeLabel: deploy.keySet()){
            if (deploy.get(nodeLabel) == 0){
                continue;
            }else{
                List<PodInfo> list = new ArrayList<>();
                for (int i=0;i<deploy.get(nodeLabel);i++){
                    PodInfo podInfo = new PodInfo();
                    String uuid = UUID.randomUUID().toString().substring(24);
                    String metadata = serviceId + "-" + uuid;
                    podInfo.setPodName(metadata);
                    v1Pod.getMetadata().setName(metadata);
                    v1Pod.getMetadata().getLabels().put("app", serviceName);
                    v1Pod.getSpec().getContainers().get(0).setName(serviceName);
                    v1Pod.getSpec().getContainers().get(0).setImage(ip+ "/" + baseName + "_" + serviceName);
                    v1Pod.getSpec().getContainers().get(0).getPorts().get(0).setContainerPort(deployinfo.getPort());
                    v1Pod.getSpec().getNodeSelector().put("node", nodeLabel);
                    v1Pod.getSpec().getContainers().get(0).getReadinessProbe().getTcpSocket().setPort(new IntOrString(deployinfo.getPort()));
                    try {
                        V1Pod resultPod = this.coreV1Api.createNamespacedPod("default",v1Pod, null,null,null);
                        logger.info("create pod success: " + resultPod);
                        podInfo.setPodIp(resultPod.getStatus().getPodIP());
                        podInfo.setNode(getNodeBylabel(nodeLabel));
                        list.add(podInfo);
                        sum =sum+1;
                    }catch (ApiException apiException){
                        logger.info("create pod faild: " + serviceName + ", times + "+ i);
                        apiException.printStackTrace();
                        return null;
                    }
                }
                map.put(nodeLabel,list);
            }
        }
        if (sum == 0) {
            return null;
        }
        resultDeploy.setMap(map);
        return resultDeploy;
    }

    public boolean onlydeployservice(String serviceName, int port, String serviceVersion){
        if (getService(serviceName) == null){
            V1Service v1Service = Utils.readServiceYaml("service");
            v1Service.getMetadata().setName(serviceName + "_" + MSvcVersion.fromStr(serviceVersion).toCommonStr());
            v1Service.getSpec().getSelector().put("app", serviceName);
            v1Service.getSpec().setType("ClusterIP");
            v1Service.getSpec().getPorts().get(0).setName("http");
            v1Service.getSpec().getPorts().get(0).setPort(port);
            try {
                this.coreV1Api.createNamespacedService("default", v1Service,null,null, null);
                logger.info("create the service Success : " + serviceName);
            }catch (ApiException e){
                logger.info(e.getResponseBody());
                logger.info("------");
                e.printStackTrace();
                return false;
            }
        }else{
            logger.info("the num pods deployed is 0, failed to create the service");
            return false;
        }
        return true;
    }


    public V1Service getService(String serviceName){
        try {
            V1ServiceList v1ServiceList = this.coreV1Api.listNamespacedService("default", null,null,null,null,null,null,null,null,null);
            boolean ifExists = false;
            for (V1Service v1Service1: v1ServiceList.getItems()){
                if (v1Service1.getMetadata().getName().equals(serviceName)){
                    return v1Service1;
                }
            }
        }catch (ApiException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是的所有的pod处于 running状态
     * @return
     */
    public boolean checkoutIfAllPodsRunning(){
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            List<V1Pod> v1pods = list.getItems();
            System.out.println(v1pods.size());
            for (V1Pod v1Pod : v1pods){
                System.out.println(v1Pod.getStatus().getPhase());
                if (!v1Pod.getStatus().getPhase().equals("Running")) {
                    return false;
                }
            }
        }catch (ApiException e){
            e.printStackTrace();
            logger.error(e);
            return false;
        }
        return true;
    }

    public void deletenotRun(){
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod v1Pod : list.getItems()){
                if (!v1Pod.getStatus().getPhase().equals("Running")) {
                    this.coreV1Api.deleteNamespacedPod(
                            v1Pod.getMetadata().getName(),
                            "default",
                            null,
                            null,
                            null,
                            null,
                            null,
                            "Foreground"
                    );
                    logger.info(String.format("Pod %s was deleted.",  v1Pod.getMetadata().getName()));
                }
            }
        }catch (ApiException e){
            e.printStackTrace();
            logger.error(e);
        };
    }

    // 只用来部署command的pod
    public ResultDeploy onlyDeployCommandPod(Deployinfo deployinfo){
        ResultDeploy resultDeploy = new ResultDeploy();
        V1Pod v1Pod = Utils.readPodYaml("commandPod");
        if (v1Pod==null){
            logger.info("v1Pod is null");
            return null;
        }
        String serviceName = deployinfo.getServiceName();
        resultDeploy.setServiceName(serviceName);
        resultDeploy.setPort(deployinfo.getPort());
        Map<String, List<PodInfo>> map = new HashMap<>();
        Map<String, Integer> deploy = deployinfo.getMap();
        int sum = 0;
        int h = 0;
        for (String nodeLabel: deploy.keySet()){
            if (deploy.get(nodeLabel) == 0){
                continue;
            }
            List<PodInfo> list = new ArrayList<>();
            for (int i=0;i<deploy.get(nodeLabel);i++){
                PodInfo podInfo = new PodInfo();
                String uuid = UUID.randomUUID().toString().substring(24);
                String metadata = serviceName + "-" + uuid;
                podInfo.setPodName(metadata);
                v1Pod.getMetadata().setName(metadata);
                v1Pod.getMetadata().getLabels().put("app", serviceName+h);
                v1Pod.getSpec().getContainers().get(0).setName(serviceName+h);
                v1Pod.getSpec().getContainers().get(0).setImage(ip + "/framework_"+serviceName);
                v1Pod.getSpec().getContainers().get(0).getPorts().get(0).setContainerPort(deployinfo.getPort());
                v1Pod.getSpec().getNodeSelector().put("node", nodeLabel);
                try {
                    V1Pod resultPod = this.coreV1Api.createNamespacedPod("default",v1Pod, null,null,null);
                    logger.info("create pod success: " + resultPod);
                    podInfo.setPodIp(resultPod.getStatus().getPodIP());
                    podInfo.setNode(getNodeBylabel(nodeLabel));
                    list.add(podInfo);
                    sum =sum+1;
                }catch (ApiException apiException){
                    logger.info("create pod faild: " + serviceName + ", times + "+ i);
                    apiException.printStackTrace();
                    return null;
                }
            }
            map.put(nodeLabel,list);
            h = h +1;
        }
        if (sum == 0) {
            return null;
        }
        resultDeploy.setMap(map);
        return resultDeploy;
    }



    public ResultDeploy deployGive(String name, int times){
        String serviceName = "ts-a-givecommand";
        int port = 32002;
        Deployinfo deployinfo = new Deployinfo();
        deployinfo.setServiceName(serviceName);
        deployinfo.setPort(port);
        Map<String, Integer> de = new HashMap<>();
        Map<String, String> map = getAllnode();
        for (String node : map.keySet()){
            // 不使用master节点了， 太卡了
            if (node.contains("master")){
                continue;
            }
            String node_label = map.get(node);
            de.put(node_label, 1);
        }
        System.out.println(de);
        deployinfo.setMap(de);
        // deployment
        ResultDeploy resultDeploy = onlyDeployCommandPod(deployinfo);
        if (resultDeploy == null){
            return null;
        }
        for(int i = 0;i<resultDeploy.getMap().size();i++){
            int aport = port+i;
            V1Service v1Service = Utils.readServiceYaml("service");
            v1Service.getMetadata().setName(serviceName+i);
            v1Service.getSpec().getSelector().put("app", serviceName+i);
            v1Service.getSpec().setType("NodePort");
            v1Service.getSpec().getPorts().get(0).setName("http");
            v1Service.getSpec().getPorts().get(0).setPort(port);
            v1Service.getSpec().getPorts().get(0).setNodePort(aport);
            try {
                coreV1Api.createNamespacedService("default", v1Service,null,null, null);
                logger.info("create the service Success : " + serviceName);
            }catch (Exception e){
                logger.info("create the give-command-serivice, failed");
                logger.debug(e);
                return null;
            }
        }
        // 更新resultDeploy, 更新ip地址
        try {
            Thread.sleep(180000);
            Map<String, List<PodInfo>> maps = resultDeploy.getMap();
            for (String str: maps.keySet()){
                List<PodInfo> newlist = new ArrayList<>();
                List<PodInfo> oldlist = maps.get(str);
                for (PodInfo podInfo: oldlist){
                    podInfo.setPodIp(getPOdInfoByName(podInfo.getPodName()).getStatus().getPodIP());
                    newlist.add(podInfo);
                }
                maps.put(str, newlist);
            }
            resultDeploy.setMap(maps);
            System.out.println("更新map成功，得到的结果为 ： " + resultDeploy.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Utils.writeFile(new File(base_path + "/deploydata/" + name+"_"+times), resultDeploy.toString());

        // 顺便更新 mongo的service
        // todo : 更新mongo的数据到 deploymeny文件中：
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (item.getMetadata().getName().contains("mongo")){
                    String mongoName = item.getMetadata().getName();
                    ResultDeploy mongo = new ResultDeploy();
                    mongo.setServiceName(mongoName.substring(0, mongoName.indexOf("mongo") + 5));
                    mongo.setPort(27017);
                    Map<String, List<PodInfo>> mongoMap = new HashMap<>();
                    List<PodInfo> mongoList = new ArrayList<>();
                    mongoList.add(new PodInfo(mongoName, item.getStatus().getPodIP(), item.getSpec().getNodeName()));
                    mongoMap.put(getLabelByNode(item.getSpec().getNodeName()), mongoList);
                    mongo.setMap(mongoMap);
                    Utils.writeFile(new File(base_path + "/deploydata/" + name+"_"+times), mongo.toString());
                }
            }
        }catch (ApiException a){
            a.printStackTrace();
        }
        return resultDeploy;
    }

    public void deleteServiceAndPods(String serviceName){
        try {
            V1PodList list = this.coreV1Api.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);
            for (V1Pod v1Pod : list.getItems()){
                if (v1Pod.getMetadata().getLabels().get("app").equals(serviceName)){
                    // 删除此pod
                    this.deletePod(v1Pod.getMetadata().getName());
                }
            }
            // 删除服务
            deleteService(serviceName);
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
    }

    public void deleteService(String serviceName){
        logger.info("delete servicename: " + serviceName);
        try {
            this.coreV1Api.deleteNamespacedService(serviceName,"default",null,null,null,null,null,null);
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
    }

    public void deletePod(String instanceId){
        logger.info("delete pods : " + instanceId);
        try {
            this.coreV1Api.deleteNamespacedPod(
                    new String(instanceId),
                    "default",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Foreground"
            );
        }catch (ApiException apiException){
            apiException.printStackTrace();
        }
    }

}
