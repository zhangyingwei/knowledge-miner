package com.zhangyingwei.spiders.konwledge.queue;

import com.zhangyingwei.cockroach.executer.task.Task;
import com.zhangyingwei.cockroach.queue.CockroachQueue;
import com.zhangyingwei.cockroach.queue.TaskQueue;
import com.zhangyingwei.spiders.konwledge.cache.CacheMap;
import com.zhangyingwei.spiders.konwledge.config.WebSiteConfig;
import com.zhangyingwei.spiders.konwledge.config.WebSiteConfigFactory;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import com.zhangyingwei.spiders.konwledge.service.EmailService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:05
 * @desc:
 */
public class QueueManager {
    private CockroachQueue queue = null;
    private WebSiteConfigFactory configFactory;
    private EmailService emailService;

    public QueueManager() {
        this.queue = TaskQueue.of();
        this.configFactory = new WebSiteConfigFactory();
        this.configFactory.load();
        this.emailService = new EmailService();
    }

    public CockroachQueue bulid() {
        List<WebSiteConfig> configs = this.configFactory.getWebSiteConfigs();
        configs.forEach(config -> {
            Task task = new Task(config.getUrl(),config.getGroup());
            task.setExtr(config);
            try {
                this.queue.push(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.startWatcher();
        return this.queue;
    }

    private void startWatcher() {
        System.out.println("start watcher");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(60);
                    while (!queue.isEmpty()) {
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务已经全部完毕，开始整理整理发送邮件了");
                Map<String, List<Konwledge>> cache = CacheMap.getIns().getCache();
                collectKonwledges(cache);
            }
        }).start();
    }

    private void collectKonwledges(Map<String, List<Konwledge>> cache) {
        List<Konwledge> konwledges = new ArrayList<Konwledge>();
        cache.values().forEach(kns -> {
            konwledges.addAll(kns);
        });

        Map<String, List<Konwledge>> emailMap = this.bulidEmailMap(konwledges);
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
