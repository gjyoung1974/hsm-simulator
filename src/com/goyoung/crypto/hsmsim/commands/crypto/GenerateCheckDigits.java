package com.goyoung.crypto.hsmsim.commands.crypto;

public class GenerateCheckDigits {

	public static String Go(String sCommand) {
		if (sCommand.contains("<7E#")) {
			//TODO make this actually work
			return "<8E#A#E7B8A6#>";
		}
		else return "";
	}
}
