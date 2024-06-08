package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.PrizeBook;
import com.yitu.cpcFounding.api.domain.UserPrize;
import com.yitu.cpcFounding.api.dto.userprize.UserPrizeDetailDTO;
import com.yitu.cpcFounding.api.enums.ActiveEntranceEnum;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.enums.GetStatusEnum;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.enums.prize.PrizeTypeEnum;
import com.yitu.cpcFounding.api.enums.prize.UserPrizeStatusEnum;
import com.yitu.cpcFounding.api.mapper.PrizeBookMapper;
import com.yitu.cpcFounding.api.mapper.UserPrizeMapper;
import com.yitu.cpcFounding.api.service.UserPrizeService;
import com.yitu.cpcFounding.api.utils.*;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeDetailVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeListVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户奖品表服务实现
 *
 * @author shenjun
 * @date 2021-01-21 11:47:39
 */
@Service
public class UserPrizeServiceImpl implements UserPrizeService {

    @Resource
    private UserPrizeMapper userPrizeMapper;

    @Resource
    private PrizeBookMapper prizeBookMapper;
    @Resource
    private HttpServletRequest request;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Value("${exchangeCode.secret}")
    private String secret;

    /**
     *
     * 用户奖品列表
     *
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author chenpinjia
     * @date 2021/1/21 15:04
     */
    @Override
    public JsonResult<List<UserPrizeListVO>> getUserPrizeList() {
        Long userId = loginUserUtil.getUserId();

        LambdaQueryWrapper<UserPrize> userPrizeQw = new LambdaQueryWrapper<>();
        userPrizeQw.eq(UserPrize::getUserId, userId);
        userPrizeQw.eq(UserPrize::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        List<UserPrize> userPrizeList = userPrizeMapper.selectList(userPrizeQw);
        if (CollectionUtils.isEmpty(userPrizeList)) {
            return JsonResult.ok();
        }
        List<Long> prizeIdList = userPrizeList.stream().map(UserPrize::getPrizeId).collect(Collectors.toList());

        QueryWrapper<PrizeBook> prizeQw = new QueryWrapper<>();
        prizeQw.in("id", prizeIdList);
//        prizeQw.notIn("prize_type", PrizeTypeEnum.ADD_COUNT.getCode(), PrizeTypeEnum.NOTHING.getCode());
        List<PrizeBook> prizeList = prizeBookMapper.selectList(prizeQw);
        Map<Long, PrizeBook> prizeMap = prizeList.stream().collect(Collectors.toMap(PrizeBook::getId, p -> p));

        List<UserPrizeVO> userPrizeVOList = userPrizeList.stream().map(userPrize -> {
            UserPrizeVO vo = new UserPrizeVO();
            vo.setId(userPrize.getId());
            vo.setOrderNo(userPrize.getOrderNo());
            vo.setUserId(userPrize.getUserId());
            vo.setPrizeId(userPrize.getPrizeId());
            vo.setAddDate(userPrize.getAddDate());
            PrizeBook prizeBook = prizeMap.get(userPrize.getPrizeId());
            if (prizeBook == null) {
                return vo;
            }
            vo.setActiveType(prizeBook.getActiveType());
            vo.setActiveTypeStr(EnumUtil.getDesc(ActiveEntranceEnum.class,prizeBook.getActiveType()));
            vo.setPrizeType(prizeBook.getPrizeType());
            vo.setTitle(prizeBook.getTitle());
            vo.setImageUrl(HttpUtil.getNetResourcePath(request,prizeBook.getImageUrl()));
            vo.setGetStatus(GetStatusEnum.getGetStatus(userPrize.getStatus(), userPrize.getAddDate()));
            return vo;
        }).filter(userPrizeVO -> {
            Integer prizeType = userPrizeVO.getPrizeType();
            return prizeType != PrizeTypeEnum.ADD_COUNT.getCode() && prizeType!= PrizeTypeEnum.NOTHING.getCode()
                    && prizeType!= PrizeTypeEnum.SCORE.getCode();
        }).collect(Collectors.toList());

        LinkedHashMap<String, List<UserPrizeVO>> resultMap = userPrizeVOList.stream().sorted(Comparator.comparing(UserPrizeVO::getAddDate).reversed())
                .collect(Collectors.groupingBy(
                        prize -> DateFormatUtils.format(prize.getAddDate(), "yyyy年MM月dd日"),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        List<UserPrizeListVO> resultList = new ArrayList<>();
        resultMap.forEach((date, list) -> {
            UserPrizeListVO resultVO = new UserPrizeListVO();
            resultVO.setDate(date);
            resultVO.setPrize(list);
            resultList.add(resultVO);
        });
        return JsonResult.ok(resultList);
    }

    /**
     * 用户奖品明细
     *
     * @param userPrizeId 用户奖品id
     * @return JsonResult<UserPrizeDetailVO>
     * @author chenpinjia
     * @date 2021/1/21 16:52
     */
    @Override
    public JsonResult<UserPrizeDetailVO> getUserPrizeDetail(Long userPrizeId) {
        UserPrize userPrize = userPrizeMapper.selectById(userPrizeId);
        if (userPrize == null){
            return JsonResult.fail("不存在的中奖记录");
        }
        long thisUserId = loginUserUtil.getUserId();
        if (userPrize.getUserId() != thisUserId){
            return JsonResult.fail("并非属于当前用户的奖品");
        }
        Integer getStatus = GetStatusEnum.getGetStatus(userPrize.getStatus(), userPrize.getAddDate());
        if(getStatus== null || GetStatusEnum.AVAILABLE.getValue() == getStatus){
            return JsonResult.fail("奖品已失效");
        }
        PrizeBook prize = prizeBookMapper.selectById(userPrize.getPrizeId());
        UserPrizeDetailVO vo = new UserPrizeDetailVO();
        vo.setId(userPrize.getId());
        vo.setStatus(userPrize.getStatus());
        vo.setStatusStr(UserPrizeStatusEnum.getName(userPrize.getStatus()));
        vo.setTitle(prize.getTitle());
        vo.setImageUrl(HttpUtil.getNetResourcePath(request,prize.getImageUrl()));
        vo.setOrderNo(userPrize.getOrderNo());
        vo.setPrizeType(prize.getPrizeType());
        vo.setExplainContent(prize.getExplainContent());
        vo.setRemark(prize.getRemark());
        vo.setAddressProvince(userPrize.getAddressProvince());
        vo.setAddressCity(userPrize.getAddressCity());
        vo.setAddressArea(userPrize.getAddressArea());
        vo.setAddressDetail(userPrize.getAddressDetail());
        vo.setReceiverUser(StringUtil.userNameMask(userPrize.getReceiverUser()));
        vo.setReceiverUserPhone(StringUtil.phoneMask(userPrize.getReceiverUserPhone()));
        vo.setExpressNumber(userPrize.getExpressNumber());
        vo.setExpirationDate(prize.getExpirationDate());

        //解密
        String exchangeCodeEncrypt = userPrize.getExchangeCode();
        if (!StringUtils.isBlank(exchangeCodeEncrypt)){
            String showExchangeCode = AESUtils.decrypt(exchangeCodeEncrypt, secret);
            vo.setExchangeCode(showExchangeCode);
        }
        vo.setBankCardNo(userPrize.getBankCardNo());
        vo.setOpenCardBank(userPrize.getOpenCardBank());
        vo.setOpenCardBankBranch(userPrize.getOpenCardBankBranch());
        vo.setIdCardNo(StringUtil.stringMask(userPrize.getReceiverUserIdCardNo()));
        vo.setPrizeValue(prize.getPrizeValue());
        
        //虚拟奖品 查看后状态改为已兑换
        if (userPrize.getStatus() == UserPrizeStatusEnum.UN_EXCHANGE.getCode() &&
                prize.getPrizeType() == PrizeTypeEnum.VIRTUAL.getCode()) {
            userPrize.setStatus(UserPrizeStatusEnum.EXCHANGED.getCode());
            userPrize.setGetTime(new Date());
            userPrize.updateById();
        }
        return JsonResult.ok(vo);
    }


    /**
     * 保存用户领奖信息
     *
     * @param saveInfo 保存的明细
     * @return JsonResult
     * @author chenpinjia
     * @date 2021/1/21 17:42
     */
    @Override
    public JsonResult saveUserPrizeDetailInfo(UserPrizeDetailDTO saveInfo) {
        UserPrize userPrize = userPrizeMapper.selectById(saveInfo.getId());
        if (userPrize == null) {
            return JsonResult.fail("不存在的用户奖品");
        }
        Integer getStatus = GetStatusEnum.getGetStatus(userPrize.getStatus(), userPrize.getAddDate());
        if(getStatus== null || GetStatusEnum.AVAILABLE.getValue() == getStatus){
            return JsonResult.fail("奖品已失效");
        }
        PrizeBook prize = prizeBookMapper.selectById(userPrize.getPrizeId());
        //各个类型的字段校验
        JsonResult checkRe = this.checkSaveInfo(saveInfo, prize.getPrizeType());
        if (checkRe.getStatus() != ResponseCode.OK.getCode()) {
            return checkRe;
        }
        UserPrize update = new UserPrize();
        Date now = new Date();
        update.setId(userPrize.getId());
        update.setModifyDate(now);
        update.setStatus(UserPrizeStatusEnum.EXCHANGED.getCode());
        update.setGetTime(now);
        UserVO user = loginUserUtil.getLoginUser();
        if (user == null) {
            return JsonResult.build(ResponseCode.UNAUTHORIZED, "请先登录");
        }
        if (userPrize.getUserId().longValue() != user.getId().longValue()){
            return JsonResult.fail("不属于当前用户的奖品");
        }

        update.setModifyUser(user.getWxUserName());
        //实物奖品
        if (prize.getPrizeType() == PrizeTypeEnum.MATERIAL.getCode()) {
            update.setAddressProvince(saveInfo.getAddressProvince());
            update.setAddressCity(saveInfo.getAddressCity());
            update.setAddressArea(saveInfo.getAddressArea());
            update.setAddressDetail(saveInfo.getAddressDetail());
            update.setReceiverUser(saveInfo.getReceiverUserName());
            update.setReceiverUserPhone(saveInfo.getReceiverUserPhone());
        }
        //现金奖品
        if (prize.getPrizeType() == PrizeTypeEnum.CASH.getCode()) {
            update.setBankCardNo(saveInfo.getBankCardNo());
            update.setOpenCardBank(saveInfo.getOpenCardBank());
            update.setOpenCardBankBranch(saveInfo.getOpenCardBankBranch());
            update.setReceiverUser(saveInfo.getReceiverUserName());
            update.setReceiverUserPhone(saveInfo.getReceiverUserPhone());
        }
        //酒店奖品
        if (prize.getPrizeType() == PrizeTypeEnum.HOTEL.getCode()) {
            update.setReceiverUserIdCardNo(saveInfo.getUserIdCardNo());
            update.setReceiverUser(saveInfo.getReceiverUserName());
            update.setReceiverUserPhone(saveInfo.getReceiverUserPhone());
        }
        update.updateById();

        return JsonResult.ok();
    }

    /**
     * 校验保存字段
     *
     * @param saveInfo  保存dto
     * @param prizeType 礼品类型
     * @return JsonResult
     * @author chenpinjia
     * @date 2021/1/21 19:38
     */
    private JsonResult checkSaveInfo(UserPrizeDetailDTO saveInfo, Integer prizeType) {
        if (prizeType == PrizeTypeEnum.VIRTUAL.getCode()) {
            return JsonResult.fail("虚拟奖品不可保存信息");
        }

        if (StringUtils.isBlank(saveInfo.getReceiverUserName())) {
            return JsonResult.fail("姓名不能为空");
        }
        if (CommonUtil.containSpecialCharacter(saveInfo.getReceiverUserName())){
            return JsonResult.fail("姓名不可包含特殊字符");
        }
        if (StringUtils.isBlank(saveInfo.getReceiverUserPhone())) {
            return JsonResult.fail("手机号不能为空");
        }
        if (!CommonUtil.isPhone(saveInfo.getReceiverUserPhone())){
            return JsonResult.fail("手机号格式有误");
        }
        //实物奖品
        if (prizeType == PrizeTypeEnum.MATERIAL.getCode()) {
            if (StringUtils.isBlank(saveInfo.getAddressProvince())) {
                return JsonResult.fail("省份不能为空");
            }
            if (StringUtils.isBlank(saveInfo.getAddressCity())) {
                return JsonResult.fail("城市不能为空");
            }
            if (StringUtils.isBlank(saveInfo.getAddressArea()) && !StringUtils.contains(saveInfo.getAddressCity(), "深汕")) {
                return JsonResult.fail("区域不能为空");
            }
            if (StringUtils.isBlank(saveInfo.getAddressDetail())) {
                return JsonResult.fail("详细地址不能为空");
            }
            if (CommonUtil.containSpecialCharacter(saveInfo.getAddressDetail(), saveInfo.getAddressProvince(), saveInfo.getAddressCity(), saveInfo.getAddressArea())){
                return JsonResult.fail("不能包含特殊字符");
            }

        }else if (prizeType == PrizeTypeEnum.CASH.getCode()) {
            //现金奖品
            if (StringUtils.isBlank(saveInfo.getBankCardNo())) {
                return JsonResult.fail("银行卡号不能为空");
            }
            if (!CommonUtil.checkBankCardNo(saveInfo.getBankCardNo())){
                return JsonResult.fail("银行卡号校验失败");
            }
            if (StringUtils.isBlank(saveInfo.getOpenCardBank())) {
                return JsonResult.fail("开户银行不能为空");
            }
            if (StringUtils.isBlank(saveInfo.getOpenCardBankBranch())) {
                return JsonResult.fail("开户支行不能为空");
            }
            if (CommonUtil.containSpecialCharacter(saveInfo.getOpenCardBank(), saveInfo.getOpenCardBankBranch())){
                return JsonResult.fail("不能包含特殊字符");
            }
        }else if (prizeType == PrizeTypeEnum.HOTEL.getCode()) {
            //酒店奖品
            if (StringUtils.isBlank(saveInfo.getUserIdCardNo())) {
                return JsonResult.fail("身份证号码不能为空");
            }
            if (!CommonUtil.checkIdCard(saveInfo.getUserIdCardNo())){
                return JsonResult.fail("身份证号码格式有误");
            }
        }else{
            return JsonResult.fail("奖品类型有误");
        }

        return JsonResult.ok();
    }
    
    /**
     * @param prizeId
     * @param userId
     * @param userName
     * @return int
     * @Description: 添加记录
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @Override
    public long insertRecord(long prizeId, long userId, String userName, String exchangeCode,String ip) {
        UserPrize userPrize = new UserPrize();
        RandomValidateCodeUtil codeUtil = new RandomValidateCodeUtil();
        String orderNo = codeUtil.generateOrderNo(userId, prizeId);
        userPrize.setOrderNo(orderNo);
        userPrize.setPrizeId(prizeId);
        userPrize.setUserId(userId);
        userPrize.setUserName(userName);
        userPrize.setExchangeCode(exchangeCode);
        userPrize.setStatus(UserPrizeStatusEnum.UN_EXCHANGE.getCode());
        userPrize.setAddUser(userName);
        userPrize.setAddDate(new Date());
        userPrize.setModifyUser(userName);
        userPrize.setModifyDate(new Date());
        userPrize.setIp(ip);
        userPrize.setDeleted(0);
        userPrizeMapper.insert(userPrize);
        return userPrize.getId();
    }

    /**
     * 摇一摇首页中奖列表接口
     *
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/21 18:57
     */
    @Override
    public JsonResult<List<UserPrizeVO>> userPrizeOfYaoYiYao() {
        return JsonResult.ok(this.getUserPrizeData(ActiveEntranceEnum.SHAKE.getValue(), 100));
    }

    /**
     * 查询所有活动类型的用户中奖记录
     *
     * @param activeType 活动类型
     * @param count      需要查询的记录数
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/22 16:07
     */
    @Override
    public JsonResult<List<UserPrizeVO>> userPrizeList(Integer activeType, Integer count) {
        return JsonResult.ok(this.getUserPrizeData(activeType, count));
    }

    /**
     * 中奖列表分页查询接口
     *
     * @param pageIndex  当前页
     * @param pageSize   每页大小
     * @param activeType 活动类型
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yitu.mayday.api.vo.userprize.UserPrizeVO>
     * @author qinmingtong
     * @date 2021/4/27 15:14
     */
    @Override
    public Page<UserPrizeVO> userPrizePageList(Integer pageIndex, Integer pageSize, Integer activeType) {
        Page<UserPrizeVO> page = new Page<>(pageIndex, pageSize);
        userPrizeMapper.userPrizePageList(page, activeType);
        page.getRecords().forEach(e -> e.setUserName(StringUtil.userNameMask(e.getUserName())));
        return page;
    }

    /**
     * 通用获取用户奖品记录列表
     *
     * @param activeType 活动类型
     * @param count      需要查询的记录数
     * @return java.util.List<UserPrizeVO>
     * @author qinmingtong
     * @date 2021/1/26 20:44
     */
    private List<UserPrizeVO> getUserPrizeData(Integer activeType, Integer count) {
        List<UserPrizeVO> data = userPrizeMapper.userPrizeList(activeType, count);
        data.forEach(e -> e.setUserName(StringUtil.userNameMask(e.getUserName())));
        return data;
    }

}
