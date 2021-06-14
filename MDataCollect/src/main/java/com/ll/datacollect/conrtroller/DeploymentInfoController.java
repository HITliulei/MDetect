package com.ll.datacollect.conrtroller;

import com.ll.datacollect.bean.EXDeploy;
import com.ll.datacollect.utils.SaveDataInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@RestController
@RequestMapping("/deploymentInfo")
public class DeploymentInfoController {

    private static Logger logger = LogManager.getLogger(DeploymentInfoController.class);

    @PostMapping("/savedeploydata")
    public void getdeploy(@RequestBody EXDeploy exDploy, @RequestHeader HttpHeaders httpHeaders){
        logger.info("部署的信息为 : " + exDploy.toString());
        SaveDataInfo.savedeploydata(exDploy);
    }
}
