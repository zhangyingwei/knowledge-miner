package com.zhangyingwei.spiders.konwledge.service;

import com.zhangyingwei.smail.Smail;
import com.zhangyingwei.smail.config.SmailConfig;
import com.zhangyingwei.spiders.konwledge.common.PropertiesUtils;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import com.zhangyingwei.spiders.konwledge.common.DateUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午9:05
 * @desc:
 */
public class EmailService {
    public void send(Map<String, List<Konwledge>> emailMap) {
        emailMap.entrySet().forEach(entity -> {
            String email = entity.getKey();
            try {
                String content = this.bulidContent(entity.getValue());
                TimeUnit.SECONDS.sleep(10);
                new Smail(new SmailConfig().setStarttls(true)).auth(PropertiesUtils.get("email.username"),PropertiesUtils.get("email.password")).to(email).send("起来觅食了", content);
                System.out.println("发邮件成功 "+email);
                System.out.println("-------------------");
            } catch (Exception e) {
                System.out.println("邮件发送失败 " + email);
                e.printStackTrace();
            }
        });
    }

    private String bulidContent(List<Konwledge> value) throws IOException {
        Map<String, List<Konwledge>> groupKon = new HashMap<String,List<Konwledge>>();
        value.forEach(konwledge -> {
            List<Konwledge> tmpKonws = Optional.ofNullable(groupKon.get(konwledge.getGroup())).orElse(new ArrayList<Konwledge>());
            tmpKonws.add(konwledge);
            groupKon.put(konwledge.getGroup(), tmpKonws);
        });

        List<Map.Entry<String, List<Konwledge>>> enres = groupKon.entrySet().stream().collect(Collectors.toList());
        enres.sort((a, b) -> {
            return a.getKey().split("\\.")[0].compareTo(b.getKey().split("\\.")[0]);
        });
        Map<String, List<Konwledge>> result = new TreeMap<String, List<Konwledge>>();
        for (Map.Entry<String, List<Konwledge>> enre : enres) {
            result.put(enre.getKey(),enre.getValue());
        }
        return this.bulidContentByBeetl(result);
    }

    private String bulidContentByBeetl(Map<String, List<Konwledge>> groupKon) throws IOException {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate(this.readTemplate());
        t.binding("datetime", DateUtils.currentDate());
        t.binding("articles",groupKon);
        return t.render();
    }

    private String readTemplate() throws IOException {
        File file = new File(PropertiesUtils.get("email.template"));
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        in.close();
        return new String(bytes);
    }
}
