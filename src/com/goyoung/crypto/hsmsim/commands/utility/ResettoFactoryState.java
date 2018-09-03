package com.goyoung.crypto.hsmsim.commands.utility;

public class ResettoFactoryState {

    public static String Go(String sCommand) {

        if (sCommand.contains("<1227#RESET_TO_FACTORY_STATE#>")) {

            //TODO implement something that resets the config file
            return "<2227#nnnnnn#>";
        } else
            return "";
    }
}
