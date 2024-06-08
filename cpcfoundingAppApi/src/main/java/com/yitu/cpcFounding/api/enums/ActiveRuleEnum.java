package com.yitu.cpcFounding.api.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 活动规则枚举1
 *
 * @author jxc
 * @date 2021/1/26
 */
public enum  ActiveRuleEnum implements IEnum<Integer> {
    YEAR_PRAISE_RULE(1, "晒年味规则");

    private final int code;
    private final String desc;

    ActiveRuleEnum(int code, String desc) {
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
}
