package com.ll.common.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/6
 */
public class MShellUtils {

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


    public static boolean onlyRunShell(String shStr){
        try {
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",shStr},null,null);
            return true;
        }catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {

    }
}
