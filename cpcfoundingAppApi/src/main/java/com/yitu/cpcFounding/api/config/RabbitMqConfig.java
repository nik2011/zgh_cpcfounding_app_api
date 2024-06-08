package com.yitu.cpcFounding.api.config;

import com.yitu.cpcFounding.api.enums.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列配置
 * Created by macro on 2018/9/14.
 */
@Configuration
public class RabbitMqConfig {
    /**
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     * @Description: 摇一摇日志记录交换机
     * @author liuzhaowei
     * @date 2020/6/5
     */
    @Bean
    DirectExchange yearShakeLogDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.YEAR_SHAKE_LOG_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue yearShakeLogQueue() {
        return new Queue(QueueEnum.YEAR_SHAKE_LOG_QUEUE.getName());
    }

    @Bean
    Binding yearShakeLogBinding(DirectExchange yearShakeLogDirect, Queue yearShakeLogQueue) {
        return BindingBuilder.bind(yearShakeLogQueue).to(yearShakeLogDirect).with(QueueEnum.YEAR_SHAKE_LOG_QUEUE.getRouteKey());
    }

    /**
     * 易盾
     *
     * @return org.springframework.amqp.core.DirectExchange
     * @author shenjun
     * @date 2021/1/25 15:17
     */
    @Bean
    DirectExchange yiDunDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.YI_DUN_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue yiDunQueue() {
        return new Queue(QueueEnum.YI_DUN_QUEUE.getName());
    }

    @Bean
    Binding yiDunBinding(DirectExchange yiDunDirect, Queue yiDunQueue) {
        return BindingBuilder.bind(yiDunQueue).to(yiDunDirect).with(QueueEnum.YI_DUN_QUEUE.getRouteKey());
    }

    /**
     * 点赞
     *
     * @return org.springframework.amqp.core.DirectExchange
     * @author shenjun
     * @date 2021/1/25 15:17
     */
    @Bean
    DirectExchange praiseDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.PRAISE_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue praiseQueue() {
        return new Queue(QueueEnum.PRAISE_QUEUE.getName());
    }

    @Bean
    Binding praiseBinding(DirectExchange praiseDirect, Queue praiseQueue) {
        return BindingBuilder.bind(praiseQueue).to(praiseDirect).with(QueueEnum.PRAISE_QUEUE.getRouteKey());
    }

    /**
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     * @Description: 摇一摇中奖记录交换机
     * @author liuzhaowei
     * @date 2020/6/5
     */
    @Bean
    DirectExchange yearShakeWinningDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.YEAR_SHAKE_WINNING_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue yearShakeWinningQueue() {
        return new Queue(QueueEnum.YEAR_SHAKE_WINNING_QUEUE.getName());
    }

    @Bean
    Binding yearShakeWinningBinding(DirectExchange yearShakeWinningDirect, Queue yearShakeWinningQueue) {
        return BindingBuilder.bind(yearShakeWinningQueue).to(yearShakeWinningDirect).with(QueueEnum.YEAR_SHAKE_WINNING_QUEUE.getRouteKey());
    }

    /**
     * 党史阅读量
     *
     * @return org.springframework.amqp.core.DirectExchange
     * @author wangping
     * @date 2021/6/7 17:36
     */
    @Bean
    DirectExchange visitNumDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.VISIT_NUM_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue visitNumQueue() {
        return new Queue(QueueEnum.VISIT_NUM_QUEUE.getName());
    }

    @Bean
    Binding visitNumBinding(DirectExchange visitNumDirect, Queue visitNumQueue) {
        return BindingBuilder.bind(visitNumQueue).to(visitNumDirect).with(QueueEnum.VISIT_NUM_QUEUE.getRouteKey());
    }

    /**
     * 加积分
     *
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     * @author qixinyi
     * @date 2021/6/15 15:49
     */
    @Bean
    DirectExchange userScoreDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.USER_SCORE_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue userScoreQueue() {
        return new Queue(QueueEnum.USER_SCORE_QUEUE.getName());
    }

    @Bean
    Binding userScoreBinding(DirectExchange userScoreDirect, Queue userScoreQueue) {
        return BindingBuilder.bind(userScoreQueue).to(userScoreDirect).with(QueueEnum.USER_SCORE_QUEUE.getRouteKey());
    }

    /**
     * 批量加用户答题
     *
     * @param
     * @return org.springframework.amqp.core.DirectExchange
     * @author qixinyi
     * @date 2021/6/15 15:49
     */
    @Bean
    DirectExchange userAnswerDirect() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.USER_ANSWER_QUEUE.getExchange()).durable(true).build();
    }

    @Bean
    public Queue userAnswerQueue() {
        return new Queue(QueueEnum.USER_ANSWER_QUEUE.getName());
    }

    @Bean
    Binding userAnswerBinding(DirectExchange userAnswerDirect, Queue userAnswerQueue) {
        return BindingBuilder.bind(userAnswerQueue).to(userAnswerDirect).with(QueueEnum.USER_ANSWER_QUEUE.getRouteKey());
    }
}
