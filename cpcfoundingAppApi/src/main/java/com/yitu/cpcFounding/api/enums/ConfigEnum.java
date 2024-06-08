package com.yitu.cpcFounding.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 配置表枚举
 *
 * @author pangshihe
 * @date 2021/1/21 11:28
 */
@Getter
@AllArgsConstructor
public enum ConfigEnum {
    YEAR_FESTIVAL(1, "晒年味标签"),
    AREA(4, "区域"),
//    HOT(5, "暖城"),
    ACTIVE_ENTRANCE(6, "活动入口"),
    AUDIT_NUM(7, "审贴人数"),
    SHOW_YEAR_PRIZE_WINNER(8, "是否公布晒年味获奖名单"),
    DANG_HIS_LABEL(10,"党史标签"),
    AD_TYPE_LABEL(11,"广告类型"),
    RANKING_LABEL(13,"奖品提示，排行榜提示语")
    ;
//    HOT_PRIZE_WINNER(9, "是否公布暖城获奖名单");


    private int code;
    private String name;


    public static String getName(int code) {
        for (ConfigEnum c : ConfigEnum.values()) {
            if (c.code == code) {
                return c.name;
            }
        }
        return null;
    }

    public static ConfigEnum getEnumByCode(int code) {
        for (ConfigEnum type : ConfigEnum.values()) {
            if (code == type.code) {
                return type;
            }
        }
        return null;
    }
}

