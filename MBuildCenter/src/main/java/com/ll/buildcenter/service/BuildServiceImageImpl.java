package com.ll.buildcenter.service;

import com.ll.common.config.ServiceConfig;
import com.ll.common.service.MSvcVersion;
import com.ll.common.utils.MShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/7
 */
@Service("BuildServiceImageImpl")
public class BuildServiceImageImpl implements BuildServiceImage{



    @Value("${docker.registry.ip}")
    private String registryIp;
    @Value("${docker.registry.port}")
    private int port;


    private static String allname = "train-ticket";

    @Override
    public String buildServiceImage(String serviceName, String serviceVersion) {
        String serviceImage = serviceName.toLowerCase() + ":" + MSvcVersion.fromStr(serviceVersion).toCommonStr();
        String localPath = ServiceConfig.TRACEINFO_PATH + File.separator + allname + File.separator + serviceName.toLowerCase();
        String strSh = new StringBuilder("cd ").append(localPath).append(" ; ").append("docker build -t ").append(serviceImage).append(" .").toString();
        return MShellUtils.runShellWithOne(strSh);
    }

    @Override
    public boolean compile() {
        String localPath = ServiceConfig.TRACEINFO_PATH + File.separator + allname;
        String sh = "cd " +localPath + " ; mvn clean package -Dmaven.test.skip=true";
        return MShellUtils.onlyRunShell(sh);
    }

    // push
    @Override
    public boolean pushImage(String imagesName) {
        // docker tag
        String dockerRe = new StringBuilder().append(registryIp).append(":").append(port).append("/").append(imagesName).toString();;
        String shStr = new StringBuilder("docker tag ")
                .append(imagesName)
                .append(" ").append(dockerRe).toString();
        //docker push
        String shStr1 =  new StringBuilder("docker push ").append(dockerRe).toString();
        MShellUtils.onlyRunShell(shStr);
        return MShellUtils.onlyRunShell(shStr1);
    }
}
