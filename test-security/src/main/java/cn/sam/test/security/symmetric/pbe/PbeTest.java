package cn.sam.test.security.symmetric.pbe;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * 
 * 基于口令的加密
 * 
 * This is a Password-Based Encryption example.
 *
 * This example uses SHA-1 and Twofish to encrypt or decrypt some text using a
 * password. The iteration count is set to 1000 and the salt is randomly chosen
 * on encryption. The salt is then written to the first 8 bytes of the output
 * text.
 *
 * When decrypting, this program will read the first 8 bytes of the ciphertext
 * and use that as the salt.
 *
 * It requires a JCE-compliant PBEWithSHAAndTwofish-CBC engine, like the Bouncy
 * Castle Provider (http://www.bouncycastle.org).
 */
public class PbeTest {
	private static int ITERATIONS = 1000;

	private static String[] usage() {
		System.err.println("Usage: java PbeTest -e|-d password text");
		String[] args = new String[3];
//		args[0] = "-e";
		args[0] = "-d";
		args[1] = "password";  // 生产中此处可以使用管理员密码等等
//		args[2] = "text";
		args[2] = "LiU7LEq5ckQ=ji8DMNHACD4=";
		return args;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3)
			args = usage();

		char[] password = args[1].toCharArray();
		String text = args[2];
		String output = null;

		// Check the first argument: are we encrypting or decrypting?
		if ("-e".equals(args[0]))
			output = encrypt(password, text);
		else if ("-d".equals(args[0]))
			output = decrypt(password, text);
		else
			System.err.println("Usage: java PbeTest -e|-d password text");

		System.out.println(output);
	}

	private static String encrypt(char[] password, String plaintext) throws Exception {
		// Begin by creating a random salt of 64 bits (8 bytes)
		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);

		// Create the PBEKeySpec with the given password
		PBEKeySpec keySpec = new PBEKeySpec(password);

		// Get a SecretKeyFactory for PBEWithMD5AndDES
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

		// Create our key
		SecretKey key = keyFactory.generateSecret(keySpec);

		// Now create a parameter spec for our salt and iterations
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);

		// Create a cipher and initialize it for encrypting
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));

		Encoder encoder = Base64.getEncoder();

		String saltString = encoder.encodeToString(salt);
		String ciphertextString = encoder.encodeToString(ciphertext);

		return saltString + ciphertextString;
	}

	private static String decrypt(char[] password, String text) throws Exception {
		// Begin by splitting the text into salt and text Strings
		// salt is first 12 chars, BASE64 encoded from 8 bytes.
		String salt = text.substring(0, 12);
		String ciphertext = text.substring(12, text.length());

		// BASE64Decode the bytes for the salt and the ciphertext
		Decoder decoder = Base64.getDecoder();
		byte[] saltArray = decoder.decode(salt);
		byte[] ciphertextArray = decoder.decode(ciphertext);

		// Create the PBEKeySpec with the given password
		PBEKeySpec keySpec = new PBEKeySpec(password);

		// Get a SecretKeyFactory for PBEWithMD5AndDES
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

		// Create our key
		SecretKey key = keyFactory.generateSecret(keySpec);

		// Now create a parameter spec for our salt and iterations
		PBEParameterSpec paramSpec = new PBEParameterSpec(saltArray, ITERATIONS);

		// Create a cipher and initialize it for encrypting
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

		// Perform the actual decryption
		byte[] plaintextArray = cipher.doFinal(ciphertextArray);

		return new String(plaintextArray, "UTF-8");
	}
}