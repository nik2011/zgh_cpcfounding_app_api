package com.yitu.cpcFounding.api.vo;

import lombok.Data;

/**
 * 点赞实体
 * @author shenjun
 * @date 2021/1/26 17:26
 */
@Data
public class PraiseVO {
    private long showYearId;
    private Boolean praise;
    private long userId;
    private String wxUserName;
    private String ip;
}
