package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 工会会员表
 * @author shenjun
 * @date 2021-02-04 17:48:48
 */
@Data
@TableName("gh_sm_basic_situation")
public class GhSmBasicSituation {
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 身份证号
    */
    @TableField("id_num")
    private String idNum;

    /**
    * 手机号码
    */
    @TableField("contact_phone")
    private String contactPhone;

    /**
    * 会员卡号
    */
    @TableField("member_card")
    private String memberCard;

    /**
    * 会员状态
    */
    @TableField("membership_status")
    private Integer membershipStatus;

    /**
    * 添加时间
    */
    @TableField("add_date")
    private Date addDate;


}