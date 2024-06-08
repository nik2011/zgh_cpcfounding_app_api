package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户表
 *
 * @author shenjun
 * @date 2021-01-21 11:02:19
 */
@Data
@TableName("user")
public class User {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    /**
     * 微信号id
     */
    @TableField("wx_openid")
    private String wxOpenid;

    /**
     * 微信昵称
     */
    @TableField("wx_user_name")
    private String wxUserName;

    /**
     * 头像图片地址
     */
    @TableField("wx_head_pic")
    private String wxHeadPic;

    /**
     * 微信手机号
     */
    @TableField("wx_phone")
    private String wxPhone;

    /**
     * 工会手机号
     */
    @TableField("phone")
    private String phone;


    /**
     * 会员状态 1 工会会员 0不是会员
     */
    @TableField("member_state")
    private int memberState;

    /**
     * 添加时间
     */
    @TableField("add_date")
    private Date addDate;

    /**
     * 添加人
     */
    @TableField("add_user")
    private String addUser;

    /**
     * 修改时间
     */
    @TableField("modify_date")
    private Date modifyDate;

    /**
     * 修改人
     */
    @TableField("modify_user")
    private String modifyUser;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    /**
     * 是否删除：0未删除、1已删除
     */
    @TableField("deleted")
    private int deleted;

    /**
     * 登录时间
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * 积分总数
     */
    @TableField("score_total")
    private int scoreTotal;

    /**
     * 团队id
     */
    @TableField("team_id")
    private int teamId;
}