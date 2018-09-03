package com.goyoung.crypto.hsmsim.crypto.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class Gen3TDEAKey {

	public static String Go() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {


		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		kg.init(168); // key length 112 for two keys, 168 for three keys
		SecretKey sk = kg.generateKey();
		
		// The KCV is the "Key Check Value" for the key, calculated by assuming the key/components are 3DES keys, and encrypting a string of binary zeroes. The KCV is the first six hex digits of the resulting ciphertext.
		byte[] null_bytes_16 = new byte[16];
		
		//Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");
		Cipher c = Cipher.getInstance("DESede");
		
		c.init(Cipher.ENCRYPT_MODE, sk);
		byte[] cv = c.doFinal(null_bytes_16);
		byte[] b_arrays = Arrays.copyOfRange(cv, 0, 2);
		
		return DatatypeConverter.printHexBinary(sk.getEncoded()) + "##" + DatatypeConverter.printHexBinary(b_arrays);
	
	}

}
