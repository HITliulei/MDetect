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



    public static Map<String, MPathInfo> getcode(String gitUrl, String branch){
        Map<String, MPathInfo> map = new HashMap<>();
        GetSourceCode.deleteDir(ServiceConfig.CODE_DIWNLOAD_PATH);
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("boylei","BoyLei.ll980213");

        String[] urls = gitUrl.split("/");
        String projectName = urls[urls.length - 1].split("\\.")[0];
        String base_path = ServiceConfig.CODE_DIWNLOAD_PATH + "/" + projectName + "_" + branch;
        try {
            Git git = Git.cloneRepository().
                    setURI(gitUrl).
                    setBranch(branch).
                    setCredentialsProvider(usernamePasswordCredentialsProvider).
                    setDirectory(new File(base_path)).call();
            for (String serviceName: ServiceConfig.serviceList){
                MPathInfo mPathInfo = getMathInfo(serviceName, base_path);
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
        GetSourceCode.deleteDir(ServiceConfig.CODE_DIWNLOAD_PATH);
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("boylei","BoyLei.ll980213");

        String[] urls = gitUrl.split("/");
        String projectName = urls[urls.length - 1].split("\\.")[0];
        String base_path = ServiceConfig.CODE_DIWNLOAD_PATH + "/" + projectName + "_" + branch;
        try {
            Git git = Git.cloneRepository().
                    setURI(gitUrl).
                    setBranch(branch).
                    setCredentialsProvider(usernamePasswordCredentialsProvider).
                    setDirectory(new File(base_path)).call();
            MPathInfo mPathInfo = getMathInfo(serviceName, base_path);
            mPathInfo.setServiceName(serviceName);
            git.close();
            return mPathInfo;
        }catch (GitAPIException e){
            logger.info("git clone 出错误");
            e.printStackTrace();
            return null;
        }
    }


    public static MPathInfo getMathInfo(String serviceName, String basePath){
        String path = basePath + "/" + serviceName;
        MPathInfo MPathInfo = new MPathInfo();
        File file_findapplication = new File(path + "/" + "src/main/resources");
        String ymlfile = path + "src/main/resources/" + GetSourceCode.getYmlPath(file_findapplication);

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
