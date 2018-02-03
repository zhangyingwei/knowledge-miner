# knowledge-thieves

[![](https://img.shields.io/badge/language-java-orange.svg)]()
[![](https://img.shields.io/badge/jdk-1.8-green.svg)]()
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

抓取各个网站的内容整理之后并发送邮件，可以通过 crontan 定时调度。没啥好说的。

## 配置说明

### config.properties

```properties
email.username=发送邮件的邮箱
email.password=发送邮箱的密码
email.template=邮件内容模板文件地址
website.config=抓取网站配置文件地址
```

### email.template
邮件内容模板文件。

### website.xml
抓取内容配置以及邮件配置

![knowledge.jpg](doc/img/knowledge.jpg)