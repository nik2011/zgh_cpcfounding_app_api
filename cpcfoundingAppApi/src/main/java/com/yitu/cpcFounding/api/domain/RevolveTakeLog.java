package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 用户摇奖消费日志表
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("shake_take_log")
public class RevolveTakeLog {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 类型：1摇一摇、2分享
    */
    @TableField("type")
    private Integer type;

    /**
    * 用户奖品表ID
    */
    @TableField("user_prize_id")
    private Integer userPrizeId;

    /**
    * 用户ID
    */
    @TableField("user_id")
    private Integer userId;

    /**
    * 用户名
    */
    @TableField("user_name")
    private String userName;

    /**
    * 摇一摇次数加减
    */
    @TableField("in_or_out")
    private Integer inOrOut;

    /**
    * 是否删除：0否、1是
    */
    @TableField("deleted")
    private Integer deleted;

    /**
    * 添加用户
    */
    @TableField("add_user")
    private String addUser;

    /**
    * 添加时间
    */
    @TableField("add_date")
    private Date addDate;

    /**
    * 修改用户姓名
    */
    @TableField("modify_user")
    private String modifyUser;

    /**
    * 修改时间
    */
    @TableField("modify_date")
    private Date modifyDate;

    /**
    * Ip地址
    */
    @TableField("ip")
    private String ip;

    /**
     * 摇一摇次数加减
     */
    private Integer score;


}