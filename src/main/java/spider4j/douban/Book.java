package spider4j.douban;

public class Book implements Comparable<Book>{
	// 书id
	public String bookId;
	// 封面地址
	public String coverUrl;
	// 书名
	public String title;
	// 评分
	public Double score;
	// 评论数
	public Integer people;
	// 作者
	public String author;
	// 出版社
	public String pubCompany;
	// 出版日期
	public String pubDate;
	// 价格
	public Double price;
	// 标签
	public String tag;

	//用来排序
	public int compareTo(Book o) {
		return this.getScore().compareTo(o.getScore());
	}
	
	
	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubCompany() {
		return pubCompany;
	}

	public void setPubCompany(String pubCompany) {
		this.pubCompany = pubCompany;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
