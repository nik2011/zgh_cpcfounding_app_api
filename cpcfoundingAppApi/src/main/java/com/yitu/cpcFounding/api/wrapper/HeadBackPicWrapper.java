/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yitu.cpcFounding.api.wrapper;

import com.yitu.cpcFounding.api.vo.ad.HeadBackPicVO;
import com.yitu.cpcFounding.api.vo.ad.LsAdVO;
import org.springframework.beans.BeanUtils;

/**
 * 返回广告类型所需要的字段
 *
 * @author jxc
 * @date 2020/9/1 12:47
 */
public class HeadBackPicWrapper extends BaseEntityWrapper<LsAdVO, HeadBackPicVO>  {

	public static HeadBackPicWrapper build() {
		return new HeadBackPicWrapper();
 	}

	@Override
	public HeadBackPicVO entityVO(LsAdVO lsAd) {
		HeadBackPicVO vo = new HeadBackPicVO();
		BeanUtils.copyProperties(lsAd, vo);
		return vo;
	}

}
