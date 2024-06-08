package com.yitu.cpcFounding.api.enums;

import com.yitu.cpcFounding.api.enums.prize.UserPrizeStatusEnum;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * 领取状态枚举
 *
 * @author qinmingtong
 * @date 2021/2/4 15:12
 */
@Slf4j
@Getter
public enum GetStatusEnum {

    /**
     *  待领取
     */
    BEFORE_RECEIVED(0, "待领取"),

    /**
     *  已领取
     */
    RECEIVED(1, "已领取"),

    /**
     *  已失效
     */
    AVAILABLE(2, "已失效");

    /**
     * 状态码
     */
    private final int value;

    /**
     * 状态描述
     */
    private final String name;

    GetStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据领取状态码获取领取枚举
     *
     * @param value 状态码
     * @return GetStatusEnum
     * @author qinmingtong
     * @date 2021/2/4 15:17
     */
    public static GetStatusEnum fromValue(Integer value) {
        if (value != null) {
            for (GetStatusEnum getStatusEnum : GetStatusEnum.values()) {
                if (getStatusEnum.getValue() == value) {
                    return getStatusEnum;
                }
            }
        }
        return null;
    }

    /**
     * 根据订单状态和中奖时间判断获取领取状态
     *
     * @param userPrizeStatus 订单状态
     * @param addDate 中奖时间
     * @return java.lang.Integer
     * @author qinmingtong
     * @date 2021/2/4 15:35
     */
    public static Integer getGetStatus(Integer userPrizeStatus, Date addDate){
        try {
            if(userPrizeStatus.equals(UserPrizeStatusEnum.EXCHANGED.getCode())
                    || userPrizeStatus.equals(UserPrizeStatusEnum.SEND.getCode())){
                return GetStatusEnum.RECEIVED.getValue();
            }
            int maxDuringDay = 7;
            if(userPrizeStatus.equals(UserPrizeStatusEnum.UN_EXCHANGE.getCode())
                    && DateTimeUtil.getDayDiffer(addDate, new Date()) <= maxDuringDay){
                return GetStatusEnum.BEFORE_RECEIVED.getValue();
            }else{
                return GetStatusEnum.AVAILABLE.getValue();
            }
        }catch (ParseException e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
