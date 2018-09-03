package com.goyoung.crypto.hsmsim.crypto.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ThreeTDEA_Decrypt_2 {

	public static byte[] Go(byte[] b_Enc_Data, byte[] bMFK) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException
	 {
		
		Security.addProvider(new BouncyCastleProvider());
		 SecretKeySpec   key = new SecretKeySpec(bMFK, "DesEde");
	        Cipher          cipher = Cipher.getInstance("DesEde/ECB/NoPadding", "BC");

	       
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        
	        byte[] cipherText = new byte[cipher.getOutputSize(b_Enc_Data.length)];
	        
	        int ctLength = cipher.update(b_Enc_Data, 0, b_Enc_Data.length, cipherText, 0);
	        //System.out.println("output: " + Hex.toHexString(cipherText) + " bytes: " + ctLength);
	        
	        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

	        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
	        
	        ptLength += cipher.doFinal(plainText, ptLength);
		
		
		return cipherText;

	}

}
