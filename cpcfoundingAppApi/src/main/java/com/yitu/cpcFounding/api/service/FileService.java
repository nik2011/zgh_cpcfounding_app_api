package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.JsonResult;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 文件/图片上传
 *
 * @author pangshihe
 * @date 2020/7/24
 */
public interface FileService {
    /**
     * 上传文件
     *
     * @param file    上传的文件
     * @return com.sihc.supervision.entity.JsonResult
     * @author zhujinming
     * @date 2020/6/22 11:45
     */
    JsonResult uploadPic(MultipartFile file) throws IOException;
}
