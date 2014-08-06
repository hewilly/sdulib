package com.why.message.response;

/**
 * 文本消息
 */
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toString() {
		return "TextMessage [Content=" + Content + ", getToUserName()="
				+ getToUserName() + ", getFromUserName()=" + getFromUserName()
				+ ", getCreateTime()=" + getCreateTime() + ", getMsgType()="
				+ getMsgType() + ", getFuncFlag()=" + getFuncFlag() + "]";
	}

	
	
	
}
