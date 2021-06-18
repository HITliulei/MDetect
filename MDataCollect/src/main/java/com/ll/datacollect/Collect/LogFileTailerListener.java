package com.ll.datacollect.Collect;


import com.ll.datacollect.utils.LogstashUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Lei
 * @version 1.0
 * @date 2020/8/30
 */

public class LogFileTailerListener implements TailerListener {


    private Tailer tailer;
    private String logstashIp;
    private int logstashPort;

    private Logger logger = LogManager.getLogger(LogFileTailerListener.class);

    public LogFileTailerListener(String logstashIp, int logstashPort) {
        this.logstashPort = logstashPort;
        this.logstashIp = logstashIp;
    }
    @Override
    public void init(Tailer tailer) {
        this.tailer = tailer;
    }

    @Override
    public void fileNotFound() {
        logger.warn(tailer.getFile().getName() + " lost!");
        this.tailer.stop();
    }
    @Override
    public void fileRotated() {
    }

    @Override
    public void handle(String s) {
        logger.info("Tailer handles: " + s);
        if (s == null) {
            logger.info("Failed to parse: " + s + ", ignored");
            return;
        }
        LogstashUtils.sendInfoToLogstash(logstashIp, logstashPort, s);
    }

    @Override
    public void handle(Exception e) {

    }
}
