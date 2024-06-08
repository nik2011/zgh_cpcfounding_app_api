package com.yitu.cpcFounding.api.utils.yidun;

import lombok.Data;

import java.util.List;

/**
 * 易盾返回结果
 * @author shenjun
 * @date 2021/1/23 10:33
 */
@Data
public class DunResult {
    /**
     * 本次请求数据标识，可以根据该标识查询数据最新结果
     */
    public String taskId;
    /**
     * 分类信息
     */
    public ActionEnum action;

    /**
     * 标签信息
     */
    public List<Labels> labels;


}
