package cn.net.yzl.crm.customer.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * 时间转换工具类
 * wangzhe
 * 2021-03-02
 */
public class DealDateUtil {


    /**
     * 获取指定日期的开始时间 (默认时区)
     * @param date
     * @return
     */
    public static Date getStart(Date date) {
        return getStart(date,ZoneId.systemDefault());
    }

    /**
     * 获取指定日期的开始当天的结束时间 (指定时区)
     * @param date
     * @return
     */
    public static Date getStart(Date date,ZoneId zone) {
        return getDate(date, zone, LocalTime.MIN);
    }


    /**
     * 获取指定日期的开始当天的结束时间 (默认时区)
     * @param date
     * @return
     */
    public static Date getEnd(Date date) {
        return getEnd(date, ZoneId.systemDefault());
    }


    /**
     * 获取指定日期的开始当天的结束时间 (指定时区)
     * @param date
     * @param zone
     * @return
     */
    public static Date getEnd(Date date,ZoneId zone) {
        return getDate(date, zone, LocalTime.MAX);
    }


    /**
     * 时间转换
     * @param date
     * @param zone
     * @param m
     * @return
     */
    public static Date getDate(Date date, ZoneId zone, LocalTime m) {
        if(null == date) {
            return null;
        }
        LocalDate toLocalDate = date.toInstant().atZone(zone).toLocalDate();
        //开始时间
        LocalDateTime endTime=LocalDateTime.of(toLocalDate,m);
        //日期转换
        Date enddate=Date.from(endTime.atZone(zone).toInstant());
        return enddate;
    }







}
