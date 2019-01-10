package cn.sam.test.security.symmetric;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Test {
	
	public static void main(String[] args) throws Exception {

		Provider[] providers = Security.getProviders();
		System.out.println(Arrays.toString(providers));
		
		String data = "待加密的数据";
		
		// Blowfish
		System.out.println("\nBlowfish：");
		KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
		keyGenerator.init(128);
		SecretKey key = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");  // 指定算法、加解密模式、填充方式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(data.getBytes("UTF-8"));
		System.out.println(Arrays.toString(cipherText));
		System.out.println(Base64.getEncoder().encodeToString(cipherText));
		
		// DESede
		System.out.println("\nDESede：");
		KeyGenerator keyGenerator1 = KeyGenerator.getInstance("DESede");
		keyGenerator1.init(168);
		SecretKey key1 = keyGenerator1.generateKey();
		Cipher cipher1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");  // 指定算法、加解密模式、填充方式
		cipher1.init(Cipher.ENCRYPT_MODE, key1);
		byte[] cipherText1 = cipher1.doFinal(data.getBytes("UTF-8"));
		System.out.println(Arrays.toString(cipherText1));
		String encodeToString = Base64.getEncoder().encodeToString(cipherText1);
		System.out.println(encodeToString);
		
		// 解密
		System.out.println("\n解密：");
		cipher1.init(Cipher.DECRYPT_MODE, key1);
		byte[] doFinal = cipher1.doFinal(Base64.getDecoder().decode(encodeToString));
		System.out.println(new String(doFinal, "UTF-8"));
	}

}
