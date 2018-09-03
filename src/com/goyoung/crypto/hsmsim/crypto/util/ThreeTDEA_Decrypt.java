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

import org.bouncycastle.util.encoders.Hex;

public class ThreeTDEA_Decrypt {

	public static String Go(byte[] b_Enc_Data, byte[] bMFK)
			throws InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IOException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(b_Enc_Data);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		DESedeKeySpec desedeKeySpec = new DESedeKeySpec(bMFK);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DesEde");
		Key desKey = keyFactory.generateSecret(desedeKeySpec);

		Cipher cipher = Cipher.getInstance("DesEde");
		cipher.init(Cipher.DECRYPT_MODE, desKey);

		// Read bytes, decrypt, and write them out.
		byte[] buffer = new byte[b_Enc_Data.length];
		int bytesRead;

		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(cipher.update(buffer, 0, bytesRead));
		}
		
		byte[] test = out.toByteArray();
		String s_Data = Hex.toHexString(test).toUpperCase();
	
		return s_Data;

	}

}
