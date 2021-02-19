package cn.net.yzl.crm.customer.utils.date;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    //获取当天（按当前传入的时区）00:00:00所对应时刻的long型值
    public static Date getCurrentDateStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    //获取昨天（按当前传入的时区）00:00:00所对应时刻的long型值
    public static Date getYesterdayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
