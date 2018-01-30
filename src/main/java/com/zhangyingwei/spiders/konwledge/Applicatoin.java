package com.zhangyingwei.spiders.konwledge;

import com.zhangyingwei.cockroach.CockroachApplication;
import com.zhangyingwei.cockroach.annotation.*;
import com.zhangyingwei.cockroach.queue.CockroachQueue;
import com.zhangyingwei.spiders.konwledge.queue.QueueManager;
import com.zhangyingwei.spiders.konwledge.store.KonwledgeStore;

/**
 * @author: zhangyw
 * @date: 2018/1/30
 * @time: 下午8:03
 * @desc:
 */
@EnableAutoConfiguration
@AppName("知识小偷")
@ThreadConfig(num = 1,sleep = 1000)
@Store(KonwledgeStore.class)
@AutoClose(true)
public class Applicatoin {
    public static void main(String[] args) throws Exception {
        CockroachQueue queue = new QueueManager().bulid();
        CockroachApplication.run(Applicatoin.class, queue);
    }
}
