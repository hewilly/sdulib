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
 * ���ķ�����
 */
public class CoreService {
	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			
			// Ĭ�Ϸ��ص��ı���Ϣ����
			String respContent = "ɽ����ѧͼ��ݸ�л���Ĺ�ע!\n���� \"help\"�˽����ָ��";
			// xml�������
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// ���ͷ��ʺţ�open_id��
			String fromUserName = requestMap.get("FromUserName");
			// �����ʺ�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");
			// ��Ϣ����
			String content = requestMap.get("Content");
			// �ظ��ı���Ϣ
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			
			//����¼�
			if (msgType.equals("event")){
				
			}
			/*
			 * һ������ xj#ѧ�ţ����� �� xj#219651,252519 ��ǰ���� dq#ѧ�ţ����� �� jy#219651,252519
			 * �鼮��ѯ ����������cst#���� ��cst#java �������ߣ� csa#���� ��csa#���� ���������磩 csp#������
			 * ��csp#��е��ҵ������
			 */
			// �ı���Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				
				if(content.equalsIgnoreCase("help")){
					respContent = "������ָ���ʽ��\nһ�����裺xj#�û���������\n��ǰ���ģ�jy#�û���������\n"+
				"�鼮��ѯ��\n\tcst#��������������\n\tcsa#���ߣ�����������\n\tcsp#�����磨������������\nͼ��ݶ�̬��ѯ��\n\t"+
				"������Ϣ��dt#gg\n\t��Դ��̬��dt#zy\n\t������Ϣ��dt#jz\n��ʹ�ù����г������������뷢���ʼ���linweize0325@163.com";
				}
				else{
				

				String[] cmds = content.split("#", 2);
				if (cmds.length != 2) {
					return cmdFormatError(textMessage);
				}
				String cmd = cmds[0].toLowerCase();//#֮ǰ��
				String param = cmds[1];//#֮���
				
				if(cmd.equals("dt")){
					//parmsΪ��gg���棩����zy��Դ������jz������
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
				// ��������ı���"xj"��ͷ,��ʾ�����衯���� xj#219814,123456
				else if (cmd.equals("xj")) {
					String[] params=param.split("[,��]");
					if (params.length!=2) {
						return cmdFormatError(textMessage);
					}
					String sid = params[0];
					String pwd = params[1];
					try {
						respContent = new ReBorrow(sid, pwd).renewAll();
					} catch (Exception e) {
						respContent = "����ʧ��,���������ʽ���������ӣ���������!";
					}
				}
				// ��������ı���"jy#"��ͷ����ʾ���鿴��ǰ���ġ�����jy#219651��252519
				else if (cmd.equals("jy")) {
					String[] params=param.split("[,��]");
					//��ʽ����
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
						respContent = "����ʧ��,���������ʽ���������ӣ���������!";
					}
				} else if (cmd.startsWith("cs")) {

					NewsMessage newsMessage;
					// ����������
					if (cmd.equals("cst")||cmd.equals("cs")) {
						newsMessage = SearchBooks.getInfo(param,
								SearchBooks.TYPE_TITLE);
					}// �����߲���
					else if (cmd.equals("csa")) {
						newsMessage = SearchBooks.getInfo(param,
								SearchBooks.TYPE_AUTHER);
					}// �����������
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
				// respContent = "�����͵����ı���Ϣ��";
			}}
			// ͼƬ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "�����͵���ͼƬ��Ϣ��";
			}
			// ����λ����Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "�����͵��ǵ���λ����Ϣ��";
			}
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "�����͵���������Ϣ��";
			}
			// ��Ƶ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "�����͵�����Ƶ��Ϣ��";
			}
			// �¼�����
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// �¼�����
				String eventType = requestMap.get("Event");
				// ����
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "лл���Ĺ�ע��";
				}
				// ȡ������
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ
				}
				// �Զ���˵�����¼�
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// TODO �Զ���˵�Ȩû�п��ţ��ݲ����������Ϣ
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
		textMessage.setContent("ָ���ʽ����");
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);
		return MessageUtil.textMessageToXml(textMessage);
	}

}
