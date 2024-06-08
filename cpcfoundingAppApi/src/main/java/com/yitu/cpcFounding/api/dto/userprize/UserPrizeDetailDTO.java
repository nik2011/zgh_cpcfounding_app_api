package com.yitu.cpcFounding.api.dto.userprize;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户奖品领奖DTO
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Data
@ApiModel("用户奖品明细保存DTO")
public class UserPrizeDetailDTO {

    @ApiModelProperty(value = "用户奖品记录id")
    @NotNull(message = "用户奖品记录id不能为空")
    private Long id;

    //实物奖品字段

    @ApiModelProperty(value = "实物奖品-收货地址省")
    @Size(min = 0, max = 10, message = "省份过长")
    private String addressProvince;

    @ApiModelProperty(value = "实物奖品-收货地址市")
    @Size(min = 0, max = 10, message = "市过长")
    private String addressCity;

    @ApiModelProperty(value = "实物奖品-收货地址区")
    @Size(min = 0, max = 10, message = "省份过长")
    private String addressArea;

    @ApiModelProperty(value = "实物奖品-收货地址详细地址")
    @Size(min = 0, max = 50, message = "详细地址过长")
    private String addressDetail;

    @ApiModelProperty(value = "实物奖品/酒店-收件人")
    @Size(min = 0, max = 10, message = "收件人过长")
    private String receiverUserName;

    @ApiModelProperty(value = "实物奖品/酒店-收件人号码")
    private String receiverUserPhone;

    //现金字段

    @ApiModelProperty(value = "现金-银行卡号")
    @Size(min = 0, max = 100, message = "银行卡号过长")
    private String bankCardNo;

    @ApiModelProperty(value = "现金-开户行")
    @Size(min = 0, max = 20, message = "开户行过长")
    private String openCardBank;

    @ApiModelProperty(value = "现金-开户行分行")
    @Size(min = 0, max = 10, message = "开户行分行过长")
    private String openCardBankBranch;

    //酒店字段

    @ApiModelProperty(value = "酒店-身份证")
    @Size(min = 0, max = 100, message = "身份证号过长")
    private String userIdCardNo;



}
