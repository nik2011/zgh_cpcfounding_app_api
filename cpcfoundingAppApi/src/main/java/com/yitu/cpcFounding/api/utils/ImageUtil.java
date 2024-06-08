package com.yitu.cpcFounding.api.utils;//package com.yitu.year.utils;

import com.yitu.cpcFounding.api.exception.GlobalException;
import com.yitu.cpcFounding.api.enums.ImageEnum;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 图片工具类
 * @author shenjun
 * @date 2021/1/23 11:05
 */
public class ImageUtil {
    private static Logger log = LoggerFactory.getLogger(GlobalException.class);
    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile){
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try{
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }catch (IOException e){
            log.error(e.getMessage());
        }
        return new String(Objects.requireNonNull(Base64.encodeBase64(data)));
    }

    /**
     * 图片地址
     * @param imageEnum 图片类型
     * @author shenjun
     * @date 2021/1/27 10:34
     * @return java.lang.String
     */
    public static String getPath(String filePath, ImageEnum imageEnum){
        if(StringUtil.isEmpty(filePath)){
            return filePath;
        }
        String[] arr = filePath.split("\\.");
        if (arr.length < 2) {
            return filePath;
        }
        return String.format("%s_%s.%s", arr[0], imageEnum.getName(), arr[1]);
    }
}
