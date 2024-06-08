package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import java.util.Date;

/**
 * 团队表
 * @Author: wangping
 * @Date: 2021/04/08 17:48
 */
@Data
@TableName("team")
public class Team {


	/**主键id*/
   	@TableId(value = "id", type = IdType.AUTO)
	private long id;

	/**团队名称*/
	@TableField("name")
	private String name;

	/**积分数*/
	@TableField("score_total")
	private Long scoreTotal;

	/**总人数*/
	@TableField("people_total")
	private Long peopleTotal;

	/**团队获奖状态 0未获奖、1已获奖*/
	@TableField("prize_status")
	private Integer prizeStatus;

	/**ip*/
	@TableField("ip")
	private String ip;

	/**创建人*/
	@TableField("add_user")
	private String addUser;

	/**创建时间*/
	@TableField("add_date")
	private Date addDate;

	/**更新人*/
	@TableField("modify_user")
	private String modifyUser;

	/**更新时间*/
	@TableField("modify_date")
	private Date modifyDate;

	/**删除状态（0未删除 1已删除）*/
	@TableField("deleted")
	private Integer deleted;

	/**
	 * 父节点id
	 */
	@TableField("parent_id" )
	private long parentId;

	/**
	 * 1 片区 2 街道 3 团队
	 */
	@TableField("type" )
	private int type;

}
