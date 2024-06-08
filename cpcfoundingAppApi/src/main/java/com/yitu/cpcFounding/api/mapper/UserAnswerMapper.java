package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.UserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户答题表Mapper
 *
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
@Mapper
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    /**
     * @param
     * @return long
     * @Description: 获取答题次数
     * @author liuzhaowei
     * @date 2021/4/10
     */
    long getUserAnswerCount();

    /**
     * @param
     * @return long
     * @Description: 获取答题最近人的头像列表
     * @author liuzhaowei
     * @date 2021/4/10
     */
    List<String> getUserAnswerHeadPic();

    /**
     * 获取用户每日答题次数
     *
     * @param id 用户id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/7/2 16:02
     */
    Integer selectTimesOfDayByUserId(@Param("id") Long id);
}
