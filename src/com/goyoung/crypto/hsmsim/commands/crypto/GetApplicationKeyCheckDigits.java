package com.goyoung.crypto.hsmsim.commands.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import com.goyoung.crypto.hsmsim.ServerProcess;
import com.goyoung.crypto.hsmsim.crypto.util.DesDEBC;
import com.goyoung.crypto.hsmsim.crypto.util.Load2Part3DESKey_No_IV;

public class GetApplicationKeyCheckDigits {
	public static String Go(String sCommand) {

		//TODO: get keys from a keystore or database:
		if (sCommand.contains("<1226#>")) {

			byte[] key = Load2Part3DESKey_No_IV.load(ServerProcess.LMK0x01);
					
			DESedeEngine engine = new DESedeEngine();
			PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(engine);
			encryptCipher.init(true, new KeyParameter(key));

			CipherParameters params = new KeyParameter(key);

			engine.init(true, params);// encrypt 16 null bytes to generate KCV value
			byte[] null_bytes_16 = new byte[16];
			DesDEBC desdebc = new DesDEBC(key);
			ByteArrayInputStream in = new ByteArrayInputStream(null_bytes_16);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				desdebc.encrypt(in, null_bytes_16.length, out);
			} catch (DataLengthException | ShortBufferException
					| IllegalBlockSizeException | BadPaddingException
					| IllegalStateException | InvalidCipherTextException e) {
				e.printStackTrace();
			}
			
			byte[] b_KCV = Arrays.copyOfRange(out.toByteArray(), 0, 2);
			String sKCV = new String(Hex.encode(b_KCV));

			return "<2226#MFK1=" + sKCV.toUpperCase() + "######>";
		} else
			return "";
	}
}
