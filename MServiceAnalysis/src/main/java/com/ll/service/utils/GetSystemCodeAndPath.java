package com.ll.service.utils;

import com.ll.common.config.ServiceConfig;
import com.ll.service.bean.MPathInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */
public class GetSystemCodeAndPath {

    private static Logger logger = LogManager.getLogger(GetSystemCodeAndPath.class);


    private static String[] serviceList = {
            "ts-voucher-service", "ts-verification-code-service", "ts-travel-service", "ts-travel-plan-service", "ts-travel2-service", "ts-train-service",
            "ts-ticketinfo-service", "ts-ticket-office-service", "ts-station-service", "ts-auth-service", "ts-security-service", "ts-seat-service", "ts-route-service",
            "ts-route-plan-service", "ts-rebook-service", "ts-price-service", "ts-preserve-service", "ts-preserve-other-service",
            "ts-payment-service", "ts-order-service", "ts-order-other-service",
            "ts-news-service", "ts-notification-service", "ts-user-service", "ts-inside-payment-service", "ts-food-service",
            "ts-food-map-service", "ts-execute-service", "ts-contacts-service", "ts-consign-service", "ts-consign-price-service",
            "ts-config-service", "ts-cancel-service", "ts-basic-service", "ts-assurance-service", "ts-admin-user-service", "ts-admin-travel-service",
            "ts-admin-route-service", "ts-admin-order-service", "ts-admin-basic-info-service"};

    public static Map<String, MPathInfo> getcode(String gitUrl, String branch){
        System.out.println(serviceList.length);
        logger.info("get service Path ");
        Map<String, MPathInfo> map = new HashMap<>();
        String[] urls = gitUrl.split("/");
        String projectName = urls[urls.length - 1].split("\\.")[0];
        String base_path = ServiceConfig.CODE_DIWNLOAD_PATH + "/" + projectName + "_" + branch;

        GetSourceCode.deleteDir(base_path);
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("boylei","BoyLei.ll980213");
        try {
            logger.info("clone source code");
            Git git = Git.cloneRepository().
                    setURI(gitUrl).
                    setBranch(branch).
                    setCredentialsProvider(usernamePasswordCredentialsProvider).
                    setDirectory(new File(base_path)).call();
            for (String serviceName: serviceList){
                MPathInfo mPathInfo = getMathInfo(serviceName, base_path);
                if (mPathInfo == null){
                    continue;
                }
                mPathInfo.setServiceName(serviceName);
                map.put(serviceName, mPathInfo);
            }
            git.close();
        }catch (GitAPIException e){
            logger.info("git clone 出错误");
            e.printStackTrace();
        }
        return map;
    }


    public static MPathInfo getcodeByserviceName(String gitUrl, String branch, String serviceName){
        logger.info("获取 路径信息: " + serviceName);
        String[] urls = gitUrl.split("/");
        String projectName = urls[urls.length - 1].split("\\.")[0];
        String base_path = ServiceConfig.CODE_DIWNLOAD_PATH + "/" + projectName + "_" + branch;
        GetSourceCode.deleteDir(base_path);
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("boylei","BoyLei.ll980213");
        try {
            logger.info(" clone 相关的代码仓库");
            Git git = Git.cloneRepository().
                    setURI(gitUrl).
                    setBranch(branch).
                    setCredentialsProvider(usernamePasswordCredentialsProvider).
                    setDirectory(new File(base_path)).call();
            MPathInfo mPathInfo = getMathInfo(serviceName, base_path);
            mPathInfo.setServiceName(serviceName);
            git.close();
            logger.info("获取的路径信息为" +  mPathInfo);
            return mPathInfo;
        }catch (GitAPIException e){
            logger.info("git clone 出错误");
            e.printStackTrace();
            return null;
        }
    }


    public static MPathInfo getMathInfo(String serviceName, String basePath){
        logger.info("获取服务的信息路径信息： " + serviceName );
        String path = basePath + "/" + serviceName;
        MPathInfo MPathInfo = new MPathInfo();
        File file_findapplication = new File(path + "/" + "src/main/resources");
        if (!file_findapplication.exists()){
            return null;
        }
        String ymlfile = path +"/src/main/resources/" + GetSourceCode.getYmlPath(file_findapplication);

        MPathInfo.setApplicationPath(ymlfile);
        List<File> pathList = GetSourceCode.getListFiles(new File(path + "/src/main/java"));
        List<String> listPath = new ArrayList<>();
        for (File file : pathList) {
            if (GetSourceCode.ifController(file)) {
                listPath.add(file.toString().replace("\\", "/"));
            }
        }
        MPathInfo.setControllerListPath(listPath);
        return MPathInfo;


    }
}
