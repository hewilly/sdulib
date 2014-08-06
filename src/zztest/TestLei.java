package zztest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestLei {
	
	
	public static void main(String args[]) throws UnsupportedEncodingException{
	
		
		String url="http://opac.lib.sdu.edu.cn/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText=java&doctype=ALL&displaypg=10&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
		
		try {
			Document document = Jsoup.connect(url).timeout(0).get();
//			System.out.println(document);
			Element element = document.getElementById("search_book_list");
//			System.out.println(element);
			Elements elets = element.select("li.book_list_info");
//			System.out.println(elets);
			
			int elesize = elets.size();
			System.out.println(elesize);
			
			for(int i=0;i<8;i++){
				Element ele = elets.get(i);
				System.out.println(ele);
			}
			
			
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
