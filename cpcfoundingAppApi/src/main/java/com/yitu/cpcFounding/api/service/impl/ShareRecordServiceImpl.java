package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.ShareRecord;
import com.yitu.cpcFounding.api.enums.ActiveEntranceEnum;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.mapper.ShareRecordMapper;
import com.yitu.cpcFounding.api.service.ShareRecordService;
import com.yitu.cpcFounding.api.utils.EnumUtil;
import com.yitu.cpcFounding.api.utils.IpUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Auther: zhangyongfeng
 * @Date: 2021/1/22 11 : 57
 * @Description:
 */
@Service
public class ShareRecordServiceImpl extends ServiceImpl<ShareRecordMapper, ShareRecord> implements ShareRecordService {

    @Resource
    HttpServletRequest request;

    @Resource
    LoginUserUtil loginUserUtil;


    @Override
    public JsonResult addShareRecord(Integer type) {

        UserVO loginUser = loginUserUtil.getLoginUser();

        // 判断是否分享类型在枚举类里
        if (EnumUtil.getDesc(ActiveEntranceEnum.class, type) == null) {
            return JsonResult.build(ResponseCode.UNVALIDATED.getCode(), ResponseCode.UNVALIDATED.getMessage());
        }
        ShareRecord shareRecord = new ShareRecord();
        shareRecord.setUserId(loginUser.getId());
        shareRecord.setType(type);
        shareRecord.setAddUser(loginUser.getWxUserName() == null ? "system" : loginUser.getWxUserName());
        shareRecord.setAddDate(new Date());
        shareRecord.setIp(IpUtil.getIpAdrress(request));
        shareRecord.setDeleted(DeletedEnum.NOT_DELETE.getValue());
        int insert = baseMapper.insert(shareRecord);
        if (insert > 0) {
            return JsonResult.ok();
        } else {
            return JsonResult.fail("添加失败");
        }
    }

}
