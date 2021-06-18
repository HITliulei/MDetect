package com.ll.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/5
 */
public class MFileUtils {


    public static void writeFile(File file , String content){
        try {
//            FileWriter fw = new FileWriter(file, true);
//            PrintWriter pw = new PrintWriter(fw);
//            pw.println(content);
//            pw.flush();
//            fw.flush();
//            pw.close();
//            fw.close();
            FileUtils.write(file, content,true);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{
//        writeFile(new File("tmp/test/asdf"), "qerqwlkjfakjniahg");
        FileUtils.write(new File("/tmp/test/asdf"),"qwerqwerqwer"+System.currentTimeMillis()+ IOUtils.LINE_SEPARATOR,true);
    }
}


class Tailerlisten extends TailerListenerAdapter {

    private Logger logger = LogManager.getLogger(Tailerlisten.class);

    private String logstasIp;
    private int logstashPort;

    public Tailerlisten(String logstasIp , int port){
        this.logstashPort = port;
        this.logstasIp = logstasIp;
    }

    @Override
    public void fileNotFound() {
        super.fileNotFound();
    }

    @Override
    public void fileRotated() {
        super.fileRotated();
    }

    @Override
    public void handle(String line) {
        logger.info("写入文件 ： " + line);
        //发送到logstash
    }
    @Override
    public void handle(Exception ex) {
        ;
    }
}