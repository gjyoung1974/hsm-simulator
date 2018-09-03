package com.goyoung.crypto.hsmsim.commands.utility;

        import com.goyoung.crypto.hsmsim.ServerProcess;

public class GetSystemInformation {
    public static String Go(String sCommand) {

        if (sCommand.contains("<1120#>")) {

            return "<2120#SerialNumber=" + ServerProcess.SerialNumber + "#ProductID=" + ServerProcess.ProductID +
                    "#LoaderVersion=" + ServerProcess.LoaderVersion + "#PersonalityVersion=" + ServerProcess.PersonalityVersion + "#>";
        } else return "";
    }
}
