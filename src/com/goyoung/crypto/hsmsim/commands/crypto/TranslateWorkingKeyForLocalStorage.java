/**
 * 
 */
package com.goyoung.crypto.hsmsim.commands.crypto;

import com.goyoung.crypto.hsmsim.ServerProcess;
import com.goyoung.crypto.hsmsim.crypto.util.Get_KeyCheckValue_KCV;
import com.goyoung.crypto.hsmsim.crypto.util.Load2Part3DESKey_Variant_N;
import com.goyoung.crypto.hsmsim.crypto.util.ThreeTDEA_Decrypt_2;
import com.goyoung.crypto.hsmsim.crypto.util.ThreeTDEA_Encrypt;
import org.bouncycastle.util.encoders.Hex;


//TODO for sake of completeness add support for 1key-3DES (single-length)
public class TranslateWorkingKeyForLocalStorage {
	
/* Command 13 translates a working key from encryption using any KEK to encryption using the MFK. 
 * Use this command to import a working key from another node that uses Variant mode.
 * Your node and the remote node must have the same KEK. 
 * This command translates both1key-3DES (single-length) and 2key-3DES (double-length) working keys.	
 */
	
//Command 
//<13#Variant#EMFK.0(KEK)#EKEK.V(Working Key)#>

//Response
//<23#EMFK.V(Working Key)#Working Key Check Digits#>[CRLF]
	
	
	public static String Go(String sCommand) throws Exception  {
		int Variant = Integer.valueOf(Character.toString(sCommand.charAt(4)));

		//load enciphered KEK as HEX
		String s_E_KEK = sCommand.substring(6);//get the supplied encrypted KEK as hex string from s_Command
		String s_E_KEK_1= s_E_KEK.substring(0, s_E_KEK.length() - 35);//Trim off 4 chars of s_Command
		byte[] b_eKEK = Hex.decode(s_E_KEK_1);
		
		//load enciphered WorkingKey as HEX
		String s_E_Working_Key = sCommand.substring(39);
		String s_E_Working_Key_1= s_E_Working_Key.substring(0, s_E_Working_Key.length() - 2);//Trim off 4 chars of s_Command
		byte[] b_eWorkingKey = Hex.decode(s_E_Working_Key_1);
		
		
		//Load the MFK and Variant_N MFK
		byte[] bMFK = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, 0); // load LMK Variant 0 to Decrypt inbound KEK
		byte[] bMFK_Vn = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, Variant); // load VariantNN of LMK to Encrypt new Key
		
		//Decrypt the KEK from MFKv0
		byte[] b_dec_KEK = ThreeTDEA_Decrypt_2.Go(b_eKEK, bMFK);
		
		//Load KEK Variant N
		byte[] b_dec_KEK_Vn = Load2Part3DESKey_Variant_N.Go(Hex.toHexString(b_dec_KEK), Variant);
		
		//Decrypt the working key from
		byte[] b_dec_WorkingKey = ThreeTDEA_Decrypt_2.Go(b_eWorkingKey, b_dec_KEK_Vn);	
		
		
		byte[] b_re_enc_WorkingKey = ThreeTDEA_Encrypt.Go(b_dec_WorkingKey, bMFK_Vn);
		
		String sResult = Hex.toHexString(b_re_enc_WorkingKey).toUpperCase();

		
		return "<23#" + sResult+ "#" + Get_KeyCheckValue_KCV.Go(b_dec_WorkingKey) + "#>";
	}

}










