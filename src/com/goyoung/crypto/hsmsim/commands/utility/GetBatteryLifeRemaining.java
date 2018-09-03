package com.goyoung.crypto.hsmsim.commands.utility;

public class GetBatteryLifeRemaining {
    public static String Go(String sCommand) {

        if (sCommand.contains("<1216#1#>")) {
            return "<2216#1#2553#>";
        } else
            return "";
    }
}
