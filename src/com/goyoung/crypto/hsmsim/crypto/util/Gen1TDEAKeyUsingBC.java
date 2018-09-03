package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class Gen1TDEAKeyUsingBC {

	public static String Go() throws DataLengthException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IllegalStateException, InvalidCipherTextException {
		  SecureRandom sr = new SecureRandom();


			CipherKeyGenerator generator = new CipherKeyGenerator();
			generator.init(new KeyGenerationParameters(sr, 128));
			byte[] key = generator.generateKey();

			DESedeEngine engine = new DESedeEngine();
			PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(engine);
			encryptCipher.init(true, new KeyParameter(key));

			CipherParameters params = new KeyParameter(key);

			engine.init(true, params);//encrypt 16 0 bytes to generate KCV value
			
			byte[] null_bytes_16 = new byte[16]; // same as: byte[] _iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; 
			//byte[] keyhex = Hex.encode(key);

			DesDEBC desdebc = new DesDEBC(key);
			ByteArrayInputStream in = new ByteArrayInputStream(null_bytes_16);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			desdebc.encrypt(in, null_bytes_16.length, out);
			byte[] b_KCV = Arrays.copyOfRange(out.toByteArray(), 0, 2);
			byte[] b_sKey = Arrays.copyOfRange(key, 0, 8);

		return new String(Hex.encode(b_sKey)).toUpperCase() + "##" + new String(Hex.encode(b_KCV)).toUpperCase();


	}

}
