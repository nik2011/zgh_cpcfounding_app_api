package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 周积分排名
 * @author shenjun
 * @date 2021-06-03 10:25:23
 */
@Data
@TableName("week_score_rank")
public class WeekScoreRank {
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 用户id
    */
    @TableField("target_id")
    private Integer targetId;

    /**
    * 1 用户周榜 2 团队周榜 3 答题榜周榜
    */
    @TableField("type")
    private Integer type;

    /**
    * 积分
    */
    @TableField("score")
    private Integer score;

    /**
    * 排名
    */
    @TableField("rank")
    private Integer rank;

    /**
    * 周开始时间
    */
    @TableField("begin_date")
    private Date beginDate;

    /**
    * 周结束时间
    */
    @TableField("end_date")
    private Date endDate;

    /**
    * ip
    */
    @TableField("ip")
    private String ip;

    /**
    * 创建人
    */
    @TableField("add_user")
    private String addUser;

    /**
    * 创建时间
    */
    @TableField("add_date")
    private Date addDate;

    /**
    * 更新人
    */
    @TableField("modify_user")
    private String modifyUser;

    /**
    * 更新时间
    */
    @TableField("modify_date")
    private Date modifyDate;

    /**
    * 删除状态（0未删除 1已删除）
    */
    @TableField("deleted")
    private Integer deleted;


}