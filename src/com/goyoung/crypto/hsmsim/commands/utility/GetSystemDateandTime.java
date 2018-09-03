package com.goyoung.crypto.hsmsim.commands.utility;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetSystemDateandTime {

    public static String Go(String sCommand) {
        if (sCommand.contains("<1111#>")) { //return a key not wrapped by MFK

            DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            //get current date time with Date()
            Date date = new Date();

            return "<2111#" + dateFormat.format(date) + "#>";
        } else return "";
    }
}
