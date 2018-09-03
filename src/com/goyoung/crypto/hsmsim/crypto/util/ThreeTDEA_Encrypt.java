package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class ThreeTDEA_Encrypt {

	public static byte[] Go(byte[] b_Clear_Data, byte[] bKEY)
			throws InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(b_Clear_Data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		DESedeKeySpec desedeKeySpec = new DESedeKeySpec(bKEY);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("TripleDES");
		Key desKey = (Key) keyFactory.generateSecret(desedeKeySpec);

		Cipher cipher = Cipher.getInstance("TripleDES");
		cipher.init(Cipher.ENCRYPT_MODE, desKey);

		// Read bytes, decrypt, and write them out.
		byte[] buffer = new byte[b_Clear_Data.length];
		int bytesRead;

		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(cipher.update(buffer, 0, bytesRead));
		}
		byte[] b_E_Data = out.toByteArray();
		return b_E_Data;

	}

}
