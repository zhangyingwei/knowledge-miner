package com.zhangyingwei.spiders.konwledge.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangyw on 2018/2/2.
 */
public class DateUtils {
    public static String currentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(new Date());
    }

    public static String currentFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date()).concat(".html");
    }
}
