package com.yitu.women.api.test;

import org.junit.Test;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/18 16:02
 */

public class TestString extends FatherTest{
    @Test
    public void testString(){
        String a="a";
        System.out.println(a.split(",")[0]);
    }

    @Test
    public void testIndexOf(){
        String a="我的其他街道";
        String b="其他街道";

        if(a.indexOf(b)!=-1){
            System.out.println("包含");
        }
    }
}
