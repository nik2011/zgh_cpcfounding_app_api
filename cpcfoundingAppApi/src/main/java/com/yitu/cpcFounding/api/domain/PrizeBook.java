package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 奖品库表(LsPrizeBook)表实体类
 *
 * @author chenpinjia
 * @since 2021-01-21 18:41:29
 */
@TableName("prize_book")
@EqualsAndHashCode(callSuper = true)
@Data
public class PrizeBook extends Model<PrizeBook> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动类型 1.摇一摇活动 2.晒年味 3.暖城活动
     */
    @TableField("active_type")
    private Integer activeType;
    
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    
    /**
     * 奖品类型 1.实物  2 虚拟 3 现金 4.酒店
     */
    @TableField("prize_type")
    private Integer prizeType;
    
    /**
     * 总数
     */
    @TableField("total")
    private Integer total;
    
    /**
     * 库存
     */
    @TableField("stock")
    private Integer stock;
    
    /**
     * 中奖概率开始值
     */
    @TableField("chance_begin_num")
    private Integer chanceBeginNum;
    
    /**
     * 中奖概率结束值
     */
    @TableField("chance_end_num")
    private Integer chanceEndNum;
    
    /**
     * 图片
     */
    @TableField("image_url")
    private String imageUrl;
    
    /**
     * 工会奖品类型  1普通、2工会
     */
    @TableField("member_type")
    private Integer memberType;
    
    /**
     * 有效期
     */
    @TableField("expiration_date")
    private Date expirationDate;
    
    /**
     * 说明
     */
    @TableField("explain_content")
    private String explainContent;
    
    /**
     * 备注
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
    @TableLogic
    private Integer deleted;
    
    /**
     * 奖品值（现金用到
     */
    @TableField("prize_value")
    private Integer prizeValue;
    
    /**
     * 中奖概率
     */
    @TableField("winning_probability")
    private Double winningProbability;

    /**
     * 批次号（代金券）
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 是否上架 0否 1是
     */
    private Integer isShelves;

}