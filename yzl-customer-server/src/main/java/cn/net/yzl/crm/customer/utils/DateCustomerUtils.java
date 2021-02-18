package cn.net.yzl.crm.customer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateCustomerUtils {

    public static Date beforeMonth(Date time, int month) throws ParseException {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(time);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -3);  //设置为前3月
        time = calendar.getTime();   //得到前3月的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM"); //设置时间格式
        String defaultStartDate = sdf.format(time);    //格式化前3月的时间

        return sdf.parse(defaultStartDate);
    }

    public static void main(String[] args) throws ParseException {

        System.err.println(beforeMonth(new Date(),1));
    }

}
