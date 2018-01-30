package com.zhangyingwei.spiders.konwledge.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:09
 * @desc:
 */
@Data
public class WebSiteConfig {
    private String url;
    private String group;
    private String itemCss;
    private ItemConfig itemConfig;
    private Set<String> emails;
    private int limit = 10;
}
