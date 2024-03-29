package zztest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public static String doSearch(String url){
		List<Book> books = new ArrayList<Book>();
		try {
			Document document = Jsoup.connect(url).timeout(0).get();
//			System.out.println(document);
			Element element = document.getElementById("search_book_list");
//			System.out.println(element);
			Elements eleBooks = element.children();
//			System.out.println(eleBooks);
			for (Element eleBook : eleBooks){
				Book book = new Book();
				
				String address="";
				StringBuilder sb=new StringBuilder();
				
				List<String> addrs=new ArrayList<String>();
				//第二层搜索
				String href=eleBook.getElementsByTag("a").get(0).attr("href");
//				System.out.println(href);
				String link="http://58.194.172.34/opac/"+href;
				
				//得到每本书的详细信息
//				Document bookInfoDoc=Jsoup.connect(link).get();
				
				String bookName = eleBook.getElementsByTag("a").get(0).text();
//				System.out.println(bookName);
				
				//馆藏副本、可借副本
				String availableCount = eleBook.getElementsByTag("span").get(1)
						.text();
//				System.out.println(availableCount);
				//出版社
				String raw = eleBook.getElementsByTag("p").get(0).toString();
//				System.out.println(raw);
				String s1 = raw.split("<br />")[2];
//				System.out.println(s1);
				String publisher = s1.split("&nbsp;")[0];
//				System.out.println(publisher);
				String year = s1.split("&nbsp;")[1];
//				System.out.println(year);
				
				book.setBookName(bookName);
				book.setAvailableCount(availableCount);
				book.setPublisher(publisher);
				book.setYear(year);

				books.add(book);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "图书馆未收录!";
		}
		
		StringBuilder sb = new StringBuilder();
		
		// 判断得到书的数目
				if (books.size() == 0) {
					return "图书馆未收录";
				} else if (books.size() < 10) {//书的数目小于10本
					for (Book book : books) {
						sb.append(book.getBookName()).append(" ")
								.append(book.getPublisher()).append(" ")
								.append(book.getYear()).append("\n")
								.append(book.getAvailableCount()).append("\n\n");
					}

				} else {//书的数目大于10本

					for (int i = 0; i < 10; i++) {
						sb.append(books.get(i).getBookName()).append(" ")
								.append(books.get(i).getPublisher()).append(" ")
								.append(books.get(i).getYear()).append("\n")
								.append(books.get(i).getAvailableCount()).append("\n")
								.append(books.get(i).getAddress())
								.append("\n\n");
					}
				}
	 return sb.toString();
	}
}
