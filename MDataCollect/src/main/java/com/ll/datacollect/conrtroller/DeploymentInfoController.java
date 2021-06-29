package com.ll.datacollect.conrtroller;

import com.ll.common.utils.MResponse;
import com.ll.datacollect.bean.EXDeploy;
import com.ll.datacollect.utils.SaveDataInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */

@RestController
@RequestMapping("/deploymentInfo")
public class DeploymentInfoController {
    private static Logger logger = LogManager.getLogger(DeploymentInfoController.class);


    private static String path = "/home/ubuntu";

    @PostMapping("/savedeploydata")
    public void getdeploy(@RequestBody EXDeploy exDploy, @RequestHeader HttpHeaders httpHeaders){
        logger.info("部署的信息为 : " + exDploy.toString());
        SaveDataInfo.savedeploydata(exDploy);
    }



    @GetMapping("/getDeployment/{name}/{times}")
    public MResponse<List<String>> getDeployment(@PathVariable("name")String name, @PathVariable("times")int times){
        logger.info("getDeploymentInfo");
        String base = path + "/data/deploydata/";
        String filename = base + name +"_" + times;
        File file = new File(filename);
        if (!file.exists()){
            return new MResponse<List<String>>().data(new ArrayList<>());
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> list = new ArrayList<>();
            String str = null;
            while((str = bufferedReader.readLine()) != null){
                if(str.contains("mongo") || str.contains("ts-a-givecommand")){
                    continue;
                }
                list.add(str);
            }
            inputStream.close();
            bufferedReader.close();
            return new MResponse<List<String>>().data(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new MResponse<List<String>>().data(new ArrayList<>());
    }
}
