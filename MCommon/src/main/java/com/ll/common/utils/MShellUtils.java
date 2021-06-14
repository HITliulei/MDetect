package com.ll.common.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 */
public class MShellUtils {


    private static Logger logger = LogManager.getLogger(MShellUtils.class);

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
        Process process = null;
        InputStream in = null;
        String s = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            System.out.println(process);
            in = process.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            LineNumberReader input = new LineNumberReader(ir);
            while ((s = input.readLine()) != null) {
                logger.info(s);
            }
            process.waitFor();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String runShellWithOne(String shStr, File file) {
        Process process = null;
        InputStream in = null;
        String s = null;
        try {
            process = Runtime.getRuntime().exec(shStr,null,file);
            in = process.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            LineNumberReader input = new LineNumberReader(ir);
            while ((s = input.readLine()) != null) {
                logger.info(s);
            }
            process.waitFor();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean onlyRunShell(String shStr){
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            return true;
        }catch (IOException ioException) {
            System.out.println("执行脚本出现错误");
            ioException.printStackTrace();
            return false;
        }
    }


    public static boolean onlyRunShell(String shStr, File file) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(shStr, null, file);
            int statux = process.waitFor();
            if (statux != 0){
                System.out.println("处于没有执行的状态");
                return false;
            }else{
                System.out.println("执行了");
                process.exitValue();
            }
            return true;
        } catch (IOException | InterruptedException ioException) {
            System.out.println("执行脚本出现错误");
            ioException.printStackTrace();
            return false;
        }
    }
}
