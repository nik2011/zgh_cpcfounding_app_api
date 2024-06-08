package com.yitu.cpcFounding.api.controller;

import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.FileService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 文件上传
 *
 * @author pangshihe
 * @date 2020/7/24
 */
@RestController
@RequestMapping("/file")
@LoginRequired
@Api(value = "图片上传", tags = "图片上传")
public class FileController {
    @Resource
    FileService fileService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return JsonResult
     * @author pangshihe
     * @date 2020/9/28 9:59
     */
    @ApiOperation(value = "图片上传", notes = "图片上传")
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    @LoginRequired
    public JsonResult uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadPic(file);
    }
}
