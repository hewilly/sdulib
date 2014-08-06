package http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;

public class SearchByPublisher {
	public static String getInfo(String content){
		try {
			content=URLEncoder.encode(content,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "http://opac.lib.sdu.edu.cn/opac/openlink.php?strSearchType=publisher&match_flag=forward&historyCount=1&strText="
				+ content
				+ "&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
		String result = CommonClass.doSearch(url);
		
//		Jsoup.connect(url).data("strText", content);
		return result;
	}
}
