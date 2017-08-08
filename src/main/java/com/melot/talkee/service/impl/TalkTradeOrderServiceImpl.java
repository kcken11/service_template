package com.melot.talkee.service.impl;

import com.melot.talkee.dao.*;
import com.melot.talkee.driver.domain.*;
import com.melot.talkee.driver.service.TalkTradeOrderService;
import com.melot.talkee.driver.service.TalkUserService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by mn on 2017/5/22.
 */
@Service
public class TalkTradeOrderServiceImpl implements TalkTradeOrderService {

    private static Logger logger = Logger.getLogger(TalkTradeOrderServiceImpl.class);

    @Autowired
    private BuyPeriodsMapper buyPeriodsMapper;

    @Autowired
    private StudentPeriodsMapper studentPeriodsMapper;

    @Autowired
    private OrderPackageMapper orderPackageMapper;

    @Autowired
    private CheckinInfoMapper checkinInfoMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private TalkUserService talkUserService;

    /**
     * @param order 订单相关信息
     * @return
     */
    @Override
    @Transactional
    public String addTradeOrder(BuyPeriods order) {
        if (order.getUserId() == 0) {
            return TalkTagCodeEnum.USER_NOT_EXIST;
        }

        //step.1 新增套餐
        OrderPackage orderPackage = new OrderPackage();
        orderPackage.setPeriords(order.getPeriods());
        orderPackage.setOldAmount(order.getAmount());
        orderPackage.setCurAmount(order.getAmount());
        orderPackage.setVaildDays(differentDays(new Date(), order.getValidDate()));
        orderPackage.setBeginTime(new Date());
        orderPackage.setEndTime(order.getValidDate());
        orderPackage.setState(1);
        orderPackage.setPackageName("三方订单为" + order.getCallbakOrderid() + "的套餐");
        orderPackage.setPublicLesson(0);
        orderPackageMapper.insert(orderPackage);
        String orderId = getNextOrderId();
        order.setOrderid(orderId);
        order.setBuyTime(new Date());
        order.setPackageId(orderPackage.getPackageId());
        order.setExtraPublicLesson(0);
        order.setExtraPeriods(0);
        order.setPublicLesson(0);
        order.setStatus(0);
        int resultCode = buyPeriodsMapper.insertSelective(order);
        //一期下单成功后 确认
        if (resultCode > 0) {
            //确认订单，状态为成功
            confirmTradeOrder(orderId, 1, order.getFreeTimes());
            //  System.out.println("下单成功，订单号"+orderId);
        }

        return TalkTagCodeEnum.SUCCESS;

    }

