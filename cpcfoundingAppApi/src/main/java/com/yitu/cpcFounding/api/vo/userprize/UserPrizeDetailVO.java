package com.yitu.cpcFounding.api.vo.userprize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.Valid;
import java.util.Date;

/**
 * 用户奖品明细VO
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Data
@ApiModel("用户奖品明细VO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPrizeDetailVO {

    @ApiModelProperty(value = "用户奖品记录id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "状态 0 未兑换 1 已兑换 2 已发货")
    private Integer status;

    @ApiModelProperty(value = "状态")
    private String statusStr;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "说明")
    private String explainContent;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券")
    private Integer prizeType;

    @ApiModelProperty(value = "奖品类型")
    private String prizeTypeStr;

    @ApiModelProperty(value = "奖品值（现金用到")
    private Integer prizeValue;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;


    //实物奖品字段

    @ApiModelProperty(value = "实物奖品-收货地址省")
    private String addressProvince;

    @ApiModelProperty(value = "实物奖品-收货地址市")
    private String addressCity;

    @ApiModelProperty(value = "实物奖品-收货地址区")
    private String addressArea;

    @ApiModelProperty(value = "实物奖品-收货地址详细地址")
    private String addressDetail;

    @ApiModelProperty(value = "实物奖品/酒店-收件人")
    private String receiverUser;

    @ApiModelProperty(value = "实物奖品/酒店-收件人号码")
    private String receiverUserPhone;

    @ApiModelProperty(value = "实物奖品-快递单号")
    private String expressNumber;

    //虚拟奖品字段

    @ApiModelProperty(value = "虚拟-过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date expirationDate;

    @ApiModelProperty(value = "虚拟-兑换码")
    private String exchangeCode;

    //现金字段

    @ApiModelProperty(value = "现金-银行卡号")
    private String bankCardNo;

    @ApiModelProperty(value = "现金-开户行")
    private String openCardBank;

    @ApiModelProperty(value = "现金-开户行分行")
    private String openCardBankBranch;

    //酒店字段
    @ApiModelProperty(value = "酒店-身份证")
    private String idCardNo;













}
