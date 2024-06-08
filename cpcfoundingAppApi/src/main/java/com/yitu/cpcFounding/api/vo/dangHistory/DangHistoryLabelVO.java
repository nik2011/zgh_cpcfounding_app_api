package com.yitu.cpcFounding.api.vo.dangHistory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 党史标签VO
 * @return
 * @author wangping
 * @date 2021/6/4 14:35
 */
@Data
@ApiModel(value="获奖党史标签",description="获奖党史标签")
public class DangHistoryLabelVO implements Serializable {

    private static final long serialVersionUID = 6185041234228279496L;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private String lableId;

    /**
    * 标签名称
    */
    @ApiModelProperty("标签名称")
    private String lableValue;

}