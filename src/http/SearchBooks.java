package http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import model.Book;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.why.message.response.Article;
import com.why.message.response.NewsMessage;

public class SearchBooks {

	public static final String TYPE_TITLE = "title";
	public static final String TYPE_AUTHER = "author";
	public static final String TYPE_PUBLISHER = "publisher";
	
	static String href1="http://opac.lib.sdu.edu.cn/opac/openlink.php?strSearchType=";
	static String href2="&match_flag=forward&historyCount=1&strText=";
	static String href3="&doctype=ALL&displaypg=10&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
	
	private static final String searchUrl = "http://opac.lib.sdu.edu.cn/opac/openlink.php?match_flag=forward&historyCount=1&doctype=ALL&displaypg=10&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
	private static Document getDoc(String url, String type, String keyWord) {
		try {
			return Jsoup.connect(url).data("strSearchType", type)
					.data("strText", keyWord).timeout(5000).get();
		} catch (IOException e) {
			return null;
		}
	}
	
	public static NewsMessage getInfo(String content, String type) throws UnsupportedEncodingException {
		
		String strcon=URLEncoder.encode(content,"utf-8");
		String finurl=href1+type+href2+strcon+href3;
		
		List<Article> articles = new ArrayList<Article>();
		
		Document document = getDoc(searchUrl, type, content);
//		System.out.println(document);
		if (document == null) {
			return null;
		}
		
		Elements eleBooks = document.select("li.book_list_info");
//		System.out.println(eleBooks);
		
		int bookNum=eleBooks.size();
		
		if (bookNum==0) {
			// return null;
			System.out.println("没有相关书籍");
		}
		Article articlet = new Article();
		articlet.setTitle("书籍查询结果：");
//		articlet.setUrl(finurl);
		articles.add(articlet);
		
		int size=bookNum>8?8:bookNum;//这个要加一个判断，是否超过8本书
		for(int i=0;i<size;i++){
			Element elet = eleBooks.get(i);
			
			Element tagA = elet.getElementsByTag("a").get(0);
			
			// 得到书的链接地址
			String link = "http://58.194.172.34/opac/" + tagA.attr("href");
			// 得到书名
			String title = tagA.text();
			
			String author = elet.getElementsByTag("p").toString();
			author = author.split("</span> | <br />")[2];
			
			Article article = new Article();
			article.setTitle(title + "/" + author);
			article.setUrl(link);
			articles.add(article);
			
		}
		
//		for (Element eleBook : eleBooks) {
//
//			Element tagA = eleBook.getElementsByTag("a").get(0);
//
//			// 得到书的链接地址
//			String link = "http://58.194.172.34/opac/" + tagA.attr("href");
//			// 得到书名
//			String title = tagA.text();
//
//			String author = eleBook.getElementsByTag("p").toString();
//			author = author.split("</span> | <br />")[2];
//
//			Article article = new Article();
//			article.setTitle(title + "/" + author);
//			article.setUrl(link);
//			articles.add(article);
//		}
		
		Article article = new Article();
		article.setTitle("更多...");
		article.setUrl(finurl);
		articles.add(article);
		
		NewsMessage message = new NewsMessage();
		message.setArticleCount(articles.size());
		message.setArticles(articles);

		return message;
	}

	public static void main(String[] args) {
		long l1 = System.currentTimeMillis();
//		String sss = Test.getInfo("java").toString();
//		System.out.println(sss);
		long l2 = System.currentTimeMillis();
//		System.out.println("time=" + (l2 - l1));	
		
	}
}










