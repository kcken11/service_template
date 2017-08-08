package com.melot.talkee.driver.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Pager<T> {
	// 当前页码
	private int currentPage = 1;
	// 每页显示的条数
	private int pageSize = 10;
	// 最大条数
	private int totalCount = 0;
	// 最大页数
	private int totalPage = 1;
	// 最大页数
	private int start = 0;

	private Map<String, Object> params; // 查询条件

	private Map<String, List<Object>> paramLists; // 数组查询条件

	private int first = 0;

	private List<T> pageItems;

	private String asc;
	
	private String desc;
	
	/**
	 * @return the asc
	 */
	public String getAsc() {
		return asc;
	}

	/**
	 * @param asc the asc to set
	 */
	public void setAsc(String asc) {
		this.asc = asc;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	private Order order = Order.desc;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, List<Object>> getParamLists() {
		return paramLists;
	}

	public void setParamLists(Map<String, List<Object>> paramLists) {
		this.paramLists = paramLists;
	}

	public Pager() {
		this.pageSize = 10;
	}

	public Pager(int pageSize) {
		this.pageSize = pageSize;
	}
	
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 获取总页码
	 * 
	 * @return
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置总页数
	 * 
	 * @param totalPages
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage == 0 ? 1 : totalPage;
	}

	/**
	 * 获取每页显示的条数
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置显示的页码 注意是页码
	 * 
	 * @param pageNumber
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
		this.setTotalPage(totalPages);
		if (currentPage >= totalPages) {
			setCurrentPage(totalPages);
			setFirst((totalPages - 1) * pageSize);
		} else if (currentPage <= 0) {
			setCurrentPage(1);
			setFirst(first);
		} else {
			setFirst((currentPage - 1) * pageSize);
		}
	}

	/**
	 * 返回翻页开始位置
	 * 
	 * @param MaxCount
	 * @return
	 */
	public void setFirst(int first) {
		this.first = first;
	}

	public int getFirst() {
		return first;
	}

	/**
	 * 设置每页显示数据的条数
	 * 
	 * @param showScalar
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取当前显示的页码
	 * 
	 * @param tableName
	 */
	public int getCurrentPage() {
		return currentPage <= 0 ? 1 : currentPage;
	}

	/**
	 * 获取数据总条数
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public List<T> getPageItems() {
		return pageItems;
	}

	public void setPageItems(List<T> pageItems) {
		this.pageItems = pageItems;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setOrder(String order) {
		if (StringUtils.equals("desc", order) || StringUtils.equals("asc", order)) {
			this.order = Order.valueOf(Order.class, order);
			if (StringUtils.equals("asc", order)) {
				setAsc("asc");
			}else{
				setDesc("desc");
			}
		}
	}

	public static enum Order {
		desc, asc
	}

	@Override
	public String toString() {
		return "{currentPage:" + currentPage + ", order:\"" + order
				+ "\", pageSize:" + pageSize
				+ ", totalCount:" + totalCount + ", totalPages:" + totalPage
				+ "}";
	}
}