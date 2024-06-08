package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 用户答题表
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
@Data
@TableName("user_answer")
public class UserAnswer {
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
    * 答案
    */
    @TableField("answer")
    private String answer;

    /**
    * 答题编号 唯一的随机码，每次答题时生成
    */
    @TableField("answer_number")
    private String answerNumber;

    /**
    * 是否正确
    */
    @TableField("is_correct")
    private Integer isCorrect;

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
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

}