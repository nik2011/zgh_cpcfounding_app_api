//package com.yitu.women.api.rabbitmq.Consumer;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.rabbitmq.client.Channel;
//import LsShakeTakeLog;
//import LsShowYear;
//import LsPrizeBookMapper;
//import LsPrizeExchangeCodeMapper;
//import LsShakeTakeLogMapper;
//import LsUserMapper;
//import HotService;
//import LsPrizeBookService;
//import LsShowYearService;
//import LsUserPrizeService;
//import PraiseVO;
//import SendHotMQ;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.Message;
//import org.springframework.stereotype.Component;
//
///**
// * @author liuzhaowei
// * @date 2020/6/7
// */
//@Component
//@Slf4j
//public class RabbitmqConsumer {
//    @Autowired
//    private LsShakeTakeLogMapper lsShakeTakeLogMapper;
//
//    @Autowired
//    private LsShowYearService lsShowYearService;
//    @Autowired
//    private LsUserMapper lsUserMapper;
//    @Autowired
//    private HotService hotService;
//    @Autowired
//    private LsPrizeBookMapper lsPrizeBookMapper;
//    @Autowired
//    private LsPrizeExchangeCodeMapper lsPrizeExchangeCodeMapper;
//    @Autowired
//    private LsUserPrizeService lsUserPrizeService;
//    @Autowired
//    private LsPrizeBookService lsPrizeBookService;
//
//    /**
//     * @param data, message, channel
//     * @return void
//     * @Description:摇一摇日志记录队列
//     * @author liuzhaowei
//     * @date 2021/1/18
//     */
//    @RabbitListener(queues = "year_shake_log_queue")
//    public void handlerShakeLogQueue(String data, Message message, Channel channel) {
//        //log.info("rabbit接收shake_log_queue信息：{}", data);
//        try {
//            Gson gson = new Gson();
//            LsShakeTakeLog takeLog = gson.fromJson(data, LsShakeTakeLog.class);
//            lsShakeTakeLogMapper.insert(takeLog);
//            //减摇一摇次数
////        if (takeLog.getInOrOut() < 0) {
////            lsUserMapper.minusYaoCount(takeLog.getUserId());
////        }
//        } catch (Exception e) {
//            log.error("rabbit接收year_shake_winning_queue信息： {}", data);
//            log.error("rabbit接收year_shake_winning_queue信息： {}", e.getMessage());
//        }
//    }
//
//    @RabbitListener(queues = "yi_dun_queue")
//    public void handlerYiDunQueue(String data, Message message, Channel channel) {
//          log.info("rabbit接收yi_dun_queue信息：{}", data);
////        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
////        LsShowYear lsShowYear = gson.fromJson(data, new TypeToken<LsShowYear>() {
////        }.getType());
////        lsShowYearService.checkYiDun(lsShowYear);
//    }
//
//    @RabbitListener(queues = "praise_queue")
//    public void handlerPraiseQueue(String data, Message message, Channel channel) {
//          log.info("rabbit接收praise_queue信息：{}", data);
////        Gson gson = new Gson();
////        PraiseVO praiseVO = gson.fromJson(data, new TypeToken<PraiseVO>() {
////        }.getType());
////        lsShowYearService.setPraiseToDB(praiseVO);
//    }
//
//    @RabbitListener(queues = "send_hot_queue")
//    public void handlerSendHotQueue(String data, Message message, Channel channel) {
//          log.info("rabbit接收send_hot_queue信息：{}", data);
////        Gson gson = new Gson();
////        SendHotMQ sendHotMQ = gson.fromJson(data, new TypeToken<SendHotMQ>() {
////        }.getType());
////        hotService.addUserAreaHotMQ(sendHotMQ);
//    }
//
////    @RabbitListener(queues = "click_hot_link_queue")
////    public void handlerClickHotLinkQueue(String data, Message message, Channel channel) {
////        //log.info("rabbit接收click_hot_link_queue信息：{}", data);
////        Gson gson = new Gson();
////        ClickHotLinkMQ clickHotLinkMQ = gson.fromJson(data, new TypeToken<ClickHotLinkMQ>() {
////        }.getType());
////        hotService.clickHotLinkMQ(clickHotLinkMQ);
////    }
//
//    /**
//     * @param data, message, channel
//     * @return void
//     * @Description:摇一摇中奖记录队列
//     * @author liuzhaowei
//     * @date 2021/1/18
//     */
//    @RabbitListener(queues = "year_shake_winning_queue")
//    //  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void handlerShakeWinningQueue(String data, Message message, Channel channel) {
//       // log.info("rabbit接收year_shake_winning_queue信息：{}", data);
//        try {
//            lsPrizeBookService.handelShakeWinningQueue(data);
//        } catch (Exception e) {
//            log.error("rabbit接收year_shake_winning_queue信息： {}", data);
//            log.error("rabbit接收year_shake_winning_queue信息： {}", e.getMessage());
//        }
//    }
//
////    /**
////     * @Description:需要确认
////     * @param data, message, channel
////     * @author liuzhaowei
////     * @date 2021/1/18
////     * @return void
////     */
////    @RabbitListener(queues ="mall.order.cancel" )
////    public void handler(String data, Message message, Channel channel) {
////        //处理成功之后获取deliveryTag并进行手工的ACK操作，因为我们配置文件里配置的是手工签收
////        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
//////        System.out.println("channel:"+channel);
//////        System.out.println("message:"+message);
////        try {
////            log.info("rabbit接收nik_test_queue信息：{}", data);
////            channel.basicAck(deliveryTag, false);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//}
