package com.yitu.women.api.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yitu.cpcFounding.api.domain.Team;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.enums.TeamTypeEnum;
import com.yitu.cpcFounding.api.mapper.TeamMapper;
import com.yitu.cpcFounding.api.vo.team.TeamRankingListVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/9 19:05
 */

public class TeamMapperTest extends FatherTest{
    @Autowired
    private TeamMapper teamMapper;

    @Test
    public void selectTeamList(){
        LambdaQueryWrapper<Team> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Team::getDeleted,DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.STREET.getValue());
        List<Team> list=teamMapper.selectList(wrapper);

        System.out.println("-------街道------");
        for (Team team : list) {
            System.out.println(team);
        }
        System.out.println("------结束------");
        System.out.println("\n");

        LambdaQueryWrapper<Team> teamWrapper=new LambdaQueryWrapper<>();
        teamWrapper.eq(Team::getDeleted,DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.TEAM.getValue())
                .eq(Team::getParentId,7L);
        List<Team> teamList = teamMapper.selectList(teamWrapper);

        System.out.println("------街道下团队------");
        for (Team team : teamList) {
            System.out.println(team);
        }
        System.out.println("------结束------");
    }

    @Test
    public void selectTeamScoreRanking(){
        List<TeamRankingListVO> teamRankingListVOS = teamMapper.selectTeamScoreRanking(0, 10, 3);
        for (TeamRankingListVO teamRankingListVO : teamRankingListVOS) {
            System.out.println(teamRankingListVO);
        }
    }

    @Test
    public void streetScoreRankingTest(){
        int size=2000;
        Map<String, Object> streetRankIndexMap = new HashMap<>();

        int streetPage = (int) Math.ceil((double) 3 / (double) size);

        for (int i = 0; i <= streetPage; i++) {
            int index = i * size;
            // 查询
            List<TeamRankingListVO> streetRankList = new ArrayList<>();

            // 逻辑
            // 1 查找出所有街道遍历
            List<Team> list = teamMapper.selectTeams(index, size, TeamTypeEnum.STREET.getValue());
            for (Team street : list) {
                TeamRankingListVO realTeamRankingListVO = new TeamRankingListVO();
                Integer scorceTotal = 0;

                // 封装数据
                realTeamRankingListVO.setName(street.getName());
                realTeamRankingListVO.setId(street.getId());

                // 2 查找此街道下所有团队id遍历
                QueryWrapper<Team> teamWrapper = new QueryWrapper<>();
                teamWrapper.eq("deleted", DeletedEnum.NOT_DELETE.getValue())
                        .eq("type", TeamTypeEnum.TEAM.getValue())
                        .eq("parent_id", street.getId());
                List<Team> teamList = teamMapper.selectList(teamWrapper);

                for (Team team : teamList) {
                    // 3 取出每个团队的总分加数，得到街道总分
                    Integer teamScoreTotal = teamMapper.selectTeamScoreRankingById(team.getId());

                    scorceTotal += teamScoreTotal;
                }

                realTeamRankingListVO.setScoreTotal(scorceTotal);

                streetRankList.add(realTeamRankingListVO);
            }

            // 封装排序
            if (streetRankList != null && streetRankList.size() > 0) {
                // 4 比较器排序
                Collections.sort(streetRankList, new Comparator<TeamRankingListVO>() {
                    public int compareScoreTotal(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }

                    @Override
                    public int compare(TeamRankingListVO o1, TeamRankingListVO o2) {
                        return compareScoreTotal(o1.getScoreTotal(), o2.getScoreTotal());
                    }
                });

                for (int j = 0; j < streetRankList.size(); j++) {
                    // 5 遍历列表，封装排序
                    streetRankList.get(j).setTeamSort(j + 1);

                    TeamRankingListVO streetTeamRankingListVO = streetRankList.get(j);
                    Gson gson = new Gson();
                    // 存储
                    streetRankIndexMap.put(streetTeamRankingListVO.getId().toString(), gson.toJson(streetTeamRankingListVO));
                }
            }

            for (TeamRankingListVO teamRankingListVO : streetRankList) {
                System.out.println(teamRankingListVO);
            }
        }
    }

    @Test
    public void getNextLevelRankingByParentIdAndType(){
        teamMapper.getNextLevelRankingByParentIdAndType(15L);
    }
}
