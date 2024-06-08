package com.yitu.cpcFounding.api.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;


/***
 * 将base64加密换成对称性加密1
 * 测试密码：ewbadswtdadsfawefd
 *  @author qinm
 *  2016-7-21
 */
public class DessUtils {

	 public final static String KEY_DES="DES";
	 public final static String ECB_PKCS5="DES/ECB/PKCS5Padding";

	 private DessUtils(){

	 }

	 public static String encypt(String data,String key)throws Exception{
		  byte[] bt = encrpt(data.getBytes(),key);
		 return new BASE64Encoder().encode(bt);
	 }

	 public static String decrpt(String data,String key) throws Exception {
		 BASE64Decoder decoder = new BASE64Decoder();
		 byte[] buf = decoder.decodeBuffer(data);
		 byte[] bt = decrpt(buf,key);
		 return new String(bt);
	 }

    public static byte[] encrpt(byte[] data,String key) throws Exception{
    	SecureRandom  random = new SecureRandom();
    	//
    	DESKeySpec  desKey =  new DESKeySpec(key.getBytes());
    	//创建一个秘钥
    	SecretKeyFactory   keyFactory =  SecretKeyFactory.getInstance(KEY_DES);

    	SecretKey secureKey = keyFactory.generateSecret(desKey);

    	Cipher cipher =Cipher.getInstance(ECB_PKCS5);

    	cipher.init(Cipher.ENCRYPT_MODE, secureKey,random);

    	return cipher.doFinal(data);
    }

    public static byte[] decrpt(byte[] data,String key) throws Exception {
       SecureRandom  random = new SecureRandom();
    	// 根据一个字节数组构造一个 SecretKey，而无须通过一个（基于 provider 的）SecretKeyFactory。 自己指定秘钥
    	DESKeySpec  desKey =  new DESKeySpec(key.getBytes());
    	// 获取工厂
    	SecretKeyFactory   keyFactory =  SecretKeyFactory.getInstance(KEY_DES);
    	// 将 秘钥的二进制代码转成对象
    	SecretKey secureKey = keyFactory.generateSecret(desKey);

    	Cipher cipher =Cipher.getInstance(ECB_PKCS5);

    	cipher.init(Cipher.DECRYPT_MODE, secureKey,random);

    	return cipher.doFinal(data);

    }

	public static void main(String[] args) throws Exception {
	 	String key = "ewbadswt";
		String s = "{\n" +
				"\"identity\":\"xxxxx\",\n" +
				"\"idcard\":\"43242434542424234\",\n" +
				"\"memcard\":\"54322423\",\n" +
				"\"tel\":\"13838385656\"\n" +
				"}";
		String a = DessUtils.encypt(s,key);
		System.out.print("a:"+DessUtils.encypt(a,key));
		System.out.println();

	}
}
