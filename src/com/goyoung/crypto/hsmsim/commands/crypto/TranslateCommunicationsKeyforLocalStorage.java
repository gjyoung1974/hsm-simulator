package com.goyoung.crypto.hsmsim.commands.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

import com.goyoung.crypto.hsmsim.ServerProcess;
import com.goyoung.crypto.hsmsim.crypto.util.*;
import org.jpos.iso.ISOUtil;

public class TranslateCommunicationsKeyforLocalStorage {

			// Generates a variety of working keys. The
			// command returns the generated key in two
			// forms: one for storing locally, encrypted under
			// the specified variant of the MFK, and one for
			// transmitting to another HSM node, encrypted
			// under the specified variant of the KEK.
	
	public static String Go(String sCommand) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, DataLengthException, IllegalStateException,
			InvalidCipherTextException, InvalidKeySpecException,
			ShortBufferException, NoSuchProviderException, IOException {

		String sKey="";
		
		//if there is nothing between the #KEK# delimiters, skip encrypting working key with KEK/MFK
		if (sCommand.matches("<19#\\d##\\w#>")) { // return a key not wrapped by KEK
			int Variant = Integer.valueOf(Character.toString(sCommand.charAt(4)));
			
			if (sCommand.contains("#D#>")) {
				sKey = new String(Gen2TDEAKeyUsingBC.Go()).toUpperCase();
			} 
			else if (sCommand.contains("#S#>")) {
				sKey = new String(GenDESKey.Go()).toUpperCase();
			}
					
			// Fetch the variant flags using passed in variant int
			String s_Variant_Hex = ReturnVariantFlags.Go(Variant);
			byte[] b_working_key = Hex.decode(sKey);
			
			//XOR the working key with the requested variant flag(s):
			byte[] b_working_key_VNN = ISOUtil.xor(b_working_key, DatatypeConverter.parseHexBinary(s_Variant_Hex + s_Variant_Hex));
			
			String s_KCV="";
			
			if (sCommand.contains("#D#>")) {
				s_KCV = "#" + Get_KeyCheckValue_KCV.Go(b_working_key_VNN);
			} 
			
			else if (sCommand.contains("#S#>")) {
				 SecretKey sk_working_key_VNN = new SecretKeySpec(b_working_key_VNN, "DES");		 
				 s_KCV =	"#" + Get_KeyCheckValue_KCV_1DES.Go(sk_working_key_VNN);
			}
			
			return "<29#" + new String(Hex.encode(b_working_key_VNN)).toUpperCase() +"#" + s_KCV + "#>";
		}

		
		// Generate a 2key-3DES (double-length) PIN Encryption Key.** Variant: 1
		// Example: application sends this request: <19#1#9007B8751BB7AB4EE355AF51A716113F#D#>
		// <command 10, # variant 1 # Kek encrypted under variant of MFK..# Double Length Key #>
		//Clear-text KEK: 0123 4567 89AB CDEF FEDC BA98 7654 3210.
		//The KEK encrypted under variant 0 of the MFK: 9007 B875 1BB7 AB4E 0B17 6C3E BEED 18AF.

		if (sCommand.matches(".*<19#\\d#.*")) {
			int Variant = Integer.valueOf(Character.toString(sCommand.charAt(4)));// get the variant value at position 4

			// Fetch the variant flags from properties file
			String s_Variant_Hex = ReturnVariantFlags.Go(Variant);

			if (sCommand.contains("#D#>")) // has the request called for a double length key?
			{
				sKey = new String(Gen2TDEAKeyUsingBC.Go()).toUpperCase();
			} else //if (sCommand.contains("#S#>")) // has the request called for a single length key?
			{
				sKey = new String(GenDESKey.Go()).toUpperCase();
			}

			//encode string version of Working Key to byte[]
			//to make it easier to work with
			byte[] b_working_key = Hex.decode(sKey);
			
			//XOR the working key with the requested variant flag(s):
			//TODO do we need to do this to working key when KEK/MFK are loaded as Variant? Are all three keys supposed to be variant?
			byte[] b_working_key_VNN = ISOUtil.xor(b_working_key, DatatypeConverter.parseHexBinary(s_Variant_Hex + s_Variant_Hex));
			String s_KCV="";
			String s_E_KEK = sCommand.substring(6);//get the supplied encrypted KEK as hex string from s_Command
			String s_E_KEK_1=""; //= s_E_KEK.substring(0, s_E_KEK.length() - 4);//Trim off 4 chars of s_Command
			
			if (sCommand.contains("#D#>")) {
				s_KCV = Get_KeyCheckValue_KCV.Go(b_working_key_VNN);
				
			} 
			else if (sCommand.contains("#S#>")) { //single length key, 
				 SecretKey sk_working_key_VNN = new SecretKeySpec(b_working_key_VNN, "DES");
				 s_KCV = Get_KeyCheckValue_KCV_1DES.Go(sk_working_key_VNN);//get KCV using DES
			}
			
			//Command without #S#> or #D#>
			StringBuilder sb = new StringBuilder();
			if (s_E_KEK.length() == 18) { // if less than 3key length repeat leftmost key portion of the key at end
				s_E_KEK_1 = s_E_KEK.substring(0, s_E_KEK.length() - 2);
				sb.append(s_E_KEK_1);
				sb.append(s_E_KEK_1);//add the beginning Hexstring key portion to the end
				s_E_KEK_1 = sb.toString();
				 SecretKey sk_working_key_VNN = new SecretKeySpec(b_working_key_VNN, "DES");
				 s_KCV = Get_KeyCheckValue_KCV_1DES.Go(sk_working_key_VNN);//get KCV using DES
			}else{
				
				s_E_KEK_1 = s_E_KEK.substring(0, s_E_KEK.length() - 4);//Trim off 4 chars of s_Command
				
			}
			
		    //Encrypted KEK	
			byte[] b_eKEK = Hex.decode(s_E_KEK_1);//return encrypted KEK as byte[]
		
			//plain-text MFK Variant 0
			byte[] bMFK = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, 0); // load LMK Variant 0 to decrypt inbound KEK
			
			//plain-text MFK Variant 1
			byte[] bMFK_v1 = Load2Part3DESKey_Variant_N.Go(ServerProcess.LMK0x01, 1); // load VariantNN of LMK to encrypt new Key
			
			//decrytpted kek
			String s_KEK = ThreeTDEA_Decrypt.Go(b_eKEK, bMFK);

			/*
			 * Encrypt the new "Working Key" Variant_NN of the MFK and KEK
			 * 
			 */
			DesDEBC desdebc1 = new DesDEBC(bMFK_v1);// encrypt the new key under VariantNN of MKF
			ByteArrayInputStream in1 = new ByteArrayInputStream(b_working_key);
			ByteArrayOutputStream out1 = new ByteArrayOutputStream();
			desdebc1.encrypt(in1, b_working_key.length, out1);
		
			//Load VariantNN of the KEK
			byte[] b_KEK = Hex.decode(s_KEK);//load KEK Hex String to byte array
			byte[] bKEKX2 = ByteBuffer.allocate(b_KEK.length + b_KEK.length).put(b_KEK).put(b_KEK).array();//Concatenate b_KEK + b_KEK double up the plaintext KEK
			byte[] bKEKX3 = ISOUtil.xor(bKEKX2, DatatypeConverter.parseHexBinary(s_Variant_Hex + s_Variant_Hex));// load VariantNN of the doubled up KEK

			// encrypt working key under VariantNN of KEK
			DesDEBC desdebc2 = new DesDEBC(bKEKX3);
			ByteArrayInputStream in2 = new ByteArrayInputStream(b_working_key);
			ByteArrayOutputStream out2 = new ByteArrayOutputStream();
			desdebc2.encrypt(in2, b_working_key.length, out2);
			
			/*
			 * Prepare the result and return as string:
			 * 
			 */
			//return the eMFK.VariantNN(MFK) and eMFK.VariantNN(KEK) versions of the local key respectively
			byte[] eMFK_V_Working_Key = out1.toByteArray();//Arrays.copyOfRange(out1.toByteArray(), 0, 16);
			byte[] eKEK_V_Working_Key = out2.toByteArray();//Arrays.copyOfRange(out2.toByteArray(), 0, 16);

			return "<29#" + new String(Hex.encode(eMFK_V_Working_Key)).toUpperCase() + "#" + new String(Hex.encode(eKEK_V_Working_Key)).toUpperCase() + "#" + s_KCV + "#>";

		}

		return "";
	}
}
