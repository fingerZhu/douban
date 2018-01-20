package spider4j.douban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	
	public void spider(String tag,List<Book> data) throws InterruptedException, ParseException {
		// 因为是以评论量排序(type=S),统计"评论人数"连续小于500的次数,如果大于15次 ,则再次出现大于1000的可能性很小,直接退出以免浪费
		int cacheCount = 0;
		p: for (int page = 1;; page++) {
			// 根据评论查询
			String html = getHtml("https://book.douban.com/tag/" + tag + "?start=" + (page - 1) * 20 + "&type=S", "UTF-8");
			if (html != null && !"".equals(html)) {
				// 当前页图书数据的 li标签 集合
				Elements lis = Jsoup.parse(html).selectFirst("div#subject_list ul.subject-list").select("li.subject-item");
				// 查询结束
				if (lis.isEmpty()) {
					break;
				} else {
					for (Element li : lis) {
						Book book = getBook(li, tag);
						Integer people = book.getPeople();
						if (people >= 1000) {
							data.add(book);
							cacheCount = 0;
						} else if (people <= 500) {
							// 连续15次以上评论数小于500,说明后面的评论数不会再出现大于1000的,没必要继续爬取
							if (cacheCount++ > 15) {
								break p;// 跳出最外层循环p
							}
						}
					}
				}
			}
			//每页休眠500毫秒
			Thread.sleep(500);
			System.out.println(Thread.currentThread().getName()+"准备爬取第 "+page+"页 ");
		}
	}

	Pattern ptnPeople = Pattern.compile("[^0-9]");// "评论人数"正则
	Pattern ptnBookId = Pattern.compile("subject_id:'(\\d+)");// 图书id正则
	Pattern ptnPrice = Pattern.compile("[^0-9.]");// "价格"正则

	/**
	 * 抽取图书数据
	 */
	public Book getBook(Element li, String tag) throws ParseException {
		Element info = li.selectFirst("div.info");
		Matcher ma = ptnBookId.matcher(info.selectFirst("h2 a").attr("onclick"));
		String bookId = "";
		if (ma.find()) {
			bookId = ma.group(1);
		}
		Book book = new Book();
		book.setBookId(bookId);// 书id
		book.setTag(tag);
		book.setCoverUrl(li.selectFirst("div.pic img").attr("src"));// 封面地址
		book.setTitle(info.selectFirst("h2 a").attr("title"));// 书名
		book.setScore(Double.parseDouble(info.selectFirst("div.star span.rating_nums").text()));// 评分
		book.setPeople(Integer.parseInt(ptnPeople.matcher(info.selectFirst("div.star span.pl").text()).replaceAll("").trim()));// 评价人数
		/**
		 * 出版信息 这里数据有很多奇葩情况
		 * 1.多个作者 也是/分割
		 * 2.没有出版社
		 * 3.没有出版日期
		 * 4.没有价格
		 * 想不出什么好的解决办法  所以如果异常就赋值给一个字段
		 */
		String[] pub = info.selectFirst("div.pub").text().split("/");// [作者/出版社/出版日期/价格]
		try{
			book.setPrice(Double.parseDouble(ptnPrice.matcher(pub[pub.length-1]).replaceAll("").trim()));// 价格
			book.setPubDate(pub[pub.length-2]);// 出版日期
			book.setPubCompany(pub[pub.length-3]);// 出版社
			String foo = "";
			for(int i=0;i<pub.length-3;i++){
				foo += "/"+pub[i];
			}
			book.setAuthor(foo.substring(1));// 作者
		}catch(Exception e){
			book.setAuthor(pub.toString());//为了不出现异常
			book.setPubCompany(null);
			book.setPubDate(null);
			book.setPrice(null);
		}
		return book;
	}

	/**
	 * 获取url返回的html文本
	 */
	public String getHtml(String url, String encoding) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			// 建立连接,打开网络
			URLConnection connect = new URL(url).openConnection();
			// 跳过第一层的反爬校验
			connect.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			// IO
			isr = new InputStreamReader(connect.getInputStream(), encoding);
			// 高效率读取 缓冲
			br = new BufferedReader(isr);

			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp + "\n");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
