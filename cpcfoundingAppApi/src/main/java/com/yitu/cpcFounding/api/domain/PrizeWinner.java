package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 获奖用户表
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("prize_winner")
public class PrizeWinner {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 用户表id
    */
    @TableField("user_id")
    private Integer userId;

    /**
    * 用户名
    */
    @TableField("user_name")
    private String userName;

    /**
    * 类型：0热力、1晒年味
    */
    @TableField("type")
    private Integer type;

    /**
    * 点赞数热力数
    */
    @TableField("like_num")
    private Long likeNum;

    /**
     * 区域id
     */
    @TableField("area_id")
    private Integer areaId;

    /**
    * 排名
    */
    @TableField("order_index")
    private Long orderIndex;

    /**
     * 注释
     */
    @TableField("remark")
    private String remark;

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
     * 奖品id
     */
    @TableField("prize_id")
    private Integer prizeId;

    /**
     * 是否发奖：0未发奖、1已发奖
     */
    @TableField("status")
    private Integer status;


}