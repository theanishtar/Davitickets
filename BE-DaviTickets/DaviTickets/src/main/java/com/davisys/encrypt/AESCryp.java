package com.davisys.encrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESCryp {
	
	public static final String SECRET_KEY = "8678675";
	public static String encryp(String original) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

		//original = "stackjava.com";

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] byteEncrypted = cipher.doFinal(original.getBytes());
		String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] byteDecrypted = cipher.doFinal(byteEncrypted);
		String decrypted = new String(byteDecrypted);

		System.out.println("original  text: " + original);
		System.out.println("encrypted text: " + encrypted);
		System.out.println("decrypted text: " + decrypted);

		return encrypted;

	}

	public static String decryp(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

		//String original = "stackjava.com";

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] byteEncrypted = cipher.doFinal(encrypted.getBytes());
		//String encrypted = Base64.getEncoder().encodeToString(byteEncrypted);

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] byteDecrypted = cipher.doFinal(byteEncrypted);
		String decrypted = new String(byteDecrypted);

		System.out.println("encrypted text: " + encrypted);
		System.out.println("decrypted text: " + decrypted);

		return decrypted;

	}
	
	public static void main(String[] args) {
		String s = "hello";
		try {
			String maHoa = encryp(s);
			System.out.println(maHoa);
			System.out.println(decryp(maHoa));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
