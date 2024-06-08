package com.yitu.cpcFounding.api.service.impl;

import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.FileConfigEntity;
import com.yitu.cpcFounding.api.enums.ImageEnum;
import com.yitu.cpcFounding.api.service.FileService;
import com.yitu.cpcFounding.api.utils.FileTypeUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传实现
 *
 * @author pangshihe
 * @date 2020/7/24
 */
@Service
public class FileServiceImpl implements FileService {
    private static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Resource
    private FileConfigEntity fileConfigEntity;

    private final String showYearPrefix = "showYear";

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return com.sihc.supervision.entity.JsonResult
     * @author zhujinming
     * @date 2020/6/22 11:45
     */
    @Override
    public JsonResult uploadPic(MultipartFile file) throws IOException {
        log.info("上传文件file/upload开始：" + System.currentTimeMillis());
        if (file == null || file.getSize() == 0) {
            return JsonResult.fail("未找到上传的文件");
        }
        if (!FileTypeUtil.isAllowPicType(file)) {
            return JsonResult.fail("上传的文件类型不合法");
        }
        long fileSize = file.getSize();
        if (fileSize > Configs.MAX_UPLOAD_SIZE * 1024) {
            return JsonResult.fail(String.format("上传图片不得超过%sK", Configs.MAX_UPLOAD_SIZE));
        }
        Map map = saveFile(file);
        return map == null ? JsonResult.fail("上传失败") : JsonResult.ok(map);
    }

    /**
     * 保存文件
     *
     * @param file 文件
     * @return java.util.Map
     * @author pangshihe
     * @date 2020/6/28 10:32
     */
    private Map saveFile(MultipartFile file) {
        Map map = null;
        final String originName = file.getOriginalFilename();
        final String typeFile = StringUtils.substringAfterLast(originName, ".");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fileName = System.currentTimeMillis() + RandomUtils.nextInt(0, 50) + "." + typeFile;
        String path = String.format("/%s/%s/%s",showYearPrefix,sdf.format(new Date()), fileName);
        String iosPath = fileConfigEntity.getUrl() + sdf.format(new Date()) + "/";
        String fullPath = fileConfigEntity.getUrl() + path.substring(1);
        log.info("上传文件file/upload保存文件：" + System.currentTimeMillis());
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());

            File dest = new File(fileConfigEntity.getUrl(), path);
            File parentFile = dest.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    return null;
                }
            }
            file.transferTo(dest);
            //缩略图1
            String[] thumbSize = getWidthHeight(image, ImageEnum.SMALL).split("x");
            String thumbPath = saveThumbnail(file.getInputStream(), fullPath, Integer.valueOf(thumbSize[0]), Integer.valueOf(thumbSize[1]),ImageEnum.SMALL);
            if (thumbPath == null) {
                return null;
            }
            String smallPath = "/files/" + thumbPath.replace(fileConfigEntity.getUrl(), "");

            //缩略图2
            String[] thumbSize2 = getWidthHeight(image,ImageEnum.MEDIUM).split("x");
            String thumbPath2 = saveThumbnail(file.getInputStream(), fullPath, Integer.valueOf(thumbSize2[0]), Integer.valueOf(thumbSize2[1]),ImageEnum.MEDIUM);
            if (thumbPath2 == null) {
                return null;
            }
            String mediumPath = "/files/" + thumbPath2.replace(fileConfigEntity.getUrl(), "");

            map = new HashMap(4);
            map.put("name", originName);
            map.put("path", "/files" + path);
            map.put("smallPath", smallPath);
            map.put("mediumPath", mediumPath);
            map.put("size", file.getSize());
            map.put("addDate", new Date());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
        log.info("上传文件file/upload结束：" + System.currentTimeMillis());
        return map;
    }

    /**
     * 获取等比例宽高
     * @param image 图片
     * @param imageEnum 图片类型枚举
     * @author shenjun
     * @date 2021/1/27 10:17
     * @return java.lang.String
     */
    private String getWidthHeight(BufferedImage image,ImageEnum imageEnum){
        int w = image.getWidth();
        int h = image.getHeight();
        int width = imageEnum.getValue();
        if(w<=width){
            return w+"x"+h;
        }else {
            double hight = (double)width/w*h;
            return width+"x"+(int)hight;
        }
    }

    /**
     * 生成缩略图
     *
     * @param inputStream 输入流
     * @param fullPath    原图路径
     * @param width       生成的宽度
     * @param height      高度
     * @return void
     * @author liuzhaowei
     * @date 2020/7/27
     */
    private String saveThumbnail(InputStream inputStream, String fullPath, int width, int height,ImageEnum imageEnum) {
        String[] arr = fullPath.split("\\.");
        String thumbPath = String.format("%s_%s.%s", arr[0], imageEnum.getName(), arr[1]);
        try {
            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            Thumbnails.of(inputStream)
                    .size(width, height)
                    .toOutputStream(outputBuffer);
            InputStream thumbInputStream = new ByteArrayInputStream(outputBuffer.toByteArray());
            FileUtils.copyInputStreamToFile(thumbInputStream, new File(thumbPath));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return thumbPath;

    }
}
