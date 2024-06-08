package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.QuestionLibrary;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题库表Mapper
 * @author shenjun
 * @date 2021-02-24 10:15:39
 */
@Mapper
public interface QuestionLibraryMapper extends BaseMapper<QuestionLibrary>{

    /**
    * @Description: 随机获取答题列表
    * @param count 数量
    * @author liuzhaowei
    * @date 2021/2/24
    * @return java.util.List<LsQuestionLibraryVO>
     */
   List<LsQuestionLibraryVO> getRandomList(@Param("count")int count);
}
