package http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchByTiming {
	public static String getInfo(String content){
		try {
			content=URLEncoder.encode(content,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "http://58.194.172.34/opac/openlink.php?strSearchType=title&match_flag=forward&historyCount=1&strText="
				+ content
				+ "&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&location=ALL";
		String result = CommonClass.doSearch(url);
		return result;
		
		
		
		
	}
}
