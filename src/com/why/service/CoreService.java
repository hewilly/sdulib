package com.why.service;

import http.CuntBow;
import http.News;
import http.ReBorrow;
import http.SearchBooks;
//import http.SearchBooks.SearchType;


import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.why.message.response.NewsMessage;
import com.why.message.response.TextMessage;
import com.why.util.MessageUtil;

/**
 * 核心服务类
 */
public class CoreService {
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			
			// 默认返回的文本消息内容
			String respContent = "山东大学图书馆感谢您的关注!\n发送 \"help\"了解相关指令";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 消息内容
			String content = requestMap.get("Content");
			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			
			//点击事件
			if (msgType.equals("event")){
				
			}
			/*
			 * 一键续借 xj#学号，密码 如 xj#219651,252519 当前借阅 dq#学号，密码 如 jy#219651,252519
			 * 书籍查询 （按题名）cst#题名 如cst#java （按作者） csa#作者 如csa#王鑫 （按出版社） csp#出版社
			 * 如csp#机械工业出版社
			 */
			// 文本消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				
				if(content.equalsIgnoreCase("help")){
					respContent = "以下是指令格式：\n一键续借：xj#用户名，密码\n当前借阅：jy#用户名，密码\n"+
				"书籍查询：\n\tcst#题名（按题名）\n\tcsa#作者（按作者名）\n\tcsp#出版社（按出版社名）\n图书馆动态查询：\n\t"+
				"公告信息：dt#gg\n\t资源动态：dt#zy\n\t讲座信息：dt#jz\n若使用过程中出现其他问题请发送邮件至linweize0325@163.com";
				}
				else{
				

				String[] cmds = content.split("#", 2);
				if (cmds.length != 2) {
					return cmdFormatError(textMessage);
				}
				String cmd = cmds[0].toLowerCase();//#之前的
				String param = cmds[1];//#之后的
				
				if(cmd.equals("dt")){
					//parms为（gg公告）、（zy资源）、（jz讲座）
					NewsMessage newsMessage;
					int i=0;
					if(param.equalsIgnoreCase("gg")){
						i=0;
						newsMessage = News.LibNews(i);
					}else if(param.equalsIgnoreCase("zy")){
						i=1;
						newsMessage = News.LibNews(i);
					}else if(param.equalsIgnoreCase("jz")){
						i=2;
						newsMessage = News.LibNews(i);
					}else{
						return cmdFormatError(textMessage);
					}
					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setFuncFlag(0);
					return MessageUtil.newsMessageToXml(newsMessage);
					
				}
				// 如果请求文本以"xj"开头,表示‘续借’，如 xj#219814,123456
				else if (cmd.equals("xj")) {
					String[] params=param.split("[,，]");
					if (params.length!=2) {
						return cmdFormatError(textMessage);
					}
					String sid = params[0];
					String pwd = params[1];
					try {
						respContent = new ReBorrow(sid, pwd).renewAll();
					} catch (Exception e) {
						respContent = "操作失败,请检查输入格式或网络连接，重新输入!";
					}
				}
				// 如果请求文本以"jy#"开头，表示‘查看当前借阅’，如jy#219651，252519
				else if (cmd.equals("jy")) {
					String[] params=param.split("[,，]");
					//格式错误
					if (params.length!=2) {
						return cmdFormatError(textMessage);
					}
					String sid = params[0];
					String pwd = params[1];
					try {
						NewsMessage newsMessage;
						newsMessage = new CuntBow(sid, pwd).look();
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						return MessageUtil.newsMessageToXml(newsMessage);
						
					} catch (Exception e) {
						respContent = "操作失败,请检查输入格式或网络连接，重新输入!";
					}
				} else if (cmd.startsWith("cs")) {

					NewsMessage newsMessage;
					// 按题名查书
					if (cmd.equals("cst")||cmd.equals("cs")) {
						newsMessage = SearchBooks.getInfo(param,
								SearchBooks.TYPE_TITLE);
					}// 按作者查书
					else if (cmd.equals("csa")) {
						newsMessage = SearchBooks.getInfo(param,
								SearchBooks.TYPE_AUTHER);
					}// 按出版社查书
					else if (cmd.equals("csp")) {
						newsMessage = SearchBooks.getInfo(param,
								SearchBooks.TYPE_PUBLISHER);
					} else {
						return cmdFormatError(textMessage);
					}

					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setFuncFlag(0);
					return MessageUtil.newsMessageToXml(newsMessage);
				}
				// respContent = "您发送的是文本消息！";
			}}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "谢谢您的关注！";
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO 自定义菜单权没有开放，暂不处理该类消息
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

	private static String cmdFormatError(TextMessage textMessage) {
		textMessage.setContent("指令格式错误");
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);
		return MessageUtil.textMessageToXml(textMessage);
	}

}
