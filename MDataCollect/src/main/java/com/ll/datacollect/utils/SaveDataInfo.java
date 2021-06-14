package com.ll.datacollect.utils;

import com.ll.common.bean.deployInfoCollect.ResultDeploy;
import com.ll.datacollect.bean.EXDeploy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */
public class SaveDataInfo {

    private static String BASE_PATH = "/home/liulei";

    private static String DEPLOY_DATA_PATH = BASE_PATH + "/data/deploydata";

    /**
     * 将所有的部署信息
     * @param exDeploy 部署信息
     */
    public static void savedeploydata(EXDeploy exDeploy){
        String filename = DEPLOY_DATA_PATH + "/" + exDeploy.getName() + "_" + exDeploy.getTimes();
        try {
            File file = new File(filename);
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for(ResultDeploy deploydata: exDeploy.getList()){
                out.write(deploydata.toString());
                out.write("\n");
            }
            out.flush();
            out.close();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
}
