package http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.why.message.response.Article;
import com.why.message.response.NewsMessage;

public class News {
	public News(){
		
	}
	
	public static NewsMessage LibNews(int i) throws IOException{
		
		List<Article> articles = new ArrayList<Article>();
		
		Document doc = Jsoup.connect("http://www.lib.sdu.edu.cn/portal/tpl/home/index").timeout(0).get();
		Elements eles=doc.select(".news_con_b");
		Element elet = eles.get(i);
//		System.out.println(elet);
		Elements elements = elet.select("li");
//		System.out.println(elements);
		
		Article articlet = new Article();
		articlet.setTitle("图书馆动态：");
//		articlet.setUrl(finurl);
		articles.add(articlet);
		
		for(Element element : elements){
			String date = element.getElementsByTag("span").get(0).text();
//			System.out.println(date);
			
			String href=element.getElementsByTag("a").get(0).attr("href");
//			System.out.println(href);
			String finalhref = "http://www.lib.sdu.edu.cn/portal/tpl/home/" + href;
//			System.out.println(finalhref);
			
			String news = element.getElementsByTag("a").get(0).text();
//			System.out.println(news);
			
//			System.out.println(news+"  "+date+"  "+finalhref);
			
			Article article = new Article();
			article.setTitle(date + " " + news);
			article.setUrl(finalhref);
			articles.add(article);
		}
		String moreurl = "http://www.lib.sdu.edu.cn/portal/tpl/home/newslist?type=1";
		
		Article article = new Article();
		article.setTitle("更多...");
		article.setUrl(moreurl);
		articles.add(article);
		
		NewsMessage message = new NewsMessage();
		message.setArticleCount(articles.size());
		message.setArticles(articles);

		return message;
	}
	
	
	public static void main(String args[]){
//		System.out.println(doc);
		String str = "gg";
		News news = new News();
		try {
			news.LibNews(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
