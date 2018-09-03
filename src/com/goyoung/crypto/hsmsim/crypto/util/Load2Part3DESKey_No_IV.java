package com.goyoung.crypto.hsmsim.crypto.util;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class Load2Part3DESKey_No_IV {

	public static byte[] load(String s_Key)  {

		byte[] b_Key2 = DatatypeConverter.parseHexBinary(s_Key);

		DESedeEngine engine = new DESedeEngine();
		PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(engine);
		encryptCipher.init(true, new KeyParameter(b_Key2));

		KeyParameter kp = new KeyParameter(b_Key2);

		return kp.getKey();
	}
}
