package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

public class Encrypt_Key_Under_MFK_Variant_N {

	public static byte[] Go(String sMFK, String s_ptKey_In, int i_Variant) throws InvalidKeyException, DataLengthException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IllegalStateException, InvalidCipherTextException, InvalidKeySpecException, ShortBufferException, IOException{
		
		//load plain-text MFK Variant 0: MFK.0
		byte[] bMFK_Vn = Load2Part3DESKey_Variant_N.Go(sMFK, i_Variant);

		byte[] bPtext_KEY_In = Hex.decode(s_ptKey_In);//load the plaintext inbound key

		DesDEBC desdebc = new DesDEBC(bMFK_Vn);//load DesD EBC Cipher with MFK Variant.N
		ByteArrayInputStream in = new ByteArrayInputStream(bPtext_KEY_In);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		desdebc.encrypt(in, bPtext_KEY_In.length, out);//encrypt in-bound key under Variant.N of MFK
		
		byte[] bEnc_KEY_Out = Arrays.copyOfRange(out.toByteArray(), 0, bPtext_KEY_In.length);

		return bEnc_KEY_Out;

	}

}