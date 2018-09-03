/**
 *
 */
package com.goyoung.crypto.hsmsim.commands.crypto;

import com.goyoung.crypto.hsmsim.crypto.util.*;
import org.bouncycastle.util.encoders.Hex;
import org.jpos.security.jceadapter.JCESecurityModule;

import com.goyoung.crypto.hsmsim.ServerProcess;

public class EncryptPINANSIFormat0 {

    /**
     * The command looks like this:
     * <30#47F102C2D4DE29C41DE1CF689E9699D63D1DFD44D1AD3157#12345678901#000234567890#>
     * <p>
     * Response looks like this <40#EKPE(ANSI PIN Block)#>[CRLF]
     *
     * @throws Exception
     */

    public static String Go(String sCommand) throws Exception {

        byte ANSI_PIN_Block = (byte) 01;
        String s_cmd_values = sCommand.substring(4);// get the supplied encrypted KEK as hex string from s_Command
        s_cmd_values = s_cmd_values.substring(0, s_cmd_values.length() - 2);
        String s_E_KPE = s_cmd_values.substring(0, 48);// Trim off 4 chars of
        // s_Command
        String s_PIN_PAN = s_cmd_values.substring(49, s_cmd_values.length());// Trim off 4 chars of  s_Command
        String s_PIN = s_PIN_PAN.substring(0, 11);// Trim off 4 chars of  s_Command
        String s_PAN_1 = s_PIN_PAN.substring(12, s_PIN_PAN.length());// Trim off 4 chars of s_Command

        // Encrypted KEK
        byte[] b_E_KPE = Hex.decode(s_E_KPE);// return encrypted KEK as byte[]

        // plain-text MFK Variant 1
        byte[] bMFK_v1 = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, 1); // load VariantNN of LMK to encrypt new Key

        // decrytpted KPE + first 16 char of 16 = 3Key
        String KPE_2_key = ThreeTDEA_Decrypt.Go(b_E_KPE, bMFK_v1);
        String s_KPE = KPE_2_key + KPE_2_key.substring(0, 16);
        byte[] b_KPE = Hex.decode(s_KPE);

        // Command 30 Response 40: Encrypt PIN
        // Field # Contents Length (bytes) Legal Characters
        // 0 Response identifier. 2 40
        // 1 EKPE(ANSI PIN Block). 16 0- 9, A - F
        // <40#EKPE(ANSI PIN Block)#>
        // <40#054935D6E2DA00E2#>

        JCESecurityModule JCESM = new JCESecurityModule();

        // generate the clear pin block
        byte[] b_Clear_Pin_Block = JCESM.encryptPIN(s_PIN, s_PAN_1).getPINBlock(); // JCESM.calculatePINBlock(s_PIN, ANSI_PIN_Block, s_PAN_1);

        // encrypt the pin block with the decrypted KPE
        byte[] b_E_Pin_Block = ThreeTDEA_Encrypt.Go(b_Clear_Pin_Block, b_KPE);

        // return Hexdecimal string version of ecrypted pinblock
        return "<40#" + new String(Hex.encode(b_E_Pin_Block)).toUpperCase() + "#>";

    }

}
