package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * 配置表
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("config")
public class Config {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 类型 1 晒年味标签 2 规则 3 是否公布获奖结果 4区域
    */
    @TableField("type")
    private Integer type;

    /**
    * key
    */
    @TableField("key_id")
    private String keyId;

    /**
    * value
    */
    @TableField("key_value")
    private String keyValue;

    /**
    * 排序
    */
    @TableField("order_index")
    private Integer orderIndex;

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
    private Integer deleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}