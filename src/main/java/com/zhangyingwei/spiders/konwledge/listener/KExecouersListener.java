package com.zhangyingwei.spiders.konwledge.listener;

import com.zhangyingwei.cockroach.executer.listener.IExecutersListener;
import com.zhangyingwei.spiders.konwledge.cache.CacheMap;
import com.zhangyingwei.spiders.konwledge.common.DateUtils;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import com.zhangyingwei.spiders.konwledge.service.EmailService;
import com.zhangyingwei.spiders.konwledge.service.NoticeService;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhangyw on 2018/2/2.
 */
public class KExecouersListener implements IExecutersListener {

    private EmailService emailService;
    private NoticeService noticeService;

    public KExecouersListener() {
        this.emailService = new EmailService();
        try {
            this.noticeService = new NoticeService();
        } catch (IOException e) {
            System.out.println("初始化推送服务失败");
            e.printStackTrace();
        }
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
        Map<String, List<Konwledge>> htmlMap = this.bulidHtmlMap(konwledges);
        System.out.println("统计所有内容信息...");
        this.emailService.send(emailMap);
        try {
            this.emailService.toContentHtml(htmlMap);
            this.emailService.toIndexHtml();
        } catch (IOException e) {
            System.out.println("生成 html 失败");
            e.printStackTrace();
        }
        if (this.noticeService != null) {
            try {
                this.noticeService.notice(DateUtils.currentDate()+" 发送邮件结果", this.bulidEmailList(emailMap));
            } catch (IOException e) {
                System.out.println("推送失败");
                e.printStackTrace();
            }
        }
    }

    private String bulidEmailList(Map<String, List<Konwledge>> emailMap) {
        StringBuffer res = new StringBuffer();
        for (Map.Entry<String, List<Konwledge>> entity : emailMap.entrySet()) {
            res.append(String.format("* 邮箱: %s \n* 发送条数: %d \n\n ==== \n\n", entity.getKey(),entity.getValue().size()));
            res.append("\n");
        }
        return res.toString();
    }

    private Map<String, List<Konwledge>> bulidHtmlMap(List<Konwledge> konwledges) {
        Map<String, List<Konwledge>> htmlMap = new HashMap<String, List<Konwledge>>();
        List<String> gropupList = new ArrayList<String>();
        konwledges.forEach(konwledge -> {
            List<Konwledge> list = Optional.ofNullable(htmlMap.get(konwledge.getGroup())).orElse(new ArrayList<Konwledge>());
            if (!gropupList.contains(konwledge.getUrl())) {
                list.add(konwledge);
                gropupList.add(konwledge.getUrl());
            }
            htmlMap.put(konwledge.getGroup(), list);
        });
        return htmlMap;
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
