package com.yitu.cpcFounding.api.utils;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 枚举工具类
 * @author jxc
 * @date 2020/7/7 9:43
 */
public class EnumUtil {

    public static boolean isInEnum(Object value, Class<? extends IEnum> type) {
        if (!type.isEnum()) {
            throw new IllegalArgumentException("Type: " + type + " must be a enum");
        }
        for (IEnum iEnum : type.getEnumConstants()) {
            if (iEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串比较
     * @param value value
     * @param type type
     * @return boolean
     * @author shaojun.li
     * @date 2020/7/7 9:43
     */
    public static boolean isInEnum4String(Object value, Class<? extends IEnum> type) {
        if (!type.isEnum()) {
            throw new IllegalArgumentException("Type: " + type + " must be a enum");
        }
        for (IEnum iEnum : type.getEnumConstants()) {
            if (iEnum.getValue().toString().equals(value)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据code获取枚举desc
     * @param clazz clazz
     * @param code code
     * @return 枚举desc
     */
    public static String getDesc(Class<? extends IEnum> clazz, Object code){
        Object[] objects = clazz.getEnumConstants();
        try {
            Method method = clazz.getMethod("getValue");
            for (Object o : objects) {
                if (String.valueOf(code).equals(String.valueOf(method.invoke(o)))) {
                    return String.valueOf(clazz.getMethod("getDesc").invoke(o));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * 根据desc获取枚举code
     * @param clazz clazz
     * @param desc desc
     * @return 枚举code
     * @author lsj
     */
    public static String getValue(Class<? extends IEnum<Integer>> clazz, Object desc){
        Object[] objects = clazz.getEnumConstants();
        try {
            Method method = clazz.getMethod("getDesc");
            for (Object o : objects) {
                if (String.valueOf(desc).equals(String.valueOf(method.invoke(o)))) {
                    return String.valueOf(clazz.getMethod("getValue").invoke(o));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取枚举列表
     * @param clazz clazz
     * @return java.util.List
     * @author shaojun.li
     * @date 2020/7/7 9:44
     */
    public static List getDescList(Class<? extends IEnum> clazz){
        Object[] objects = clazz.getEnumConstants();
        List list = new ArrayList();
        try {
            for (Object o : objects) {
                list.add(clazz.getMethod("getDesc").invoke(o));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取枚举列表
     * @param clazz clazz
     * @return java.util.List
     * @author shaojun.li
     * @date 2020/7/7 9:44
     */
    public static List getValueList(Class<? extends IEnum> clazz){
        Object[] objects = clazz.getEnumConstants();
        List list = new ArrayList();
        try {
            for (Object o : objects) {
                list.add(clazz.getMethod("getValue").invoke(o));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取枚举列表
     * @param clazz clazz
     * @param type type
     * @return java.util.Map
     * @author shaojun.li
     * @date 2020/7/7 9:44
     */
    public static Map getCollectionTypeList(Class<? extends IEnum> clazz, String type){
        Object[] objects = clazz.getEnumConstants();
        Map<String, Object> map = new HashMap<>();
        List<Integer> code = new ArrayList<>();
        List<Integer> subCode = new ArrayList<>();
        try {
            for (Object o : objects) {
                if(Arrays.asList(StringUtils.split(
                        (String) clazz.getMethod("getType").invoke(o), ",")).contains(type)){
                    code.add((Integer) clazz.getMethod("getValue").invoke(o));

                    String subCodeStr = (String) clazz.getMethod("getSubCode").invoke(o);
                    if(!StringUtils.isBlank(subCodeStr)){
                        for(String s: StringUtils.split(subCodeStr,",")){
                            subCode.add(Integer.valueOf(s));
                        }
                    }

                }
            }
            map.put("code",code);
            map.put("subCode",subCode);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据code获取枚举Param
     * @param clazz clazz
     * @param code code
     * @return java.lang.String
     * @author shaojun.li
     * @date 2020/7/7 9:44
     */
    public static String getParam(Class<? extends IEnum> clazz, Object code){
        Object[] objects = clazz.getEnumConstants();
        try {
            Method method = clazz.getMethod("getValue");
            for (Object o : objects) {
                if (String.valueOf(code).equals(String.valueOf(method.invoke(o)))) {
                    return (String) clazz.getMethod("getParam").invoke(o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    /**
     * 根据code获取枚举Param
     * @param clazz clazz
     * @param code code
     * @return java.lang.String
     * @author shaojun.li
     * @date 2020/7/7 9:45
     */
    public static String getEntity(Class<? extends IEnum> clazz, Object code){
        Object[] objects = clazz.getEnumConstants();
        try {
            Method method = clazz.getMethod("getValue");
            for (Object o : objects) {
                if (String.valueOf(code).equals(String.valueOf(method.invoke(o)))) {
                    return (String) clazz.getMethod("getEntity").invoke(o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * 根据code获取枚举Param
     * @param clazz clazz
     * @param method method
     * @param type type
     * @param typeMethod typeMethod
     * @return java.util.List
     * @author shaojun.li
     * @date 2020/7/7 9:45
     */
    public static List getFieldListByType(Class<? extends IEnum> clazz, String method, String type, String typeMethod){
        if(StringUtils.isBlank(method)|| StringUtils.isBlank(type)|| StringUtils.isBlank(typeMethod)){
            return null;
        }
        Object[] objects = clazz.getEnumConstants();
        List list = new ArrayList();
        try {
            for (Object o : objects) {
                if(type.equals(String.valueOf(clazz.getMethod(typeMethod).invoke(o)))){
                    if(!StringUtils.isBlank(String.valueOf(clazz.getMethod(method).invoke(o)))){
                        list.add(clazz.getMethod(method).invoke(o));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取目标值
     * @param clazz clazz
     * @param code code
     * @param codeMethod codeMethod
     * @param desMethod desMethod
     * @return java.lang.String
     * @author shaojun.li
     * @date 2020/7/7 9:45
     */
    public static String getTargetValue(Class<? extends IEnum> clazz, Object code, String codeMethod, String desMethod){
        Object[] objects = clazz.getEnumConstants();
        try {
            Method method = clazz.getMethod(codeMethod);
            for (Object o : objects) {
                if (String.valueOf(code).equals(String.valueOf(method.invoke(o)))) {
                    return String.valueOf(clazz.getMethod(desMethod).invoke(o));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * TODO
     * @param code
     * @param enumClass
     * @return T
     * @author jxc
     * @date 2020/8/14 15:57
     */
    public static <T extends IEnum> T getByCode(Class<T> enumClass, Integer code) {
        //通过反射取出Enum所有常量的属性值
        for (T each: enumClass.getEnumConstants()) {
            //利用code进行循环比较，获取对应的枚举
            if (code.equals(each.getValue())) {
                return each;
            }
        }
        return null;
    }

    /**
     * 是否包含枚举
     *
     * @param enumClass
     * @param code
     * @return boolean
     * @author caichenning
     * @date 2021/4/21 15:02
     */
    public static boolean isExist(Class enumClass, int code){
        List enumList = EnumUtils.getEnumList(enumClass);
        for (int i = 0;i<enumList.size(); i++){
            Object en = enumList.get(i);
            Class<?> enClass = en.getClass();
            try {
                Method method = enClass.getMethod("getValue"); // 需要与枚举类方法对应
                Object invoke = method.invoke(en, null);
                if(Integer.parseInt(invoke.toString()) == code) {
                    return true;
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
