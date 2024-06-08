package com.yitu.cpcFounding.api.vo.prizeBook;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * 用户摇奖消费日志表实体
 * @author shenjun
 * @date 2021-01-21 12:01:28
 */
@Data
@ApiModel("用户摇奖消费日志表实体")
public class LsShakeTakeLogVO {
    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Integer id;

    /**
    * 类型：1摇一摇、2分享
    */
    @ApiModelProperty("类型：1摇一摇、2分享")
    private Integer type;

    /**
    * 用户奖品表ID
    */
    @ApiModelProperty("用户奖品表ID")
    private Integer userPrizeId;

    /**
    * 用户ID
    */
    @ApiModelProperty("用户ID")
    private Integer userId;

    /**
    * 用户名
    */
    @ApiModelProperty("用户名")
    private String userName;

    /**
    * 摇一摇次数加减
    */
    @ApiModelProperty("摇一摇次数加减")
    private Integer inOrOut;

    /**
    * 是否删除：0否、1是
    */
    @ApiModelProperty("是否删除：0否、1是")
    private Integer deleted;

    /**
    * 添加用户
    */
    @ApiModelProperty("添加用户")
    private String addUser;

    /**
    * 添加时间
    */
    @ApiModelProperty("添加时间")
    private Date addDate;

    /**
    * 修改用户姓名
    */
    @ApiModelProperty("修改用户姓名")
    private String modifyUser;

    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间")
    private Date modifyDate;

    /**
    * Ip地址
    */
    @ApiModelProperty("Ip地址")
    private String ip;


    /**
     * 奖品id
     */
    @ApiModelProperty("奖品id")
    private long prizeId;

    /**
     * 奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券
     */
    @ApiModelProperty("奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券")
    private Integer prizeType;

    /**
     * 奖品值
     */
    @ApiModelProperty("奖品值")
    private long prizeValue;

}
