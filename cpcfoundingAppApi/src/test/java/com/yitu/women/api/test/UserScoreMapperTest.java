package com.yitu.women.api.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.domain.UserScore;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.enums.SourceTypeEnum;
import com.yitu.cpcFounding.api.mapper.UserScoreMapper;
import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.utils.WeekRankingUtil;
import com.yitu.cpcFounding.api.vo.UserScoreDetailVO;
import com.yitu.cpcFounding.api.vo.UserScoreDetailsVO;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 16:06
 */

public class UserScoreMapperTest extends FatherTest{
    @Resource
    private UserScoreMapper userScoreMapper;
    @Resource
    private WeekRankingUtil weekRankingUtil;

    @Test
    public void selectUserScoreDetails(){
        Page<UserScoreDetailVO> page = new Page<>(1, 10);
        IPage<UserScoreDetailsVO> userScoreDetailsVOIPage = userScoreMapper.selectUserScoreDetails(page, 17L,null);

        for (UserScoreDetailsVO record : userScoreDetailsVOIPage.getRecords()) {

        }
    }

    @Test
    public void selectUserScoreRanking(){
        List<UserRankingListVO> userScoreVOS = userScoreMapper.selectUserScoreRanking(0, 2000);

        for (UserRankingListVO userScoreVO : userScoreVOS) {
            System.out.println(userScoreVO);
        }
    }

    @Test
    public void selectUserScoreWeekRanking(){
        DatePo weekDate = weekRankingUtil.getWeekDate();
        List<UserRankingListVO> userRankingListVOS = userScoreMapper.selectUserScoreWeekRanking(0, 2000, weekDate.getBeginDate(), weekDate.getEndDate());
        for (UserRankingListVO userRankingListVO : userRankingListVOS) {
            System.out.println(userRankingListVO);
        }
    }

    @Test
    public void selectSumOfDayByScoreTypeAndUserId(){
        Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(3, 42L);
        System.out.println("结果："+sum);
    }

    @Test
    public void selectOne(){
        LambdaQueryWrapper<UserScore> wrapper=new LambdaQueryWrapper();
        wrapper.eq(UserScore::getUserId,52);
        wrapper.eq(UserScore::getDeleted, DeletedEnum.NOT_DELETE);
        wrapper.eq(UserScore::getSourceType, SourceTypeEnum.JOIN_TEAM.getValue());

        UserScore JoinTeamScore=userScoreMapper.selectOne(wrapper);

        System.out.println("来了结果："+JoinTeamScore);
    }

    @Autowired
    private UserScoreService userScoreService;

    @Test
    public void addScoreTest(){
    }

    @Test
    public void selectScoreTotalOfDay(){
        UserScoreTotalVO userScoreTotalVO = userScoreMapper.selectScoreTotalOfDay(45L);
        System.out.println(userScoreTotalVO);
    }
}
