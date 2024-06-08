package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * 广告表
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("ad")
public class Ad {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 广告类型：1banner图2福利活动3视屏广告4惠服务5深圳工会
     */
    private Integer type;

    /**
    * 标题
    */
    @TableField("title")
    private String title;

    /**
    * 子标题
    */
    @TableField("sub_title")
    private String subTitle;

    /**
    * 图片地址
    */
    @TableField("pic_path")
    private String picPath;

    /**
    * 内容
    */
    @TableField("content")
    private String content;

    /**
    * 链接类型：1首页活动背景，2首页广告，3公众号广告，4暖城广告，5晒年味广告'
    */
    @TableField("link_type")
    private Integer linkType;

    /**
    * 链接（跳转）地址
    */
    @TableField("link_url")
    private String linkUrl;

    /**
    * 排序
    */
    @TableField("sort")
    private Integer sort;

    /**
    * 添加人
    */
    @TableField("add_user")
    private String addUser;

    /**
    * 添加时间
    */
    @TableField("add_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    /**
    * 广告开始时间
    */
    @TableField("begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginDate;

    /**
    * 广告结束时间
    */
    @TableField("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;


}