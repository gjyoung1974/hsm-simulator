package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.jpos.iso.ISOUtil;

public class Load2Part3DESKey_Variant_N {

	public static byte[] Go(String s_KEY_HexString, int i_Variant)
			throws InvalidKeyException, DataLengthException,
			NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			IllegalStateException, InvalidCipherTextException,
			UnsupportedEncodingException, InvalidKeySpecException,
			ShortBufferException {

		// Variants are produced by performing an exclusive-OR with a fixed value
		// and the first – that is, most significant – byte of each portion of the MFK or KEK.
		StringBuilder sb = new StringBuilder();

		if (s_KEY_HexString.length() != 48) { // if less than 3key length repeat leftmost key portion of the key at end
			sb.append(s_KEY_HexString);
			sb.append(s_KEY_HexString.substring(0, 16));//add the beginning Hexstring key portion to the end
			s_KEY_HexString = sb.toString();
		}

		byte[] bMFK = LoadMFK.Go(s_KEY_HexString);
		byte[] b_HSMSim_Variant = DatatypeConverter.parseHexBinary(ReturnVariantFlags.Go(i_Variant));

		byte[] keyL = new byte[8];byte[] keyR = new byte[8];
		System.arraycopy(bMFK, 0, keyL, 0, 8); System.arraycopy(bMFK, 8, keyR, 0, 8);

		// XOR fixed value with each key portion
		byte[] vKeyL = ISOUtil.xor(keyL, b_HSMSim_Variant);
		byte[] vKeyR = ISOUtil.xor(keyR, b_HSMSim_Variant);

		// concatenate the XorED byte arrays back into one 
		byte[] bMFKv1 = ByteBuffer.allocate(vKeyL.length *3).put(vKeyL).put(vKeyR).put(vKeyL).array();

		// returns 3Key 3TDEA key faked out with leftmost key portion appended to end:
		return bMFKv1;
	}

}