    /**
     * @param orderId 订单id
     * @param status  确认后的状态
     * @return
     */
    @Override
    @Transactional
    public String confirmTradeOrder(String orderId, int status, int freetimes) {
        BuyPeriods buyPeriod = buyPeriodsMapper.selectByOrderId(orderId);
        if (buyPeriod == null) {
            return TalkTagCodeEnum.TRADE_ORDER_NOT_EXIST;
        }
        try {
            Date valid_end_time = new Date();
            if (buyPeriod.getPackageId() != 0) {
                OrderPackage orderPackage = orderPackageMapper.selectByPrimaryKey(buyPeriod.getPackageId());
                int valid_days = orderPackage.getVaildDays();
                valid_end_time = DateUtils.addDays(valid_end_time, valid_days);
            }//只有订单状态为未完成的订单才进行处理
            if (buyPeriod.getStatus() == 0) {
                buyPeriod.setStatus(status);
                buyPeriod.setAffirmTime(new Date());
                int resultCode = buyPeriodsMapper.updateByPrimaryKey(buyPeriod);
                //确认订单为完成的 增加学生课时
                if (resultCode > 0 && status == 1) {
                    //先判断用户类型
                    StudentInfo student = talkUserService.getStudentInfoByUserId(buyPeriod.getUserId());

                    int periodIncr = 0, publicIncr = 0;
                    StudentPeriods studentPeriods = null;
                    Map<String, Object> params = new HashMap<>();
                    params.put("userId", buyPeriod.getUserId());
                    periodIncr = buyPeriod.getPeriods() + buyPeriod.getExtraPeriods();
                    publicIncr = buyPeriod.getExtraPublicLesson() + buyPeriod.getPublicLesson();
                    //更新普通课时
                    if (periodIncr > 0) {
                        params.put("periodsType", TalkCommonEnum.PERIOD_TYPE_GENNEL);
                        studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(params);
                        if (studentPeriods == null) {
                            initStudentPeriods(TalkCommonEnum.PERIOD_TYPE_GENNEL, buyPeriod.getUserId());
                            studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(params);
                        }
                        studentPeriods.setLastOrderId(String.valueOf(orderId));

                        addStudentPeriods(studentPeriods, periodIncr, valid_end_time);





                    }
                   /* //更新公开课时
                    if (publicIncr > 0) {
                        params.put("periodsType", TalkCommonEnum.PERIOD_TYPE_PUBLIC);
                        studentPeriods = studentPeriodsMapper.selectByUserIdAndPeriodType(params);
                        if (studentPeriods != null) {
                            studentPeriods.setLastOrderId(String.valueOf(orderId));
                            addStudentPeriods(studentPeriods, publicIncr, valid_end_time);
                        }
                    }*/
                    //更新免责次数
                    CheckinInfo checkinInfo = checkinInfoMapper.selectByPrimaryKey(student.getUserId());
                    //考勤不存在时 初始化
                    if (checkinInfo == null) {
                        initCheckinInfo(student.getUserId(), TalkCommonEnum.USER_ACCOUNT_TYPE_STUDENT);
                        checkinInfo = checkinInfoMapper.selectByPrimaryKey(student.getUserId());
                    }
                    checkinInfo.setAllFreeAskForLeave(checkinInfo.getAllFreeAskForLeave() + freetimes);
                    checkinInfoMapper.updateByPrimaryKey(checkinInfo);
                }

            }

        } catch (Exception e) {
            logger.error("TalkTradeOrderService.confirmTradeOrder is error" + e.getLocalizedMessage());
            e.printStackTrace();
            return TalkTagCodeEnum.MODULE_PARAMETER_EXCEPTION;
        }
        return TalkTagCodeEnum.SUCCESS;
    }

    private void addStudentPeriods(StudentPeriods studentPeriods, int incr, Date valid_end_time) {
        if (studentPeriods.getEndTime().getTime() < valid_end_time.getTime()) {
            studentPeriods.setEndTime(valid_end_time);
        }
        studentPeriods.setVaildPreviwPeriods(studentPeriods.getVaildPreviwPeriods() + incr);
        studentPeriods.setAllPeriods(studentPeriods.getAllPeriods() + incr);
        studentPeriods.setCurPeriods(studentPeriods.getCurPeriods() + incr);

        studentPeriods.setAccumulativeTotal(25 * 60 * 1000 * (studentPeriods.getOverPeriods()));

        studentPeriodsMapper.updateByPrimaryKey(studentPeriods);
    }

    private static String getNextOrderId() {
        Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String orderid = dateFormat.format(new Date()) + String.valueOf(random.nextInt(100));
        return orderid;
    }


    public void initStudentPeriods(int periodType, int userId) {
        Date eventTime = new Date();
        StudentPeriods studentPeriods = new StudentPeriods();
        studentPeriods.setUserId(userId);
        studentPeriods.setPeriodsType(periodType);
        studentPeriods.setDtime(eventTime);
        studentPeriods.setBeginTime(eventTime);
        studentPeriods.setEndTime(eventTime);
        studentPeriods.setCurPeriods(0);
        studentPeriods.setVaildPreviwPeriods(0);
        studentPeriods.setOverPeriods(0);
        studentPeriods.setAllPeriods(0);
        studentPeriods.setAccumulativeTotal(0);

        studentPeriodsMapper.insert(studentPeriods);
    }

    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            return day2-day1;
        }
    }

    /**
     * 初始化用户考勤信息
     *
     * @param userId
     * @param userType 用户类型(1:学生 2：老师)
     * @return
     */
    public int initCheckinInfo(int userId, int userType) {
        CheckinInfo checkinInfo = new CheckinInfo();
        checkinInfo.setAllFreeAskForLeave(0);
        checkinInfo.setDayOffTimes(0);
        checkinInfo.setDelayTimes(0);
        checkinInfo.setFreeAskForLeave(0);
        checkinInfo.setLeaveEarlyTimes(0);
        checkinInfo.setNormalOver(0);
        checkinInfo.setPeriodsAskForLeave(0);
        checkinInfo.setUserId(userId);
        checkinInfo.setUserType(userType);
        return checkinInfoMapper.insert(checkinInfo);
    }


}
