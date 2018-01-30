package com.zhangyingwei.spiders.konwledge.model;

import lombok.Data;

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
        return String.format("<div style='margin:5px; background: #4285f4;padding:10px;border-radius:2px;border-top:5px solid #fbbc05;'><a style='color:#f4f3f4;font-size:15px;text-decoration:none;' href='%s'>%s</a><span style='background-color:#ea4335; padding:2px 5px;margin:10px;color:#fff;border-radius:4px;'>%s</span><p style='font-size:13px;color:#f4f3f4;'>%s</p> </div>",this.url,this.title,this.group,this.desc);
    }
}
