package com.yitu.cpcFounding.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志
 *
 * @author qinmingtong
 * @date 2021/1/27 9:27
 */
@TableName("operation_log")
@EqualsAndHashCode(callSuper = true)
@Data
public class OperationLog extends Model<OperationLog> {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联表外id
     */
    private Long relateId;

    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 添加时间
     */
    private LocalDateTime addDate;

    /**
     * 添加人
     */
    private String addUser;

    /**
     * 修改时间
     */
    private LocalDateTime modifyDate;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * ip
     */
    private String ip;

}
