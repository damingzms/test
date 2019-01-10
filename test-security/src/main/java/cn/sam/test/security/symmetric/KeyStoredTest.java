package cn.sam.test.security.symmetric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 密钥存储
 */
public class KeyStoredTest {
	
	public static void main(String[] args) throws Exception {
		
		// 直接存储不经过加密的key
		storeKeySimply();
		
		// 使用PBE方式加密存储key
//		storeKeyWithPBE();
	}

	/**
	 * 直接存储不经过加密的key的encoded数据
	 * @throws Exception
	 */
	public static void storeKeySimply() throws Exception {
		String data = "待加密的数据";
		
		// DESede
		System.out.println("\nDESede加密：");
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		keyGenerator.init(168);
		SecretKey key = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");  // 指定算法、加解密模式、填充方式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] cipherText = cipher.doFinal(data.getBytes("UTF-8"));
		String encodeToString = Base64.getEncoder().encodeToString(cipherText);
		System.out.println(encodeToString);
		
		// Storing key
		System.out.println("\nKEY存储。。。");
		File keyFile = new File("key");
		byte[] encodedKey = key.getEncoded();
		try (FileOutputStream fos = new FileOutputStream(keyFile)) {
			fos.write(encodedKey);
			fos.flush();
		}
		
		// 解密
		System.out.println("\n解密：");
		byte[] encodedKeyDe = null;
		try (FileInputStream fis = new FileInputStream(keyFile)) {
			encodedKeyDe = new byte[fis.available()];
			fis.read(encodedKeyDe);
		}
		KeySpec keySpec = new SecretKeySpec(encodedKeyDe, "DESede");
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey keyDe = keyFactory.generateSecret(keySpec);
		Cipher cipherDe = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipherDe.init(Cipher.DECRYPT_MODE, keyDe);
		byte[] doFinal = cipherDe.doFinal(Base64.getDecoder().decode(encodeToString));
		System.out.println(new String(doFinal, "UTF-8"));
	}

	/**
	 * 使用PBE方式加密存储key
	 * @throws Exception
	 */
	public static void storeKeyWithPBE() throws Exception {

		String data = "待加密的数据";
		
		// DESede
		System.out.println("\nDESede加密：");
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		keyGenerator.init(168);
		SecretKey keyEn = keyGenerator.generateKey();
		Cipher cipherEn = Cipher.getInstance("DESede/ECB/PKCS5Padding");  // 指定算法、加解密模式、填充方式
		cipherEn.init(Cipher.ENCRYPT_MODE, keyEn);
		byte[] cipherText = cipherEn.doFinal(data.getBytes("UTF-8"));
		String encodeToString = Base64.getEncoder().encodeToString(cipherText);
		System.out.println(encodeToString);
		
		// Storing key
		// 如果Provider没有提供wrap实现，可以考虑使用Key的getEncoded()方法，然后加密存储encoded后的key数据。
		System.out.println("\nKEY加密存储。。。");
		String password = "password";
		int iterationCount = 1000;
		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(keySpec);
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.WRAP_MODE, key, paramSpec);
		byte[] encryptedKey = cipher.wrap(keyEn);
		File keyFile = new File("key");
		try (FileOutputStream fos = new FileOutputStream(keyFile)) {
			fos.write(encryptedKey);
			fos.flush();
		}
		
		// 解密
		System.out.println("\n解密：");
		byte[] encryptedKeyDe = null;
		try (FileInputStream fis = new FileInputStream(keyFile)) {
			encryptedKeyDe = new byte[fis.available()];
			fis.read(encryptedKeyDe);
		}
		cipher.init(Cipher.UNWRAP_MODE, key, paramSpec);  // 为了代码简单，这里直接用了存储key是的cipher、key以及paramSpec，生产中应该参照类cn.sam.test.security.symmetric.pbe.PbeTest进行改造
		Key keyDe = cipher.unwrap(encryptedKeyDe, "DESede", Cipher.SECRET_KEY);
		Cipher cipherDe = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		cipherDe.init(Cipher.DECRYPT_MODE, keyDe);
		byte[] doFinal = cipherDe.doFinal(Base64.getDecoder().decode(encodeToString));
		System.out.println(new String(doFinal, "UTF-8"));
	}
	
}
