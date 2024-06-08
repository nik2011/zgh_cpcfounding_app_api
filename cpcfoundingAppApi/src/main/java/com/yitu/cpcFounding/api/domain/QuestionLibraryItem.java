package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 题库选项表
 * @author shenjun
 * @date 2021-02-24 10:15:12
 */
@Data
@TableName("question_library_item")
public class QuestionLibraryItem {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 题目id
    */
    @TableField("question_id")
    private Long questionId;

    /**
    * 名称
    */
    @TableField("name")
    private String name;

    /**
    * 编号 a,b,c,d
    */
    @TableField("number")
    private String number;

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


}