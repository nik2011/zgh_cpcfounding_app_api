package com.yitu.women.api.test;

import com.alibaba.fastjson.JSON;
import com.yitu.cpcFounding.api.CPCFoundingAppApiApplication;
import com.yitu.cpcFounding.api.service.AdService;
import com.yitu.cpcFounding.api.service.TradeUnionService;
import com.yitu.cpcFounding.api.vo.ad.IndexLsAdVO;
import com.yitu.cpcFounding.api.vo.ad.SunActiveInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.annotation.Resource;

/**
 * TODO
 *
 * @author jxc
 * @date 2020/12/15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPCFoundingAppApiApplication.class)
@WebAppConfiguration
public class ApplicationTests {

    @Resource
    private AdService lsadService;
    @Resource
    private TradeUnionService tradeUnionService;

//    @Test
//    public void getIndexAdInfo() {
//        IndexLsAdVO indexLsAdVo = lsadService.getIndexAdInfo();
//        log.error("广告信息：{}", JSON.toJSONString(indexLsAdVo));
//    }
//
//    @Test
//    public void getSunActiveInfo() {
//        SunActiveInfoVO sunActiveInfoVo = lsadService.getSunActiveInfo();
//        log.error("晒活动页面信息：{}", JSON.toJSONString(sunActiveInfoVo));
//    }

   @Test
    public void tradeUnionAuth() {
       tradeUnionService.tradeUnionAuth("13266583211","123456");
    }

}