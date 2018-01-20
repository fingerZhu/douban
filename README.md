# douban
## 简单的豆瓣图书top100数据爬取,爱立信面试题

**项目结构:**

App.java          程序入口

Book.java         自定义model

ExcelUtils.java   excel相关代码

Spider            爬虫代码



**1.网页结构分析**

"算法"书籍根据评论量排序的url   start为起始索引(page-1)*20    type为排序方式,S为按评论排序

https://book.douban.com/tag/算法?start=0&type=S


**2.返回数据**

返回数据有三种方式:html文本,ajax动态加载,数据混淆
幸运的是豆瓣读书使用的是html文本,难度较低,使用第三方库JSOUP操作更方便


**3.多线程抓取数据并同步存储**

根据不同标签,开启线程,互不干扰,每爬取一页休眠0.5秒,对服务器比较友好,并使用new Vector()自带线程同步的集合存储数据


**4.导出到excel**

使用poi包和IO流,注入top100数据并导出到桌面



**PS:**

**1.评论数1000**

需求说明书籍要过滤掉评论数小于1000的数据,虽然查询方式是按评论排序,但我发现排序不是绝对准确,所以加了一层自己的过滤(cacheCount).
如果书籍的评论数小于 1000/2=500,那么cacheCount加1.
如果出现连续15本数据都小于500,则后面书籍的评论数出现大于1000的微乎其微,所以该线程爬取结束.

**2.List<Book>排序**

在Book上实现一个Comparable的接口,并填入排序字段(score)

然后调用Collections.sort(List<Book>,Collections.reverseOrder());


