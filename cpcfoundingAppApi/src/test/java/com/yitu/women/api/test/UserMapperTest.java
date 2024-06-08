package com.yitu.women.api.test;

import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.mapper.UserScoreMapper;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 15:00
 */

public class UserMapperTest extends FatherTest{
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private UserScoreMapper userScoreMapper;

    @Test
    public void selectUserScoreRanking(){
        List<UserRankingListVO> userScoreVOS = userScoreMapper.selectUserScoreRanking(1, 2);

        for (UserRankingListVO userScoreVO : userScoreVOS) {
            System.out.println(userScoreVO);
        }
    }

    @Test
    public void testTest(){
        Object o=redisTemplate.opsForHash().get(Configs.REDIS_ACTIVE_USER_RANK, "30225");
        System.out.println(o);
    }
}
