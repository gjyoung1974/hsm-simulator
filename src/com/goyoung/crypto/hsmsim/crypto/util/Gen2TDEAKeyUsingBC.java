package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.util.encoders.Hex;

public class Gen2TDEAKeyUsingBC {

	public static String Go() throws DataLengthException,
			IllegalStateException, InvalidCipherTextException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException,
			InvalidKeySpecException, ShortBufferException {

		SecureRandom sr = new SecureRandom();

		CipherKeyGenerator generator = new CipherKeyGenerator();
		generator.init(new KeyGenerationParameters(sr, 128));
		byte[] key = generator.generateKey();		
		byte[] keyhex = Hex.encode(key);
		return new String(keyhex).toUpperCase();

	}
}