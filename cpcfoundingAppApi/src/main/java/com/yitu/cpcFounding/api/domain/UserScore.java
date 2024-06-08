package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 用户积分表
 * @author shenjun
 * @date 2021-06-03 10:25:15
 */
@Data
@TableName("user_score")
public class UserScore {
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 团队id
    */
    @TableField("team_id")
    private Long teamId;

    /**
    * 用户id
    */
    @TableField("user_id")
    private Long userId;

    /**
    * 微信昵称
    */
    @TableField("wx_user_name")
    private String wxUserName;

    /**
    * 工会手机号
    */
    @TableField("phone")
    private String phone;

    /**
    * 积分来源类型:1签到；2阅读文章；3每日答题；4每日首次分享；5邀请好友完成答题；6奖励得分
    */
    @TableField("source_type")
    private Integer sourceType;

    /**
    * 积分数
    */
    @TableField("score")
    private Integer score;

    /**
    * source_type为时1 党历史id 3 答题编号 4分享记录id 6 奖品id
    */
    @TableField("target_id")
    private String targetId;

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