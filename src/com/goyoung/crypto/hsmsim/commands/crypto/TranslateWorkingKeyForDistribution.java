/**
 * 
 */
package com.goyoung.crypto.hsmsim.commands.crypto;

import org.bouncycastle.util.encoders.Hex;

import com.goyoung.crypto.hsmsim.ServerProcess;
import com.goyoung.crypto.hsmsim.crypto.util.Get_KeyCheckValue_KCV;
import com.goyoung.crypto.hsmsim.crypto.util.Load2Part3DESKey_Variant_N;
import com.goyoung.crypto.hsmsim.crypto.util.ReturnVariantFlags;
import com.goyoung.crypto.hsmsim.crypto.util.ThreeTDEA_Decrypt_2;
import com.goyoung.crypto.hsmsim.crypto.util.ThreeTDEA_Encrypt;

public class TranslateWorkingKeyForDistribution {
	
	
	//inbound command
	//<11#Variant#EMFK.0(KEK)#EMFK.V(Working Key)#>

	//response
	//<21#EKEK.V(Working Key)#Working Key Check Digits#>[CRLF]

	/**
	 *  Command 11 translates a working key of any type, from encryption using the specified
	 *  variant of the MFK to encryption using the specified variant of the KEK.
	 *  <11#Variant#EMFK.0(KEK)#EMFK.V(Working Key)#>
	 *  <21#EKEK.V(Working Key)#Working Key Check Digits#>[CRLF]
	 * @throws Exception 
	 */
	
// <11#Variant#EMFK.0(KEK)#EMFK.V(Working Key)#>	
//	Field # Contents Length (bytes) Legal Characters
//	0 Command identifier 2 11
//	1 Variant (V) 1, 2 0 - 31
//	2 EMFK.0(KEK)* 16, 32 0 - 9, A - F
//	3 EMFK.V(Working Key)* 16, 32 0 - 9, A - F
//	*Can be a volatile table location.
//	
//	The command looks like this:
//	<11#1#4791B313B61DAC09370BE7D920BF774C#AE86D417E64E07E0BC62A2AD72516EA1#>
//	The HSM returns the following response:
//	<21#A7CD84EEB2AA0737EFD23931DC36DEFF#08D7#>
	
	// TODO for sake of completeness make this work for 1key-3DES (single-length)
	
	@SuppressWarnings("unused")
	public static String Go(String sCommand) throws Exception  {
		int Variant = Integer.valueOf(Character.toString(sCommand.charAt(4)));
		// Fetch the variant flags using passed in variant int
		String s_Variant_Hex = ReturnVariantFlags.Go(Variant);
		
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
		
		byte[] b_dec_KEK = ThreeTDEA_Decrypt_2.Go(b_eKEK, bMFK);
		byte[] b_dec_WorkingKey = ThreeTDEA_Decrypt_2.Go(b_eWorkingKey, bMFK_Vn);	
		
		byte[] b_dec_KEK_Vn = Load2Part3DESKey_Variant_N.Go(Hex.toHexString(b_dec_KEK), Variant);

		byte[] b_re_enc_WorkingKey = ThreeTDEA_Encrypt.Go(b_dec_WorkingKey, b_dec_KEK_Vn);	
		
		String sResult = Hex.toHexString(b_re_enc_WorkingKey).toUpperCase();
		
		return "<21#" + sResult+ "#" + Get_KeyCheckValue_KCV.Go(b_dec_WorkingKey) + "#>";
	}

}










