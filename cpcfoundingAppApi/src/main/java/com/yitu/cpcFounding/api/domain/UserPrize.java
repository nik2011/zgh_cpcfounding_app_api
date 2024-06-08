package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户奖品表(LsUserPrize)表实体类
 *
 * @author qinmingtong
 * @date 2021/1/22 9:59
 */
@TableName("user_prize")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPrize extends Model<UserPrize> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;
    
    /**
     * 奖品id
     */
    @TableField("prize_id")
    private Long prizeId;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    
    /**
     * 状态 0 未兑换 1 已兑换 2 已发货
     */
    @TableField("status")
    private Integer status;
    
    /**
     * 收货地址省
     */
    @TableField("address_province")
    private String addressProvince;
    
    /**
     * 收货地址市
     */
    @TableField("address_city")
    private String addressCity;
    
    /**
     * 收货地址区
     */
    @TableField("address_area")
    private String addressArea;
    
    /**
     * 收货地址详细地址
     */
    @TableField("address_detail")
    private String addressDetail;
    
    /**
     * 收件人 
     */
    @TableField("receiver_user")
    private String receiverUser;
    
    /**
     * 收件人号码
     */
    @TableField("receiver_user_phone")
    private String receiverUserPhone;
    
    /**
     * 快递单号
     */
    @TableField("express_number")
    private String expressNumber;
    
    /**
     * 快递名称
     */
    @TableField("express_name")
    private String expressName;
    
    /**
     * 领取时间
     */
    @TableField("get_time")
    private Date getTime;
    
    /**
     * 兑换码
     */
    @TableField("exchange_code")
    private String exchangeCode;
    
    /**
     * 后台发货时间
     */
    @TableField("deliver_time")
    private Date deliverTime;
    
    /**
     * 银行卡号
     */
    @TableField("bank_card_no")
    private String bankCardNo;
    
    /**
     * 开户行
     */
    @TableField("open_card_bank")
    private String openCardBank;
    
    /**
     * 开户行分行
     */
    @TableField("open_card_bank_branch")
    private String openCardBankBranch;
    
    /**
     * 收件人身份证号码
     */
    @TableField("receiver_user_id_card_no")
    private String receiverUserIdCardNo;
    
    /**
     * 添加用户
     */
    @TableField("add_user")
    private String addUser;
    
    /**
     * 添加时间
     */
    @TableField("add_date")
    private Date addDate;
    
    /**
     * 修改用户姓名
     */
    @TableField("modify_user")
    private String modifyUser;
    
    /**
     * 修改时间
     */
    @TableField("modify_date")
    private Date modifyDate;
    
    /**
     * Ip地址
     */
    @TableField("ip")
    private String ip;
    
    /**
     * 是否删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    

}