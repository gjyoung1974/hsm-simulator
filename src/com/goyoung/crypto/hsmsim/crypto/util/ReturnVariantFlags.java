package com.goyoung.crypto.hsmsim.crypto.util;

// To find the variant, Multiply the variant number by 0x08 (0x08 * variant 5 = 0x28), then XOR that to the first byte (and 17th byte if 3DES) of the clear key before encrypting or decrypting.
// To get an HSM that knows nothing of variants to accomplish the same thing, apply the variant to the clear key before importing into the HSM.
// For example, if you're encrypting a TMK under a ZMK, XOR 28000000000000002800000000000000 to the clear ZMK, then import it.

// A TMK, ZMK or KEK, all use variant 0 (equivalent to no variant at all since 0x08 * 0 = 0) 
// The vast majority of ATMs do not use variants. The most common use of variants 
// is for storing keys in a database encrypted under the appropriate variant of the MFK. 
// The Thales LMK key scheme 'U' serves the same purpose.

public class ReturnVariantFlags {

    public static String Go(int Variant) {

        if (Variant == 1) {
            return "0800000000000000";
        } else {

            String s_zeros = "0000000000000000";//string of 16 0's
            int i8 = 0x08;//Starting point variant 1 = 8 Hex

            //derive variant 0x8 X VariantNN = VariantFlag
            String s_iVariant = Integer.toHexString(i8 * Variant);
            String s_substring_zeros = s_zeros.substring(0, s_zeros.length() - s_iVariant.length());

            return s_iVariant + s_substring_zeros;
        }
    }
}
