package com.zhangyingwei.spiders.konwledge.service;

import com.zhangyingwei.smail.ISmail;
import com.zhangyingwei.smail.Smail;
import com.zhangyingwei.smail.config.SmailConfig;
import com.zhangyingwei.smail.exception.SmailException;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
            String content = this.bulidContent(entity.getValue());
            try {
                TimeUnit.SECONDS.sleep(10);
                new Smail(new SmailConfig().setStarttls(true)).auth("zhangyw_001@163.com","").to(email).send("起来觅食了", content);
                System.out.println("发邮件成功 "+email);
                System.out.println("-------------------");
            } catch (Exception e) {
                System.out.println("邮件发送失败 " + email);
                e.printStackTrace();
            }
        });
    }

    private String bulidContent(List<Konwledge> value) {
        Map<String, List<Konwledge>> groupKon = new HashMap<String,List<Konwledge>>();
        value.forEach(konwledge -> {
            List<Konwledge> tmpKonws = Optional.ofNullable(groupKon.get(konwledge.getGroup())).orElse(new ArrayList<Konwledge>());
            tmpKonws.add(konwledge);
            groupKon.put(konwledge.getGroup(), tmpKonws);
        });

        String content = "";
        Set<Map.Entry<String, List<Konwledge>>> entitys = groupKon.entrySet();
        for (Map.Entry<String, List<Konwledge>> entity : entitys) {
            content +="<div style='background:#4285f4; margin-bottom: 20px;'>";
            content += "<div style='color:#f4f3g4;background:#fbbc05;padding:2px;font-size:18px;text-align:center;border-radius:0 0 20px 20px;'>"+entity.getKey()+"</div>";
            for (Konwledge konwledge : entity.getValue()) {
                content += konwledge.content().concat("\n");
            }
            content +="</div>".concat("\n");
        }
        return content;
    }
}
