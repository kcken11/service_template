package com.melot.talkee.driver.domain;

import java.io.Serializable;

/**
 * Title: RespMsg
 * Description: 通用返回消息
 * @author  董毅<a href="mailto:yi.dong@melot.cn">
 * @version V1.0
 * @since 2016-1-3 15:02:53 
 */
public class RespMsg implements Serializable{

	private static final long serialVersionUID = 1489136599446L;

	/**
     * 返回tagCode
     */
    private String tagCode;
    
    /**
     * 返回的信息描述
     */
    private String respMsg;
	

	/**
	 * @return the tagCode
	 */
	public String getTagCode() {
		return tagCode;
	}

	/**
	 * @param tagCode the tagCode to set
	 */
	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}

	/**
	 * @return the respMsg
	 */
	public String getRespMsg() {
		return respMsg;
	}

	/**
	 * @param respMsg the respMsg to set
	 */
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
    
}
