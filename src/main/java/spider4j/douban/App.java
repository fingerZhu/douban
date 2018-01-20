package spider4j.douban;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * @author finger_zhu
 */
public class App {

	public static void main(String[] args) {

		final String[] tags = new String[] { "互联网", "编程", "算法" };
		// vector线程安全
		final List<Book> data = new Vector<Book>();

		// 多线程爬取不同类型的书
		for (final String tag : tags) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Spider douban = new Spider();
						douban.spider(tag, data);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}, "线程-" + tag).start();
		}

		// 还有爬虫线程未结束
		while (Thread.activeCount() > 1) {
			try {
				Thread.sleep(1000);// 节省判断性能
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("爬虫线程全部结束");
		//以score评分降序
		Collections.sort(data,Collections.reverseOrder());
		//截取top100
		ExcelUtils.exportExcel(data.subList(0, 100));
	}
}
