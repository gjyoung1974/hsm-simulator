package com.goyoung.crypto.hsmsim.commands.utility;

public class EchoTestMessage {
	public static String Go(String sCommand) {

		if (sCommand.contains("<00#")) {
			sCommand = sCommand.substring(4);
			return "<00#000028#" + sCommand;
		} else
			return "";
	}
}
