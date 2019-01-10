package cn.sam.test.security;

import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

public class Test {
	
	public static void main(String[] args) throws Exception {

		Provider[] providers = Security.getProviders();
		System.out.println(Arrays.toString(providers));
		
		String data = "待加密的数据";
		
		// 消息摘要
		System.out.println("\n消息摘要：");
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		System.out.println(messageDigest.getProvider());
		messageDigest.update(data.getBytes("UTF-8"));
		byte[] digest = messageDigest.digest();
		System.out.println(Arrays.toString(digest));
		System.out.println(Base64.getEncoder().encodeToString(digest));  // 二进制数一般都是按8位一个字节进行保存，标准的ASCII仅仅只有7位。BASE64编码是解决这个问题的办法，它把每字节8位转换为6位的块。
	}

}
