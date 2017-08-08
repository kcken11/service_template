package com.melot.talkee.driver.service;

import com.melot.talkee.driver.domain.BuyPeriods;

/**
 * Created by mn on 2017/5/22.
 */
public interface TalkTradeOrderService {

	/**
	 * 添加订单
	 * @param order
	 * @return tagcode
	 */
    public String addTradeOrder(BuyPeriods order);


    /**
     * 确认订单状态
     * @param orderId
     * @param status
     * @return tagcode
     */
    public String confirmTradeOrder(String orderId,int status,int freetimes);



}
