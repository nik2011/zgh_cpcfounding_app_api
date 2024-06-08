package com.yitu.cpcFounding.api.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件类型工具类
 *
 * @author pangshihe
 * @date 2020/7/9
 */
public class FileTypeUtil {

    /**
     * 图片后缀数组
     */
    private final static String[] ALLOW_PIC_TYPE = {"JPG", "JPEG", "PNG", "GIF", "BMP"};
    /**
     * doc,excel后缀数组
     */
    private final static String[] ALLOW_EXCEL_TYPE = {"DOC", "DOCX", "XLS", "XLSX"};

    /**
     * 允许的白名单
     */
    private final static String[] ALLOW_FILE_TYPE = new String[]{"JPG", "JPEG", "PNG", "BMP", "GIF",
            "RTF", "DOC", "XLS", "WPD", "PDF", "DOCX", "XLSX", "PPTX", "PPT", "TXT", "XML", "RAR", "ZIP"};

    /**
     * 根据文件的头部编码获取类型枚取
     *
     * @author zhujinming
     * @date 20/07/09 11:24
     */
    public enum FileCheckEnums {
        /**
         * JPG、JPEG.
         */
        JPG_JPEG("FFD8FF"),
        /**
         * PNG.
         */
        PNG("89504E47"),
        /**
         * Windows Bitmap.
         */
        BMP("424D"),
        /**
         * GIF.
         */
        GIF("47494638"),
        /**
         * Rich Text Format.
         */
        RTF("7B5C727466"),
        /**
         * MS Word/Excel.
         */
        XLS_DOC("D0CF11E0"),
        /**
         * WordPerfect.
         */
        WPD("FF575043"),
        /**
         * Adobe Acrobat.
         */
        PDF("255044462D312E"),
        /**
         * DOCX、XLSX、PPTX
         */
        DOCX_XLSX_PPTX("504B0304"),
//        /**
//         * TXT
//         */
//        TXT("61736664"),
        /**
         * XML
         */
        XML("3C3F786D6C"),
        /**
         * RAR
         */
        RAR("52617221"),
        /**
         * ZIP
         */
        ZIP("504B0304");

        private String value;

        FileCheckEnums(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 根据图片的头部编码获取类型枚取
     *
     * @author zhujinming
     * @date 16:56 20/07/23
     */
    public enum PicCheckEnums {
        /**
         * JPG、JPEG.
         */
        JPG_JPEG("FFD8FF"),
        /**
         * PNG.
         */
        PNG("89504E47"),
        /**
         * Windows Bitmap.
         */
        BMP("424D"),
        /**
         * GIF.
         */
        GIF("47494638");


        private String value;

        PicCheckEnums(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 获取文件类型
     *
     * @param file file
     * @return java.lang.String
     * @author zhujinming
     * @date 11:19 20/07/23
     */
    public static FileCheckEnums getFileType(MultipartFile file) throws IOException {
        //文件的文件头信息
        byte[] b = new byte[28];
        System.arraycopy(file.getBytes(), 0, b, 0, b.length);
        //将文件的文件头字节码转换成字符串
        String fileType = bytesToHexString(b);
        FileCheckEnums[] fileTypes = FileCheckEnums.values();
        for (FileCheckEnums type : fileTypes) {
            if (fileType != null && fileType.toUpperCase().startsWith(type.getValue().toUpperCase())) {
                return type;
            }
        }
        return null;
    }


    /**
     * 允许的文件类型（先判断文件后缀，再判断文件头信息，.TXT文件不判断文件头信息）
     *
     * @param file 文件
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    public static boolean isAllowFileType(MultipartFile file) throws IOException {
        final String originName = file.getOriginalFilename();
        final String typeFile = org.apache.commons.lang3.StringUtils.substringAfterLast(originName, ".");
        //判断后缀
        if (!isAllowFileTypeBySuffix(typeFile)) {
            return false;
        }
        //TXT没有文件头信息，该类型文件放行
        if (typeFile.equalsIgnoreCase("TXT")) {
            return true;
        }
        //文件的文件头信息
        byte[] b = new byte[28];
        System.arraycopy(file.getBytes(), 0, b, 0, b.length);
        //将文件的文件头字节码转换成字符串
        String fileType = bytesToHexString(b);
        FileCheckEnums[] fileTypes = FileCheckEnums.values();
        for (FileCheckEnums type : fileTypes) {
            if (fileType != null && fileType.toUpperCase().startsWith(type.getValue().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 允许的图片类型（先判断图片后缀，再判断文件头信息）
     *
     * @param file 文件
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    public static boolean isAllowPicType(MultipartFile file) throws IOException {
        final String originName = file.getOriginalFilename();
        final String typeFile = org.apache.commons.lang3.StringUtils.substringAfterLast(originName, ".");
        //判断后缀
        if (!isAllowPicTypeBySuffix(typeFile)) {
            return false;
        }
        //文件的文件头信息
        byte[] b = new byte[28];
        System.arraycopy(file.getBytes(), 0, b, 0, b.length);
        //将文件的文件头字节码转换成字符串
        String fileType = bytesToHexString(b);
        for (PicCheckEnums type : PicCheckEnums.values()) {
            if (fileType != null && fileType.toUpperCase().startsWith(type.getValue().toUpperCase())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 允许的表格类型（先判断图片后缀，再判断文件头信息）
     *
     * @param file 文件
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    public static boolean isAllowExcelType(MultipartFile file) throws IOException {
        final String typeFile = org.apache.commons.lang3.StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //判断后缀
        if (!isAllowExcelTypeBySuffix(typeFile)) {
            return false;
        }
        //文件的文件头信息
        byte[] b = new byte[28];
        System.arraycopy(file.getBytes(), 0, b, 0, b.length);
        //将文件的文件头字节码转换成字符串
        String fileType = bytesToHexString(b);
        return (fileType != null && (
                fileType.toUpperCase().startsWith(FileCheckEnums.XLS_DOC.getValue().toUpperCase()) ||
                fileType.toUpperCase().startsWith(FileCheckEnums.DOCX_XLSX_PPTX.getValue().toUpperCase())));
    }

    /**
     * 允许的文件类型(文件后缀判断)
     *
     * @param fileSuffix 文件后缀
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    private static boolean isAllowFileTypeBySuffix(String fileSuffix) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(fileSuffix)) {
            return false;
        }
        for (String s : ALLOW_FILE_TYPE) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 允许的图片类型(文件后缀判断)
     *
     * @param fileSuffix 文件后缀
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    private static boolean isAllowPicTypeBySuffix(String fileSuffix) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(fileSuffix)) {
            return false;
        }
        for (String s : ALLOW_PIC_TYPE) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 允许的EXCEL类型(文件后缀判断)
     *
     * @param fileSuffix 文件后缀
     * @return boolean
     * @author pangshihe
     * @date 2020/7/9 14:37
     */
    private static boolean isAllowExcelTypeBySuffix(String fileSuffix) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(fileSuffix)) {
            return false;
        }
        for (String s : ALLOW_EXCEL_TYPE) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得文件头部字符串
     *
     * @param bytes 文件字节
     * @return java.lang.String
     * @author pangshihe
     * @date 2020/7/22 9:17
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}