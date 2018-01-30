package com.zhangyingwei.spiders.konwledge.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午10:07
 * @desc:
 */
public class XmlConfigLoader {
    private final Document doc;
    private String path;
    private SAXReader reader;

    public XmlConfigLoader(String path) throws DocumentException {
        this.path = path;
        this.reader = new SAXReader();
        this.doc = this.reader.read(new File(path));
    }

    public List<WebSiteConfig> load() {
        List<WebSiteConfig> configs = new ArrayList<WebSiteConfig>();
        Element root = this.doc.getRootElement();
        List<Element> websites = root.elements("website");
        for (Element website : websites) {
            WebSiteConfig config = new WebSiteConfig();
            config.setGroup(website.attributeValue("group"));
            config.setLimit(Integer.parseInt(website.attributeValue("limit")));

            String prefix = website.element("url").attributeValue("prefix");
            config.setUrl(Optional.ofNullable(prefix).orElse("") + website.elementText("url"));

            Element item = website.element("item");
            config.setItemCss(item.attributeValue("css"));

            ItemConfig itemConfig = new ItemConfig();
            itemConfig.setUrl(item.element("url").attributeValue("css"));
            itemConfig.setTitle(item.element("title").attributeValue("css"));
            itemConfig.setDesc(item.element("desc").attributeValue("css"));

            Element emails = website.element("emails");
            Set<String> email = new HashSet<String>();

            List<Element> emailList = emails.elements("email");
            for (Element element : emailList) {
                email.add(element.getText());
            }

            config.setEmails(email);
            config.setItemConfig(itemConfig);
            configs.add(config);

            System.out.println("--| " + config.getGroup());
        }
        return configs;
    }
}
