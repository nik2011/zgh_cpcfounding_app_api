package com.yitu.cpcFounding.api.scheduled;

import com.yitu.cpcFounding.api.service.TeamService;
import com.yitu.cpcFounding.api.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 排行榜定时任务
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 9:32
 */

@Component
public class RankingTask {
    @Autowired
    private UserScoreService userScoreService;
    @Autowired
    private TeamService teamService;

//    /**
//     * 用户总榜
//     *
//     * @param
//     * @return void
//     * @author qixinyi
//     * @date 2021/6/8 10:07
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void saveUserScoreRanking() {
//        userScoreService.saveUserScoreRankingToRedis();
//    }
//
//    /**
//     * 用户周榜
//     *
//     * @param
//     * @return void
//     * @author qixinyi
//     * @date 2021/6/8 10:07
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void saveUserScoreWeekRanking() {
//        userScoreService.saveUserScoreWeekRankingToRedis();
//    }
//
//    /**
//     * 团队总榜
//     *
//     * @param
//     * @return void
//     * @author qixinyi
//     * @date 2021/6/8 10:07
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void saveTeamScoreRanking() {
//        teamService.saveTeamScoreRankingToRedis();
//    }
//
//    /**
//     * 团队周榜
//     *
//     * @param
//     * @return void
//     * @author qixinyi
//     * @date 2021/6/8 10:07
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void saveTeamWeekScoreRanking() {
//        teamService.saveTeamScoreWeekRankingToRedis();
//    }
//
//    /**
//     * 答题总榜
//     *
//     * @param
//     * @return void
//     * @author qixinyi
//     * @date 2021/6/8 10:07
//     */
//    @Scheduled(cron = "0 0/3 * * * ?")
//    public void saveAnswerScoreRanking() {
//        userScoreService.saveAnswerScoreRankingToRedis();
//    }
}
