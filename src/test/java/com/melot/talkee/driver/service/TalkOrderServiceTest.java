package com.melot.talkee.driver.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melot.kktv.util.DateUtil;
import com.melot.module.kkrpc.starter.Main;
import com.melot.sdk.core.util.MelotBeanFactory;
import com.melot.talkee.driver.domain.OrderLesson;
import com.melot.talkee.driver.domain.Pager;
import com.melot.talkee.driver.domain.TeacherOverPeriodCount;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

public class TalkOrderServiceTest {


    private static TalkOrderService talkOrderService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Main.startClient("classpath*:conf/client-spring-config.xml");
        talkOrderService = MelotBeanFactory.getBean("talkOrderService", TalkOrderService.class);
    }

    /**
     * 获取对应时区查询每天开始
     *
     * @param dailyBeginTime
     * @param beginTime
     * @return
     */
    public static Date getDailyBeginTime(Date dailyBeginTime, Date beginTime) {
        // 开始时间大于等于开始时间，小于结束数据
        Date dailyEndTime = DateUtil.addOnField(dailyBeginTime, Calendar.DATE, 1);
        if (beginTime.getTime() >= dailyBeginTime.getTime() && beginTime.getTime() < dailyEndTime.getTime()) {
            return dailyBeginTime;
        } else {
            // 开始时间加一天
            dailyBeginTime = dailyEndTime;
            return getDailyBeginTime(dailyBeginTime, beginTime);
        }
    }

    @Test
    public void testOrderLesson() {
        List<Integer> periodList = new ArrayList<Integer>();

        periodList.add(5862);
//		periodList.add(5070);
//		periodList.add(5071);
//		periodList.add(2028);
//		periodList.add(2030);

        talkOrderService.orderLesson(620, periodList, 288);
    }



    @Test
    public void testCancleOrder() {
        talkOrderService.cancleOrder(72, 2219, "请假", 72);
    }

    @Test
    public void testModifyOverLessonStatus() {
        boolean flag = talkOrderService.modifyOverLessonStatus(330, 4958, 5);
        System.out.println("flag: " + flag);
    }

    @Test
    public void testGetPagerOrderLessonList() {

        Pager<OrderLesson> pager = talkOrderService.getPagerOrderLessonList(409, 8,null, null, 1, 0, 10, "asc");

        System.out.println(new Gson().toJson(pager));

    }
    @Test
    public void testGetAwaitOrderLessonList() {

        Pager<OrderLesson> pager = talkOrderService.getAwaitLessonList(409, null, null, 1, 0, 10, "asc");

        System.out.println(new Gson().toJson(pager));

    }

    @Test
    public void testGetOverOrderLessonList() {

        Pager<OrderLesson> pager = talkOrderService.getOverLessonList(409, null, null, 1, 0, 10, "asc");

        System.out.println(new Gson().toJson(pager));

    }

    @Test
    public void testGetLeaveOrderLessonList() {

        Pager<OrderLesson> pager = talkOrderService.getLeaveLessonList(208, null, null, 1, 0, 10, "asc");

        System.out.println(new Gson().toJson(pager));

    }


    @Test
    public void testGetOrderLessonList() {
//		Date beginTime = DateUtil.parseDateStringToDate("20170417", "yyyyMMdd");
//		Date endTime = DateUtil.parseDateStringToDate("20170423 23:59:59", "yyyyMMdd");

        Pager<OrderLesson> pager = talkOrderService.getPagerOrderLessonList(25, 7,null, null, 1, 0, 10, "asc");

        System.out.println(new Gson().toJson(pager));


//		Map<Long, Map<String, List<OrderLesson>>> dailyMap = new HashMap<Long, Map<String, List<OrderLesson>>>();
//
//		String am ="am";
//		String pm = "pm";
//		String night = "night";
//		List<OrderLesson> amList = null;
//		List<OrderLesson> pmList = null;
//		List<OrderLesson> nightList = null;
//		Map<String, List<OrderLesson>> timeMap = null;
//		if (pager.getPageItems() != null && pager.getPageItems().size() > 0) {
//			for (OrderLesson detail : pager.getPageItems()) {
//
//				long lessonDate = DateUtil.parseDateStringToLong(DateUtil.formatDate(detail.getBeginTime(), "yyyyMMdd"), "yyyyMMdd");
//
//				if (dailyMap.containsKey(lessonDate)) {
//					timeMap = dailyMap.get(lessonDate);
//					amList = timeMap.get(am);
//					pmList = timeMap.get(pm);
//					nightList = timeMap.get(night);
//				}else{
//					timeMap = new HashMap<String, List<OrderLesson>>();
//					amList = new ArrayList<OrderLesson>();
//					pmList = new ArrayList<OrderLesson>();
//					nightList = new ArrayList<OrderLesson>();
//				}
//
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(detail.getBeginTime());
//				int hour = cal.get(Calendar.HOUR_OF_DAY);
//
//				if (hour >= 0 && hour < 12 ) {
//					amList.add(detail);
//				}else if (hour >= 12 && hour < 18 ) {
//					pmList.add(detail);
//				}else if (hour >= 18 && hour < 24 ) {
//					nightList.add(detail);
//				}
//
//				timeMap.put(am, amList);
//				timeMap.put(pm, pmList);
//				timeMap.put(night, nightList);
//				dailyMap.put(lessonDate, timeMap);
//			}
//		}
//		// 进行数据补录
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(beginTime);
//		System.out.println(cal.getTimeInMillis());
//		System.out.println(endTime.getTime());
//		while (cal.getTimeInMillis() <= endTime.getTime()) {
//			long lessonDate = cal.getTimeInMillis();
//			if (!dailyMap.containsKey(lessonDate)) {
//				timeMap = new HashMap<String, List<OrderLesson>>();
//				amList = new ArrayList<OrderLesson>();
//				pmList = new ArrayList<OrderLesson>();
//				nightList = new ArrayList<OrderLesson>();
//
//				timeMap.put(am, amList);
//				timeMap.put(pm, pmList);
//				timeMap.put(night, nightList);
//				dailyMap.put(lessonDate, timeMap);
//			}
//			cal.add(Calendar.DATE, 1);
//		}
//
//	    List<Map.Entry<Long, Map<String,List<OrderLesson>>>> entryList = new ArrayList<Map.Entry<Long, Map<String,List<OrderLesson>>>>(dailyMap.entrySet());
//	    Collections.sort(entryList, new Comparator<Map.Entry<Long, Map<String,List<OrderLesson>>>>() {
//			@Override
//			public int compare(Entry<Long, Map<String,List<OrderLesson>>> o1, Entry<Long, Map<String,List<OrderLesson>>> o2) {
//				if (o2.getKey().intValue() < o1.getKey().intValue()) {
//					return 1;
//				}else if (o2.getKey().intValue()== o1.getKey().intValue()) {
//					return 0;
//				}else if (o2.getKey().intValue() > o1.getKey().intValue()) {
//					return -1;
//				}
//	            return o2.getKey().compareTo(o1.getKey());
//			}
//	    });
//
//	    Map<String,List<OrderLesson>> tempMap = null;
//
//	    LinkedList<LinkedList<List<OrderLesson>>> dailyList = new LinkedList<LinkedList<List<OrderLesson>>>();
//
//	    LinkedList<List<OrderLesson>> tempList = null;
//
//	    for (Map.Entry<Long, Map<String,List<OrderLesson>>> entry : entryList) {
//	    	tempMap = entry.getValue();
//
//	    	tempList = new LinkedList<List<OrderLesson>>();
//
//	    	amList = tempMap.get(am);
//			pmList = tempMap.get(pm);
//			nightList = tempMap.get(night);
//			tempList.add(amList);
//			tempList.add(pmList);
//			tempList.add(nightList);
//			dailyList.add(tempList);
//		}
//
//		Type type = new TypeToken<LinkedList<LinkedList<List<OrderLesson>>>>() { }.getType();
//
//		System.out.println(new Gson().toJson(dailyList,type));
    }

    @Test
    public void testGetOrderLessonByPeriodList() {
        List<OrderLesson> list = talkOrderService.getOrderLessonByPeriodList(Arrays.asList(1137, 1138));

        list.size();
    }

    @Test
    public void testGetStudentLesson() {

        Date tempBeginDate = new Date(1495987200000l);
        Date tempEndDate = new Date(1496505600000l);

        List<OrderLesson> orderLessonList = talkOrderService.getOrderLessonList(25, null, tempBeginDate, tempEndDate, null);

        Map<Long, Map<String, List<OrderLesson>>> weekOrderMap = new HashMap<Long, Map<String, List<OrderLesson>>>();
        Date dailyBeginTime = tempBeginDate;
        Map<String, List<OrderLesson>> dailyOrderLessonMap = null;

        String am = "am";
        String pm = "pm";
        String night = "night";

        // 按查询天数，进行数据补录
        while (dailyBeginTime.getTime() <= tempEndDate.getTime()) {
            dailyOrderLessonMap = new HashMap<String, List<OrderLesson>>();
            dailyOrderLessonMap.put(am, new ArrayList<OrderLesson>());
            dailyOrderLessonMap.put(pm, new ArrayList<OrderLesson>());
            dailyOrderLessonMap.put(night, new ArrayList<OrderLesson>());
            weekOrderMap.put(dailyBeginTime.getTime(), dailyOrderLessonMap);

            dailyBeginTime = DateUtil.addOnField(dailyBeginTime, Calendar.DATE, 1);
        }

        // 按时间递增排序
        Collections.sort(orderLessonList, new Comparator<OrderLesson>() {
            @Override
            public int compare(OrderLesson o1, OrderLesson o2) {
                long objTime1 = o1.getBeginTime().getTime();
                long objTime2 = o2.getBeginTime().getTime();
                if (objTime2 < objTime1) {
                    return 1;
                } else if (objTime2 > objTime1) {
                    return -1;
                }
                return 0;
            }
        });

        int orderCount = 0;

        if (orderLessonList != null && orderLessonList.size() > 0) {
            Date tempBegin = null;
            // 初始化每天开始时间
            dailyBeginTime = tempBeginDate;
            // 初始查询每天结束时间
            Date dailyEndTime = DateUtil.addOnField(dailyBeginTime, Calendar.DATE, 1);
            List<OrderLesson> intervalList = null;

            for (OrderLesson detail : orderLessonList) {
                // 发布课程开始时间
                tempBegin = detail.getBeginTime();
                // 如果发布开始时间消息大于结束时间，则从新获取每天开始时间
                if (tempBegin.getTime() >= dailyEndTime.getTime()) {
                    dailyBeginTime = getDailyBeginTime(dailyBeginTime, tempBegin);
                    dailyEndTime = DateUtil.addOnField(dailyBeginTime, Calendar.DATE, 1);
                }
                if (tempBegin.getTime() >= dailyBeginTime.getTime() && tempBegin.getTime() < dailyEndTime.getTime()) {
                    if (weekOrderMap.containsKey(dailyBeginTime.getTime())) {

                        dailyOrderLessonMap = weekOrderMap.get(dailyBeginTime.getTime());

                        if (detail.getLessonState() == 3) {
                            continue;
                        }

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(detail.getBeginTime());
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        if (hour >= 0 && hour < 12) {
                            intervalList = dailyOrderLessonMap.get(am);
                            intervalList.add(detail);
                            dailyOrderLessonMap.put(am, intervalList);

                        } else if (hour >= 12 && hour < 18) {
                            intervalList = dailyOrderLessonMap.get(pm);
                            intervalList.add(detail);
                            dailyOrderLessonMap.put(pm, intervalList);
                        } else if (hour >= 18 && hour < 24) {
                            intervalList = dailyOrderLessonMap.get(night);
                            intervalList.add(detail);
                            dailyOrderLessonMap.put(night, intervalList);
                        }
                        weekOrderMap.put(dailyBeginTime.getTime(), dailyOrderLessonMap);
                        orderCount++;
                    }
                }
            }
        }

        List<Map.Entry<Long, Map<String, List<OrderLesson>>>> entryList = new ArrayList<Map.Entry<Long, Map<String, List<OrderLesson>>>>(weekOrderMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Long, Map<String, List<OrderLesson>>>>() {
            @Override
            public int compare(Entry<Long, Map<String, List<OrderLesson>>> o1, Entry<Long, Map<String, List<OrderLesson>>> o2) {
                if (o2.getKey().intValue() < o1.getKey().intValue()) {
                    return 1;
                } else if (o2.getKey().intValue() == o1.getKey().intValue()) {
                    return 0;
                } else if (o2.getKey().intValue() > o1.getKey().intValue()) {
                    return -1;
                }
                return o2.getKey().compareTo(o1.getKey());
            }
        });


        Type tempType = new TypeToken<Map<Long, Map<String, List<OrderLesson>>>>() {
        }.getType();

        System.out.println(new Gson().toJson(weekOrderMap, tempType));

//	    // 按一天 上午、下午、晚上进行分组
//	    LinkedList<LinkedList<JsonArray>> dailyList = new LinkedList<LinkedList<JsonArray>>();
//	    LinkedList<JsonArray> tempList = null;
//	    for (Map.Entry<Long, Map<String,List<OrderLesson>>> entry : entryList) {
//
//	    	dailyOrderLessonMap = entry.getValue();
//	    	tempList = new LinkedList<JsonArray>();
//
//			tempList.add(getIntervalList(dailyOrderLessonMap.get(am)));
//			tempList.add(getIntervalList(dailyOrderLessonMap.get(pm)));
//			tempList.add(getIntervalList(dailyOrderLessonMap.get(night)));
//
//			dailyList.add(tempList);
//		}
//
//		Type type = new TypeToken<LinkedList<LinkedList<JsonArray>>>() { }.getType();
//
//		System.out.println(new Gson().toJsonTree(dailyList, type)));

    }

    @Test
    public void testOverPeriodCount() {

        TeacherOverPeriodCount teacherOverPeriodCount = talkOrderService.getTeacherOverPeriodCount(14);
        System.out.println(new Gson().toJson(teacherOverPeriodCount));

    }
}
