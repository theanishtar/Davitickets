package com.davisys.encrypt;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.davisys.constant.SecretDavisy;

public class DES {
	private static final String secretKey = SecretDavisy.DES_KEY;

	/*
	 * public static String encrypt(String strToEncrypt) { try { MessageDigest sha =
	 * MessageDigest.getInstance("SHA-1"); byte[] key = secretKey.getBytes("UTF-8");
	 * key = sha.digest(key); key = Arrays.copyOf(key, 16); SecretKeySpec secretKey
	 * = new SecretKeySpec(key, "AES"); Cipher cipher =
	 * Cipher.getInstance("AES/ECB/PKCS5Padding"); cipher.init(Cipher.ENCRYPT_MODE,
	 * secretKey); return
	 * Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(
	 * "UTF-8"))); } catch (Exception e) { System.out.println(e.toString()); }
	 * return null; }
	 * 
	 * 
	 * public static String decrypt(String strToDecrypt) { try { MessageDigest sha =
	 * MessageDigest.getInstance("SHA-1"); byte[] key = secretKey.getBytes("UTF-8");
	 * key = sha.digest(key); key = Arrays.copyOf(key, 16); SecretKeySpec secretKey
	 * = new SecretKeySpec(key, "AES"); Cipher cipher =
	 * Cipher.getInstance("AES/ECB/PKCS5PADDING"); cipher.init(Cipher.DECRYPT_MODE,
	 * secretKey); return new
	 * String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))); } catch
	 * (Exception e) { System.out.println(e.toString()); } return null; }
	 */

	private static String padString(String input) {
		int paddingLength = 8 - input.length() % 8;
		StringBuilder paddedString = new StringBuilder(input);
		for (int i = 0; i < paddingLength; i++) {
			paddedString.append("\0");
		}
		return paddedString.toString();
	}

	public static String des_encrypt(String data) throws Exception {
		byte[] keyBytes = secretKey.getBytes();
		DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(desKeySpec);

		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(padString(data).getBytes());

		return des_replace(DatatypeConverter.printBase64Binary(encryptedBytes));
	}

	public static String des_decrypt(String encryptedData) throws Exception {
		String finl;
		encryptedData = des_replace(encryptedData);
        byte[] keyBytes = secretKey.getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = DatatypeConverter.parseBase64Binary(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        
        return new String(decryptedBytes).trim();
    }
	
	public static String des_replace(String s) {
		if(s.indexOf("+")>-1)
			return s.replace("+", "-dvs-");
		if(s.indexOf("-dvs-")>-1)
			return s.replace("-dvs-","+");
		return s;
	}

	public static void main(String[] args) {
		// hello -> Gm73S7hUK+hMktXIala+LA== -> hello
		try {
			//System.out.println(des_encrypt("dangtt135@gmail.com:115696097704113482578"));
			System.out.println(des_decrypt("wPa/DWqkDsusM0aBmTCW/CLLVeRwt-dvs-hAK1XbnQXCwQL9lHJq8h1YlJcblzsiY1W4"));
										  //wPa/DWqkDsusM0aBmTCW/CLLVeRwt+hAK1XbnQXCwQL9lHJq8h1YlJcblzsiY1W4
										  //wPa/DWqkDsusM0aBmTCW/FCLLVeRwt+hAK1XbnQXCwQL9lHJq8h1YlJcblzsiY1W4
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
