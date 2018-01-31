package com.zhangyingwei.spiders.konwledge.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:10
 * @desc:
 */

@Data
public class Konwledge {
    private String title;
    private String url;
    private String desc;
    private Set<String> emails;
    private String group;

    public String content() {
        return String.format("<div style='margin:10px;border-bottom:2px solid #f4f3f4;'><a style='color:#f4f3f4;font-size:15px;text-decoration:none;' href='%s'>%s</a><p style='font-size:13px;color:#f4f3f4;'>%s</p> </div>",this.url,this.title,this.desc);
    }

    public boolean isEmpty() {
        return StringUtils.isNotEmpty(this.title);
    }
}
