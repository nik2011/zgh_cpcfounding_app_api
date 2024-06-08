package com.yitu.cpcFounding.api.vo.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Description  
 * @Author  wangping
 * @Date 2021-04-21
 */
@ApiModel("个人团队信息")
@Data
public class TeamInfoVO implements Serializable {

	private static final long serialVersionUID =  8379607000502538529L;

	private long id;

	/**
	 * 团队名称
	 */
   	@ApiModelProperty("团队名称" )
	private String name;

	/**
	 * 积分数
	 */
   	@ApiModelProperty("积分数" )
	private Long scoreTotal;

	/**
	 * 总人数
	 */
   	@ApiModelProperty("总人数" )
	private Long peopleTotal;

	/**
	 * 团队获奖状态 0未获奖、1已获奖
	 */
	@ApiModelProperty("团队获奖状态 0未获奖、1已获奖" )
	private int prizeStatus;


	/**当前团队所在排行*/
	@ApiModelProperty(value = "当前团队所在排行")
	private Integer teamSort;
}
