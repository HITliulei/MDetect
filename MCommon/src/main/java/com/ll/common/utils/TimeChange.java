package com.ll.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/8/9
 */
public class TimeChange {

    /**
     * 接收的时间 格式为 2020.08.01.21  年.月.日
     * @param date
     * @return
     */
    public static Date getDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        try {
            return sdf.parse(date);
        }catch (ParseException px){
            return null;
        }
    }

    /**
     * 接收的时间 格式为 2020.08.01  年.月.日
     * @param date
     * @return
     */
    public static Date getDateWithoutHour(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        try {
            return sdf.parse(date);
        }catch (ParseException px){
            return null;
        }
    }

    /**
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH");
        return sdf.format(date);
    }
}
