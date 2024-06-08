package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserDetailVO;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用户表服务
 *
 * @author yaoyanhua
 * @date 2021-01-21 11:47:39
 */
public interface UserService extends IService<User> {
    /**
     * 微信登录
     *
     * @param code
     * @param avatarUrl
     * @param nickName
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    JsonResult<UserVO> loginByWeChat(String code, String avatarUrl, String nickName,
                                     HttpServletRequest req) throws IOException;

    /**
     * 获取用户手机号通过微信授权,并且认证
     *
     * @param code
     * @param encryptedDataBase64
     * @param ivBase64
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    JsonResult<String> getPhoneAndAuthByWxAuth(String code, String encryptedDataBase64, String ivBase64,
                                        HttpServletRequest req) throws IOException;

    /**
     * 获取用户手机号通过微信授权,并且认证
     *
     * @param code
     * @param encryptedDataBase64
     * @param ivBase64
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    JsonResult<String> getPhoneByWxAuth(String code, String encryptedDataBase64, String ivBase64,
                                        HttpServletRequest req) throws IOException;

    /**
     * 根据工会手机号获取用户信息
     *
     * @param phone
     * @return void
     * @author jxc
     * @date 2021/1/25 18:26
     */
    User getUserByPhone(@Param("phone") String phone);


    /**
     * 工会认证成功修改
     *
     * @param userId
     * @param phone
     * @param memberState
     * @return int
     * @author jxc
     * @date 2021/1/26 9:04
     */
    boolean updateUserById(long userId, String phone, int memberState);

    /**
     * 更新用户位置信息
     *
     * @param area 区域
     * @param lng  经度
     * @param lat  维度
     * @return JsonResult<java.lang.String>
     * @author pangshihe
     * @date 2021/1/29 11:51
     */
    JsonResult<String> updateUserLocation(String area, String lng, String lat);

    /**
     * 查询总记录数
     *
     * @param
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/8 10:11
     */
    Integer findCount();

    /**
     * 获取我的页面详情
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserDetailVO>
     * @author qixinyi
     * @date 2021/6/10 14:33
     */
    JsonResult<UserDetailVO> findUserDetails();

    /**
     * 根据id查找用户
     *
     * @param id
     * @return com.yitu.cpcFounding.api.domain.User
     * @author qixinyi
     * @date 2021/6/11 16:52
     */
    User findUserById(Long id);

    /**
     * 刷新会员认证
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author shenjun
     * @date 2021-6-15 17:48:20
     */
    JsonResult refreshMember();

    /**
     * 获取用户信息
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author shenjun
     * @date 2021/1/21 21:55
     */
    JsonResult<UserVO> getUserInfo();
}
