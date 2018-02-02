package com.zhangyingwei.spiders.konwledge.config;

import com.zhangyingwei.spiders.konwledge.common.PropertiesUtils;
import lombok.Getter;
import org.dom4j.DocumentException;
import sun.security.pkcs11.wrapper.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:18
 * @desc:
 */
public class WebSiteConfigFactory {
    @Getter
    private List<WebSiteConfig> webSiteConfigs;

    private XmlConfigLoader xmlConfigLoader;

    public WebSiteConfigFactory() {
        this.webSiteConfigs = new ArrayList<WebSiteConfig>();
    }

    public void load() {
//        this.loadTest();
        this.loadXml();
    }

    private void loadXml() {
        try {
            this.xmlConfigLoader = new XmlConfigLoader(PropertiesUtils.get("website.config"));
            this.webSiteConfigs = this.xmlConfigLoader.load();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTest() {
        WebSiteConfig config = new WebSiteConfig();
        config.setUrl("http://www.freebuf.com/sectool");
        config.setItemCss(".news-info");
        config.setGroup("freebuf");

        ItemConfig itemConfig = new ItemConfig();
        //#timeline > div:nth-child(1) > div.news-info > dl > dt > a
        itemConfig.setTitle("dl > dt > a");
        itemConfig.setUrl("dl > dt > a");
        itemConfig.setDesc(".text");
        config.setItemConfig(itemConfig);

        Set<String> emails = new HashSet<String>();
        emails.add("zhangyw001@gmail.com");
        emails.add("zhangyw_001@126.com");
        config.setEmails(emails);
        this.webSiteConfigs.add(config);
    }
}
