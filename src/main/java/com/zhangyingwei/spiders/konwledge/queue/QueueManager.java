package com.zhangyingwei.spiders.konwledge.queue;

import com.zhangyingwei.cockroach.executer.task.Task;
import com.zhangyingwei.cockroach.queue.CockroachQueue;
import com.zhangyingwei.cockroach.queue.TaskQueue;
import com.zhangyingwei.spiders.konwledge.cache.CacheMap;
import com.zhangyingwei.spiders.konwledge.config.WebSiteConfig;
import com.zhangyingwei.spiders.konwledge.config.WebSiteConfigFactory;
import com.zhangyingwei.spiders.konwledge.model.Konwledge;
import com.zhangyingwei.spiders.konwledge.service.EmailService;

import java.io.IOException;
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

    public QueueManager() throws IOException {
        this.queue = TaskQueue.of();
        this.configFactory = new WebSiteConfigFactory();
        this.configFactory.load();
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
        return this.queue;
    }
}
