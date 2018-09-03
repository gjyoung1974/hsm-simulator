package com.goyoung.crypto.hsmsim.crypto.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

public class GenDESKey {

    public static String Go() throws DataLengthException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IllegalStateException, InvalidCipherTextException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey key = keygenerator.generateKey();

        // byte[] b_sKey = Arrays.copyOfRange(key.getEncoded(), 0, 8);
        return new String(Hex.encode(key.getEncoded())).toUpperCase();

    }
}
