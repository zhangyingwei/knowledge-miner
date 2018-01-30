package com.zhangyingwei.spiders.konwledge.service;

import com.zhangyingwei.smail.ISmail;
import com.zhangyingwei.smail.Smail;
import com.zhangyingwei.smail.config.SmailConfig;
import com.zhangyingwei.smail.exception.SmailException;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午9:05
 * @desc:
 */
public class EmailService {

    private ISmail smail;

    public EmailService() {
        this.smail = new Smail(new SmailConfig().setStarttls(true))
                .auth("zhangyw_001@163.com","");
    }

    public void send(Map<String, List<Konwledge>> emailMap) {
        emailMap.entrySet().forEach(entity -> {
            String email = entity.getKey();
            String content = this.bulidContent(entity.getValue());
            try {
                TimeUnit.SECONDS.sleep(10);
                this.smail.to(email).send("起来觅食了", content);
                System.out.println("发邮件成功 "+email);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (SmailException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private String bulidContent(List<Konwledge> value) {
        String content = "";
        for (Konwledge konwledge : value) {
            content += konwledge.content().concat("\n");
        }
        return content;
    }
}
