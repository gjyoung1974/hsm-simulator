package com.goyoung.crypto.hsmsim.commands.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

import com.goyoung.crypto.hsmsim.ServerProcess;
import com.goyoung.crypto.hsmsim.crypto.util.Load2Part3DESKey_Variant_N;
import com.goyoung.crypto.hsmsim.crypto.util.ThreeTDEA_Decrypt_2;

public class TranslatePIN {
	
	//TODO make this support 2Key 3DES In bound/out bound KPE's
	//TODO make this support all KeyBlock algorithms
	
/*
 * Command 31 translates an encrypted PIN block from encryption under an 
 * incoming PIN Encryption Key to an outgoing PIN Encryption Key. The translated PIN block will
 * be in ANSI PIN Block format. The incoming PIN Encryption key is designated as KPEI,
 * and the outgoing PIN Encryption Key is designated as KPE0. This command supports
 * 1key-3DES (single-length) or 2key-3DES (double-length) PIN Encryption Keys (KPE)s.
 * 
 * Command:
 * <31#PIN Block Type#EMFK.1(KPEI)#EMFK.1(KPEO)#EKPEI(PIN Block)#PIN Block Data#>
 * 
 * Response:
 * 
 * <41#EKPEO(ANSI PIN Block)#Sanity Check Indicator#[IBM 3624 Sequence Number#]>[CRLF]
 * 
 * The command looks like this:
 * <31#1#8C2A7691A708A88D#72E7AEF691471872#7B58719B354B147A#987654321012#>
 * 
 * The HSM returns the following response:
 * <41#06087B12E397F5B6#Y#>
 * 
 */

	
	//sample data
	// 8C2A7691A708A88D3D1DFD44D1AD3157  = KPE1
	// 72E7AEF6914718723D1DFD44D1AD3157 = KPE2
	
	// Clear-text ANSI PIN block: 0512 AC29 ABCD EFED. The PIN is 12345.
	// The encrypted incoming PIN block: 7B58 719B 354B 147A.
	
	// PIN block data; in this case, the 12 rightmost digits of the Primary Account Number
	// excluding the check digit: 9876 5432 1012.
	
	//<31#1#8C2A7691A708A88D3D1DFD44D1AD3157#72E7AEF6914718723D1DFD44D1AD3157#A59C88B971FD9CE95004F50BB474B0C2#987654321012#>
	
	// Command:
	// <31#PIN Block Type#EMFK.1(KPEI)#EMFK.1(KPEO)# EKPEI(PIN Block)#PIN Block Data#>

	// Resposne: 
	// <41#EKPEO(ANSI PIN Block)#Sanity Check Indicator# [IBM 3624 Sequence Number#]>[CRLF]
	
	public static String Go(String sCommand) throws InvalidKeyException, DataLengthException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,  IllegalBlockSizeException, BadPaddingException, IllegalStateException, InvalidCipherTextException, InvalidKeySpecException, ShortBufferException, InvalidAlgorithmParameterException, UnsupportedEncodingException  {
		
		String s_cmd_values = sCommand.substring(6);// get the supplied command values as hex string from s_Command
		s_cmd_values = s_cmd_values.substring(0, s_cmd_values.length() - 2); //Trim the <#> off the ends
		
		String s_pre_E_KPE_I = s_cmd_values.substring(0, 33);// Trim off 4 chars of s_Command
		String s_E_KPE_I = s_pre_E_KPE_I.substring(0, 16);// Trim off 4 chars of s_Command
		String s_E_KPE_O = s_pre_E_KPE_I.substring(17, 33);// Trim off 4 chars of s_Command
		
		String s_PIN_Block_EKPEI = s_cmd_values.substring(34, 50);// Trim off 4 chars of s_Command
		String s_PIN_Block_Data = s_cmd_values.substring(51, s_cmd_values.length());
		s_PIN_Block_Data = "0000" + s_PIN_Block_Data;
		
		byte[] b_E_KPE_I = Hex.decode(s_E_KPE_I);
		byte[] b_E_KPE_O = Hex.decode(s_E_KPE_O);
		byte[] b_PIN_Block_EKPEI = Hex.decode(s_PIN_Block_EKPEI);
		// byte[] b_PIN_Block_Data = Hex.decode(s_PIN_Block_Data);
				
		byte[] bMFK_v1 = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, 1); // load VariantNN of LMK to Encrypt new Key
	
		//Decrypt KPE I
		byte[] b_KPE_I = ThreeTDEA_Decrypt_2.Go(b_E_KPE_I, bMFK_v1);
		
		//Decrypt KPE O
		byte[] b_KPE_O = ThreeTDEA_Decrypt_2.Go(b_E_KPE_O, bMFK_v1);
	
		 SecretKeySpec   sk_I_KPE = new SecretKeySpec(b_KPE_I, "DES");
		 SecretKeySpec   sk_O_KPE = new SecretKeySpec(b_KPE_O, "DES");
		 
		Cipher dcipher = Cipher.getInstance("DES/ECB/NoPadding");
		dcipher.init(Cipher.DECRYPT_MODE, sk_I_KPE);
		byte[] b_Dec_Inbound_PIN_Block = dcipher.doFinal(b_PIN_Block_EKPEI);
	
		Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");
		ecipher.init(Cipher.ENCRYPT_MODE, sk_O_KPE);
		byte[] b_Enc_Outbound_PIN_Block = 		dcipher.doFinal(b_Dec_Inbound_PIN_Block);
	
		return "<41#" + Hex.toHexString(b_Enc_Outbound_PIN_Block).toUpperCase() +"#Y#>";

	}
	

}
