package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 用户微信通知表
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("notice")
public class Notice {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 微信id
    */
    @TableField("wx_id")
    private String wxId;

    /**
    * 用户id
    */
    @TableField("user_id")
    private Integer userId;

    /**
    * 标题
    */
    @TableField("title")
    private String title;

    /**
    * 通知内容
    */
    @TableField("content")
    private String content;

    /**
    * 类型：1首页，2晒年味，3暖城
    */
    @TableField("type")
    private Integer type;

    /**
    * 链接地址
    */
    @TableField("link_url")
    private String linkUrl;

    /**
    * 消息发送时间
    */
    @TableField("send_time")
    private Date sendTime;

    /**
    * 备注
    */
    @TableField("remark")
    private String remark;

    /**
    * 添加人
    */
    @TableField("add_user")
    private String addUser;

    /**
    * 添加时间
    */
    @TableField("add_date")
    private Date addDate;

    /**
    * 修改人
    */
    @TableField("modify_user")
    private String modifyUser;

    /**
    * 修改时间
    */
    @TableField("modify_date")
    private Date modifyDate;

    /**
    * 添加ip
    */
    @TableField("ip")
    private String ip;

    /**
    * 是否删除：0未删除、1已删除
    */
    @TableField("deleted")
    private Integer deleted;


}