package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class LoadMFK {

	public static byte[] Go(String s_Command) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DataLengthException, IllegalStateException, InvalidCipherTextException, UnsupportedEncodingException, InvalidKeySpecException, ShortBufferException {
	
		byte[] bMFK = Load2Part3DESKey_No_IV.load(s_Command);
		return bMFK;
      
	}

}
