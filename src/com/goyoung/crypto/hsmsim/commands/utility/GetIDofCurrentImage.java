package com.goyoung.crypto.hsmsim.commands.utility;

public class GetIDofCurrentImage {

	public static String Go(String sCommand) {
		if (sCommand.contains("<1101#>")) { //return a image version
			
			return "<2101#HSMSim BR549-VAR Version: 1.30, Date: Jun 10 2017, Time: 15:07:05#F06F#1#>";
		}
		else return "";
	}
}
