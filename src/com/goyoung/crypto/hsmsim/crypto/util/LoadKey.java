package com.goyoung.crypto.hsmsim.crypto.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class LoadKey {

	public static SecretKey load(String s_KeyHexBin) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		//TODO load keys from properties file or database
		byte[] b_Key = DatatypeConverter.parseHexBinary(s_KeyHexBin);
				
	 	SecretKey key = new SecretKeySpec(b_Key, "DESede");

		return key;

	}
}
