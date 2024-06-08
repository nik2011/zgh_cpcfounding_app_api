package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: zhangyongfeng
 * @Date: 2021/1/22 11 : 31
 * @Description: 分享记录表
 */
@Data
@TableName("share_record")
public class ShareRecord {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 添加人
     */
    @TableField("add_user")
    private String addUser;

    /**
     * 1.摇一摇活动 2.晒年味 3.暖城活动
     */
    @TableField("type")
    private Integer type;

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
