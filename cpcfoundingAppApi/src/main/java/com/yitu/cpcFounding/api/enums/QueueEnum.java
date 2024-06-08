package com.yitu.cpcFounding.api.enums;

import lombok.Getter;

/**
 * 消息队列枚举配置
 * Created by macro on 2018/9/14.
 */
@Getter
public enum QueueEnum {
    // 摇一摇日志记录队列
    YEAR_SHAKE_LOG_QUEUE("cpcFounding_shake_log_direct","cpcFounding_shake_log_queue","cpcFounding_shake_log_route"),
    // 易盾
    YI_DUN_QUEUE("cpcFounding_yi_dun_direct","cpcFounding_yi_dun_queue","cpcFounding_yi_dun_route"),
    // 点赞
    PRAISE_QUEUE("cpcFounding_praise_direct","cpcFounding_praise_queue","cpcFounding_praise_route"),
    // 摇一摇中奖记录队列
    YEAR_SHAKE_WINNING_QUEUE("cpcFounding_shake_winning_direct","cpcFounding_shake_winning_queue","cpcFounding_shake_winning_route"),
    // 党史阅读量
    VISIT_NUM_QUEUE("cpcFounding_visit_num_direct","cpcFounding_visit_num_queue","cpcFounding_visit_mum_route"),
    // 加积分明细
    USER_SCORE_QUEUE("cpcFounding_user_score_direct","cpcFounding_user_score_queue","cpcFounding_user_score_route"),
    // 加用户答题
    USER_ANSWER_QUEUE("cpcFounding_batchSaveUserAnswer_direct","cpcFounding_batchSaveUserAnswer_queue","cpcFounding_batchSaveUserAnswer_route");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
