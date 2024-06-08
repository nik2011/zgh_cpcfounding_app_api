
package com.yitu.cpcFounding.api.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;


/**
 * 广告类型枚举1
 *
 * @author zouhao
 * @version 1.0
 * @date 2020/11/30
 */

public enum AdTypeEnum implements IEnum<Integer> {

    BANNER(1, "banner图"),
    MATERIAL_ACTIVE(2, "福利活动"),
    VIDEO(3, "视频广告"),
    HUI_FU_WU(4, "惠服务"),
    GRADE(5, "深圳工会"),
    //    HOT_CITY(6, "暖城"),
    TOP_PIC_INDEX(7, "首页头图"),

    //    SHARE_HOT(8, "暖城分享"),
    SHARE_SHAKE(9, "摇一摇分享"),
    SHARE_YEAR_PRAISE(10, "晒的分享"),

    TOP_IC_YEAR_PRAISE(11, "晒头图"),
    //    TOP_PIC_HOT(12, "暖城头图"),
    TOP_PIC_SHAKE(13, "摇一摇头图"),

    RULE_HOT(14, "晒活动规则"),
    RULE_SHAKE(15, "暖城活动规则"),
    RULE_YEAR_PRAISE(16, "摇一摇活动规则"),
    PRIZE_DETAILS(17, "奖品详情"),
    DISCLAIMER_THAT(18, "免责说明"),
    YAO_ADVERTISING(19, "摇一摇广告"),
    PRIVACY_POLICY(20, "隐私政策"),

    MIDDLE_IC_YEAR_PRAISE(21, "晒中间图"),
    SHARE_QUESTION(22, "答题的分享"),
    SHARE_INDEX(23, "首页分享"),
    SHARE_AD(24,"广告分享"),
    ARTICLE_SHARE(73,"文章分享"),
    RANKING_SHARE(74,"排行榜分享"),
    TASK_SHARE(75,"任务分享"),
    ROTARY_DRAW_SHARE(76,"抽奖分享"),

    LUCKY_DRAW(25,"幸运大抽奖"),
    EXCLUSIVE_HEAD(26,"专属头像"),
    IMAGE_PHONE(27,"形象照"),
    HOME_LIST_AD(28,"首页列表广告"),
    TEAM_PRIZE(29,"团队获奖"),

    JOIN_TEAM_SHARE_PICTURE(30,"加入团队分享图"),
    SEND_MEDAL_SHARE_PICTURE(31,"送奖章分享图"),
    IMAGE_PHOTO_SHARE_PICTURE(32,"形象照分享图"),
    HEAD_SHARE_PICTURE(33,"个性头像分享图"),
    SEND_MEDAL_POPUP_PICTURE(34,"送奖章弹窗图"),

    SEND_MEDAL_RULE(35, "送奖章活动规则"),
    LUCKY_DRAW_RULE(36, "大转盘抽奖活动规则"),

    DIY_HEAD_LIST(37, "个性头像框集"),
    FUNNY_IMAGE_LIST(38, "趣味形象照集"),
    EMPTY_FUNNY_IMAGE_LIST(39, "掏空趣味形象照集")
    ;

    private final int code;
    private final String desc;

    AdTypeEnum(int code, String desc) {
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

    public static AdTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (AdTypeEnum adTypeEnum : AdTypeEnum.values()) {
                if (adTypeEnum.getValue() == value) {
                    return adTypeEnum;
                }
            }
        }
        return null;
    }
}

