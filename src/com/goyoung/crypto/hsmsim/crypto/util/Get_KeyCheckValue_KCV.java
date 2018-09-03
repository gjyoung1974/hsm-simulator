package com.goyoung.crypto.hsmsim.crypto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

public class Get_KeyCheckValue_KCV {

    public static String Go(byte[] keyhex) throws DataLengthException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, IllegalStateException, InvalidCipherTextException {

        byte[] null_bytes_16 = new byte[16]; // same as: byte[] _iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        DesDEBC_Cipher desdebc = new DesDEBC_Cipher(keyhex);
        ByteArrayInputStream in = new ByteArrayInputStream(null_bytes_16);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        desdebc.Encrypt(in, out);
        byte[] b_KCV = Arrays.copyOfRange(out.toByteArray(), 0, 2);

        return new String(Hex.encode(b_KCV)).toUpperCase();
    }

}
