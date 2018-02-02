package com.zhangyingwei.spiders.konwledge.listener;

import com.zhangyingwei.cockroach.executer.listener.IExecutersListener;
import com.zhangyingwei.spiders.konwledge.cache.CacheMap;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import com.zhangyingwei.spiders.konwledge.service.EmailService;

import java.util.*;

/**
 * Created by zhangyw on 2018/2/2.
 */
public class KExecouersListener implements IExecutersListener {

    private EmailService emailService;

    public KExecouersListener() {
        this.emailService = new EmailService();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        System.out.println("任务已经全部完毕，开始整理整理发送邮件了");
        Map<String, List<Konwledge>> cache = CacheMap.getIns().getCache();
        collectKonwledges(cache);
    }

    private void collectKonwledges(Map<String, List<Konwledge>> cache) {
        List<Konwledge> konwledges = new ArrayList<Konwledge>();
        cache.values().forEach(kns -> {
            konwledges.addAll(kns);
        });

        Map<String, List<Konwledge>> emailMap = this.bulidEmailMap(konwledges);
        System.out.println("统计所有内容信息...");
        this.emailService.send(emailMap);
    }

    private Map<String, List<Konwledge>> bulidEmailMap(List<Konwledge> konwledges) {
        Map<String, List<Konwledge>> emailMap = new HashMap<String, List<Konwledge>>();
        konwledges.forEach(konwledge -> {
            konwledge.getEmails().forEach(email -> {
                List<Konwledge> list = Optional.ofNullable(emailMap.get(email)).orElse(new ArrayList<Konwledge>());
                list.add(konwledge);
                emailMap.put(email, list);
            });
        });
        return emailMap;
    }
}
