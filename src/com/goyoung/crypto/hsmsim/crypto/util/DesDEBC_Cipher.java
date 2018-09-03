package com.goyoung.crypto.hsmsim.crypto.util;


import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DesDEBC_Cipher {

    private PaddedBufferedBlockCipher enc;
    private PaddedBufferedBlockCipher dec;

    // Buffers used to transport the bytes from one stream to another
    private byte[] ibuff = new byte[8];       //input buffer - block size length
    private byte[] obuf = new byte[512];    //output buffer

    byte[] key; //the key

    public DesDEBC_Cipher(byte[] keyBytes) {
        key = new byte[keyBytes.length];
        System.arraycopy(keyBytes, 0, key, 0, keyBytes.length);
        InitCiphers();
    }

    private void InitCiphers() {
        enc = new PaddedBufferedBlockCipher(new DESedeEngine());
        enc.init(true, new KeyParameter(key));

        dec = new PaddedBufferedBlockCipher(new DESedeEngine());
        dec.init(false, new KeyParameter(key));

    }

//    public void ResetCiphers() {
//        if (enc != null)
//            enc.reset();
//        if (enc != null)
//            dec.reset();
//    }

    public void Encrypt(InputStream in, OutputStream out)
            throws
            DataLengthException,
            IllegalStateException {
        processor(in, out);
    }

    public void Decrypt(InputStream in, OutputStream out)
            throws DataLengthException, IllegalStateException {
        processor(in, out);
    }

    private void processor(InputStream in, OutputStream out) {

        try {
            // Bytes read from in are to be either encrypted or decrypted
            // Read in the decrypted bytes from in InputStream and and
            // write them in cleartext to out OutputStream

            int noBytesRead = 0;        //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(ibuff)) >= 0) {
                noBytesProcessed = dec.processBytes(ibuff, 0, noBytesRead, obuf, 0);
                out.write(obuf, 0, noBytesProcessed);
            }

            noBytesProcessed = dec.doFinal(obuf, 0);
            out.write(obuf, 0, noBytesProcessed);

            out.flush();
        } catch (IOException | InvalidCipherTextException e) {
            System.out.println(e.getMessage());
        }
    }
}