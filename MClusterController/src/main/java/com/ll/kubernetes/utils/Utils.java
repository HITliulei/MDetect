package com.ll.kubernetes.utils;

import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Yaml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/10
 */
public class Utils {
    private static Logger log = LogManager.getLogger(Utils.class);

//    private static String path = "/home/liulei/yaml";

    private static String path = "/home/ubuntu/yaml";

    private static String path1 = "src/yaml";

    private static String path_givecommand = "/home/liulei/SmellEx/RUNEX";

    public static V1Pod readPodYaml(String serviceName) {
        V1Pod pod = null;
        try {
            Object podYamlObj = Yaml.load(new File(path + File.separator +serviceName + ".yaml"));
            if (podYamlObj instanceof V1Pod) {
                pod = (V1Pod) podYamlObj;
            }
        } catch (Exception e) {
            log.debug(e);
        }
        return pod;
    }

    public static V1Deployment readDeploymentYaml(String serviceName) {
        log.info(" read Deployment now");
        File file = new File(path + File.separator +serviceName + ".yaml");
        if (file.exists()){
            V1Deployment deployment = null;
            try {
                Object podYamlObj = Yaml.load(file);
                if (podYamlObj instanceof V1Deployment) {
                    deployment = (V1Deployment) podYamlObj;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.debug(e);
            }
            return deployment;
        }else {
            log.warn(" file is not exit");
            return null;
        }
    }

    public static V1Service readServiceYaml(String serviceName){
        log.info(" read service now");
        File file = new File(path + File.separator +serviceName + ".yaml");
        if (file.exists()){
            V1Service service = null;
            try {
                Object podYamlObj = Yaml.load(file);
                if (podYamlObj instanceof V1Service) {
                    service = (V1Service) podYamlObj;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.debug(e);
            }
            return service;
        }
        else{
            log.warn(" file is not exit");
            return null;
        }
    }

    public static List<String> runShell(String shStr) {
        List<String> strList = new ArrayList<String>();
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            process.waitFor();
            while ((line = input.readLine()) != null){
                strList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strList;
    }

    public static String runShellWithOne(String shStr) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            process.waitFor();
            return input.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void writeFile(File file , String content){
        try {
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            fw.flush();
            pw.close();
            fw.close();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
}


