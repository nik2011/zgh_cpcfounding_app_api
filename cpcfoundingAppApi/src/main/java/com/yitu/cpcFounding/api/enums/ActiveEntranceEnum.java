package com.yitu.cpcFounding.api.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 活动入口枚举1
 *
 * @author zouhao
 * @version 1.0
 * @date 2020/11/30
 */
public enum ActiveEntranceEnum implements IEnum<Integer> {

    SHAKE(1, "转盘抽奖"),

    YEAR_PRAISE(2, "晒幸福"),

    //    HOT(3, "暖城活动"),
    ANSWER(4, "答题活动"),
    USER_MEDAL(5, "向劳模致敬"),
    JION_TEAM(6, "加入团队"),
    USER_SCORE(7, "积分活动");
    private final int code;
    private final String desc;

    ActiveEntranceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static ActiveEntranceEnum parse(int type) {
        for (ActiveEntranceEnum item : ActiveEntranceEnum.values()) {
            if (item.getValue() == type) {
                return item;
            }
        }
        return null;
    }

    public static String getText(int type) {
        ActiveEntranceEnum item = ActiveEntranceEnum.parse(type);
        return item != null ? item.getDesc() : "";
    }
}
