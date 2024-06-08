package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 用户表Mapper
 *
 * @author shenjun
 * @date 2021-01-21 11:02:19
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * @param userId
     * @return int
     * @Description: 用户摇一摇次数减一
     * @author liuzhaowei
     * @date 2021/1/21
     */
    int minusYaoCount(@Param("userId") long userId);

    /**
     * @param userId
     * @return int
     * @Description:用户摇一摇次数加一
     * @author liuzhaowei
     * @date 2021/1/21
     */
    int addYaoCount(@Param("userId") long userId);

    /**
     * 修改用户登录信息
     *
     * @return int
     * @author yaoyanhua
     * @date 2021/1/21 18:16
     */
    int updateUserLogin(User user);

    /**
     * 修改用户微信手机号
     *
     * @return int
     * @author yaoyanhua
     * @date 2021/1/21 18:16
     */
    int updateUserWxPhone(@Param("wxOpenid") String wxOpenid, @Param("wxPhone") String wxPhone);

    /**
     * 根据工会手机号获取用户信息
     *
     * @param phone
     * @return LsUser
     * @author jxc
     * @date 2021/1/25 18:28
     */
    User getUserByPhone(@Param("phone") String phone);

    /**
     * 工会认证成功修改，并把手机号覆盖为工会手机号
     *
     * @param userId
     * @param phone
     * @param memberState
     * @return int
     * @author jxc
     * @date 2021/1/26 9:04
     */
    int updateUserById(@Param("userId") long userId, @Param("phone") String phone, @Param("memberState") int memberState);

    /**
     * 更新审核不通过次数
     *
     * @param userId
     * @return void
     * @author shenjun
     * @date 2021/1/28 18:50
     */
    void updateAuditFailNum(@Param("userId") long userId);

    /**
     * 更新用户位置信息
     *
     * @param userId 用户id
     * @param area   区域
     * @param lng    经度
     * @param lat    维度
     * @return int
     * @author pangshihe
     * @date 2021/1/29 11:52
     */
    int updateUserLocation(@Param("userId") long userId, @Param("area") String area, @Param("lng") String lng, @Param("lat") String lat);

    /**
     *
     *  送奖章查询个人分页数据
     * @param userPage
     * @param wxUserName
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yitu.cpcFounding.api.domain.User>
     * @author wangping
     * @date 2021/4/23 11:12
     */
    Page<User> userPageList(Page<User> userPage,@Param("wxUserName") String wxUserName);

    /**
     * 查询用户奖章总数
     * @return
     */
    int userMedalCount();
}
