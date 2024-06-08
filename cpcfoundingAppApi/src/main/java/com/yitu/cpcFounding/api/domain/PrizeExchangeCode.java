package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 奖品兑换码
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@TableName("prize_exchange_code")
public class PrizeExchangeCode {
    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 奖品id
    */
    @TableField("prize_id")
    private Integer prizeId;

    /**
    * 兑换码
    */
    @TableField("exchange_code")
    private String exchangeCode;

    /**
    * 状态 0 未使用  1 已使用  2 已失效
    */
    @TableField("status")
    private Integer status;

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