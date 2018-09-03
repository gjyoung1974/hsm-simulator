package com.goyoung.crypto.hsmsim.commands.utility;

public class ConfirmResettoFactoryState {
	public static String Go(String sCommand) {

		if (sCommand.contains("<1228#")) {

			return "<2228#ok#>";
		} else
			return "";
	}
}
