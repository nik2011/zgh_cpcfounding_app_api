package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 党史
 * @author shenjun
 * @date 2021-06-03 10:24:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dang_history")
public class DangHistory extends Model<DangHistory> {
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 标签id
    */
    @TableField("lable_id")
    private String lableId;

    /**
    * 类型：1 文章 2 视频
    */
    @TableField("type")
    private Integer type;

    /**
    * 封面类型：1无图 2单图 3三图 4 宽图
    */
    @TableField("cover_type")
    private Integer coverType;

    /**
    * 标题
    */
    @TableField("title")
    private String title;

    /**
    * 列表图片地址集合
    */
    @TableField("url")
    private String url;

    /**
    * 内容
    */
    @TableField("content")
    private String content;

    /**
    * 排序 012345排序
    */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否置顶 1置顶 0未置顶
     */
    @TableField("top")
    private Integer top;

    /**
     * 是否推荐 1推荐 0未推荐
     */
    @TableField("recommend")
    private Integer recommend;


    /**
    * 访问量
    */
    @TableField("visit_num")
    private Integer visitNum;

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
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;


}