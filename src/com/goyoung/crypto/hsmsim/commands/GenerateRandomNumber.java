package com.goyoung.crypto.hsmsim.commands;

import java.math.BigInteger;
import java.security.SecureRandom;


public class GenerateRandomNumber {

    private static SecureRandom random = new SecureRandom();

    public static String go(String sCommand) {


        //TODO make this dynamically select the numbits for the RNG
        if (sCommand.contains("<93#>")) { //return a random number
            return "<A3#" + new BigInteger(64, random).toString(16).toUpperCase() + "#>";
        }

        if (sCommand.contains("<93#H#4#>")) { //return a byte digit random number
            return "<A3#" + new BigInteger(16, random).toString(16).toUpperCase() + "#>";
        }

        if (sCommand.contains("<93#D#6#>")) { //return a byte digit random number
            return "<A3#" + new BigInteger(24, random).toString(16).toUpperCase() + "#>";
        } else return "";
    }

}
