package com.zhangyingwei.spiders.konwledge.service;

import com.zhangyingwei.spiders.konwledge.common.PropertiesUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by zhangyw on 2018/2/5.
 */
public class NoticeService {
    private String url;
    private OkHttpClient client;
    private String sendkey;

    public NoticeService() throws IOException {
        this.sendkey = PropertiesUtils.get("serverchan.sendkey");
        this.url = "https://pushbear.ftqq.com/sub";
        this.client = new OkHttpClient();
    }

    public void notice(String title,String content) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("sendkey", this.sendkey);
        builder.add("text", title);
        builder.add("desp", content);
        Request request = new Request.Builder()
                .url(this.url).post(builder.build())
                .build();
        Response response = this.client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println("推送消息成功");
        }else {
            System.out.println("推送消息失败");
        }
        System.out.println("|-- 推送结果 --| " + response.body().string());
    }
}
