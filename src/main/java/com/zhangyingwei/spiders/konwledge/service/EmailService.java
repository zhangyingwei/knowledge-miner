package com.zhangyingwei.spiders.konwledge.service;

import com.zhangyingwei.cockroach.utils.FileUtils;
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
    private File file;
    private File indexFile;
    private String htmlPath;
    public EmailService() {
        try {
            this.htmlPath = PropertiesUtils.get("html.path");
            this.file = FileUtils.openOrCreate(htmlPath, DateUtils.currentFileName());
            this.indexFile = FileUtils.openOrCreate(htmlPath, "index.html");
        } catch (IOException e) {
            System.out.println("创建文件失败");
            e.printStackTrace();
        }
    }

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
        Template t = gt.getTemplate(this.readTemplate("email.template"));
        t.binding("datetime", DateUtils.currentDate());
        t.binding("datahtml", DateUtils.currentFileName());
        t.binding("articles",groupKon);
        System.out.println("-----------------------------------|");
        groupKon.entrySet().forEach(eneity -> {
            System.out.println("-- html --| "+eneity.getKey());
        });
        System.out.println("-----------------------------------|");
        return t.render();
    }

    private String readTemplate(String key) throws IOException {
        File file = new File(PropertiesUtils.get(key));
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        in.close();
        return new String(bytes);
    }

    public void toContentHtml(Map<String, List<Konwledge>> htmlMap) throws IOException {
        FileUtils.write(this.bulidContentByBeetl(htmlMap).getBytes(),this.file);
        System.out.println("html 保存成功");
    }

    public void toIndexHtml() throws IOException {
        FileUtils.write(this.bulidIndexByBeetl().getBytes(),this.indexFile);
        System.out.println("index.html 保存成功");
    }

    private String bulidIndexByBeetl() throws IOException {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate(this.readTemplate("index.template"));
        t.binding("filelist", this.getHtmlList());
        return t.render();
    }

    private List<String> getHtmlList() {
        File baseFile = new File(this.htmlPath);
        List<String> result = new ArrayList<String>();
        if (baseFile.exists() && baseFile.isDirectory()) {
            String[] fileNames = baseFile.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("html") && !name.equals("index.html");
                }
            });
            result.addAll(Arrays.asList(fileNames));
        }
        return result;
    }
}
