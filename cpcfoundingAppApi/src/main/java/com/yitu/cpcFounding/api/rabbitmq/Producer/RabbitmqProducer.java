package com.yitu.cpcFounding.api.rabbitmq.Producer;

import com.yitu.cpcFounding.api.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuzhaowei
 * @date 2020/6/7
 */
@Component
@Slf4j
public class RabbitmqProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * @param queueEnum 枚举队列
     * @param data      数据
     * @return void
     * @Description: 发送消息
     * @author liuzhaowei
     * @date 2020/6/7
     */
    public void sendMessage(QueueEnum queueEnum, String data) {
        //log.info("rabbitmq发送消息：{}", data);
        try {
            amqpTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), data);
        } catch (Exception ex) {
            log.error("rabbitmq发送消息失败：", ex);
        }
    }

    /**
     * @param queueEnum  枚举队列
     * @param data       数据
     * @param delayTimes 延期时间  单位毫秒
     * @return void
     * @Description: 发送过期消息
     * @author liuzhaowei
     * @date 2020/6/7
     */
    public void sendExpireMessage(QueueEnum queueEnum, String data, long delayTimes) {
        log.info("rabbitmq发送消息：{}", data);
        try {
            amqpTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), data, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //给消息设置延迟毫秒值
                    message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                    return message;
                }
            });
        } catch (Exception ex) {
            log.error("rabbitmq发送消息失败：", ex);
        }
    }

}
