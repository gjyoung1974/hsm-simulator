package com.goyoung.crypto.hsmsim.crypto.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.encoders.Hex;

public class Get_KeyCheckValue_KCV_1DES {

    public static String Go(Key keyhex) throws IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException {

        // Zero vector used to Calculate KCV
        byte[] null_bytes_16 = new byte[16]; // same as: byte[] _iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0 };

        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, keyhex);

        // Encrypt the vector + grab first 4 char
        byte[] enc_16_0_Vector = desCipher.doFinal(null_bytes_16);
        byte[] b_KCV = Arrays.copyOfRange(enc_16_0_Vector, 0, 2);

        return new String(Hex.encode(b_KCV)).toUpperCase();
    }
}
