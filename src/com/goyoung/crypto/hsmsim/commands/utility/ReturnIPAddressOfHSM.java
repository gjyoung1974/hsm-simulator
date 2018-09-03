package com.goyoung.crypto.hsmsim.commands.utility;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class ReturnIPAddressOfHSM {
    public static String Go(String sCommand) throws UnknownHostException {

        if (sCommand.contains("<1221#>")) {

            return "<2221#" + Inet4Address.getLocalHost().getHostAddress() + "#>";
        } else
            return "";
    }
}
