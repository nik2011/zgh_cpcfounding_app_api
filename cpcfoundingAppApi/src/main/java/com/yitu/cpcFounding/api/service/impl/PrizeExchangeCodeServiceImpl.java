package com.yitu.cpcFounding.api.service.impl;

import com.yitu.cpcFounding.api.mapper.PrizeExchangeCodeMapper;
import com.yitu.cpcFounding.api.service.PrizeExchangeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 奖品兑换码服务实现
 *
 * @author shenjun
 * @date 2021-01-21 11:09:10
 */
@Service
public class PrizeExchangeCodeServiceImpl implements PrizeExchangeCodeService {

    @Autowired
    private PrizeExchangeCodeMapper prizeExchangeCodeMapper;

}
