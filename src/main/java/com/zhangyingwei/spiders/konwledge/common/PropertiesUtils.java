package com.zhangyingwei.spiders.konwledge.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangyw on 2018/2/2.
 */
public class PropertiesUtils {
    private static Properties properties = new Properties();

    public static void load(String path) throws IOException {
        properties.load(new FileInputStream(path));
    }

    public static String get(String key) throws IOException {
        return properties.getProperty(key);
    }
}
