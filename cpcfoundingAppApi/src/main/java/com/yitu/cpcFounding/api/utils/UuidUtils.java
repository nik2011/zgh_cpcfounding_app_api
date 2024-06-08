package com.yitu.cpcFounding.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 *
 * @author shenjun
 * @date 2021/7/16 10:16
 */
public class UuidUtils {

    private static AtomicLong id;


    /**
     * 生成Long 类型唯一ID
     */
    public synchronized static Long getId() {
        Long time = Long.valueOf(new SimpleDateFormat("MMddHHmmssSSS").format(new Date()));
        if (id == null) {
            id = new AtomicLong(time);
            return id.get();
        }
        if (time <= id.get()) {
            id.addAndGet(1);
        } else {
            id = new AtomicLong(time);
        }
        return id.get();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(getId());
        }
    }

}
