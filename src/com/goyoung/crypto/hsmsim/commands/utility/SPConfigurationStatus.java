package com.goyoung.crypto.hsmsim.commands.utility;

public class SPConfigurationStatus {

    public static String go(String sCommand) {

        // TODO parse the config from a properties file
        if (sCommand.contains("<9A#CONFIG-ON#>")) { //Simulate a static 9A-config on command for now
            return "<AA#D18NN4#CONFIG-ON#1A,55,95,97,98,11B,(4A),(63),(6C),(88),(A0)=\""
                    + "4\",(A1)=\"S\",(A2)=\"B\",(E0)=\"ACDIKMPSVcdmn\",(E1)=\"ACDIKMPSVcdmn\",(E2)#00,10,31,"
                    + "70,71,72,73,74,7E,7F,93,99,9A,9E,9F,101,105,106,107,108,109#0000000000000001#>";
        }

        if (sCommand.contains("<9A#CONFIG-OFF#>")) { //Simulate a static 9A-config on command for now
            return "<AA#JL0585#CONFIG-OFF#14,15,16,18,19,1A,1C,1D,1E,1F,30,33," +
                    "34,35,36,37,38,39,3A,3D,3F,55,58,59,5D,5F,75,76,77,78,79,7A," +
                    "7B,90,94,95,96,97,98,B1,B2,B3,B4,B5,B6,BA,BB,BC,BD,BE,BF,D0," +
                    "D1,D2,D3,D4,D5,D6,D7,D8,D9,DA,110,111,112,114,115,11D,11E," +
                    "301,302,306,307,308,309,30A,30B,30C,30D,30E,30F,319,31A,31B,31C,31D,31E,31F,321,32A,32B,32C,332,333,334,336,337,338,339," +
                    "33A,33B,33C,33D,33E,33F,349,34A,34B,34C,34D,34E,34F,351,35B," +
                    "35C,35E,360,361,362,363,364,370,371,372,37A,37B,381,382,386," +
                    "388,3A1,3A2,3A3,3A4,3B2,3B3,3B4,3B5,3EA,3FA,(46),(47),(48)," +
                    "(49),(4B),(4C),(4D),(4E),(4F),(60),(61),(64),(65),(66),(68)," +
                    "(69),(6A),(6B),(6C),(6E),(6F),(80),(81),(82),(83),(84),(87)," +
                    "(88),(89),(8A),(8B),(8D)#(20),(21),(23),(27),(44)#00000000000" +
                    "00001#>";
        }

        if (sCommand.contains("<9A#CONFIG-ALL#>")) { //Simulate a static 9A-config on command for now
            return "<AA#JL0585#CONFIG-ALL#14,15,16,18,19,1A,1C,1D,1E,1F,30,33,34," +
                    "35,36,37,38,39,3A,3D,3F,55,58,59,5D,5F,75,76,77,78,79,7A,7B," +
                    "90,94,95,96,97,98,B1,B2,B3,B4,B5,B6,BA,BB,BC,BD,BE,BF,D0,D1," +
                    "D2,D3,D4,D5,D6,D7,D8,D9,DA,110,111,112,114,115,11D,11E,301," +
                    "302,306,307,308,309,30A,30B,30C,30D,30E,30F,319,31A,31B,31C," +
                    "31D,31E,31F,321,32A,32B,32C,332,333,334,336,337,338,339,33A," +
                    "33B,33C,33D,33E,33F,349,34A,34B,34C,34D,34E,34F,351,35B,35C," +
                    "35E,360,361,362,363,364,370,371,372,37A,37B,381,382,386,388," +
                    "3A1,3A2,3A3,3A4,3B2,3B3,3B4,3B5,3EA,3FA,(46),(47),(48),(49)," +
                    "(4B),(4C),(4D),(4E),(4F),(60),(61),(62),(63),(64),(65),(66)," +
                    "(68),(69),(6A),(6B),(6C),(6E),(6F),(80),(81),(82),(83),(84)," +
                    "(87),(88),(89),(8A),(8B),(8D),(A0)=\"4\",(A1)=\"S\",(A2)=\"S\"#00," +
                    "10,11,12,13,17,31,32,5C,5E,70,71,72,73,74,7E,7F,93,99,9A,9B," +
                    "9C,9E,9F,101,105,106,107,108,109,113,335,348,350,352,354,356," +
                    "357,359,35A,35F,(20),(21),(23),(27),(44)#0000000000000001#>";

        }

        if (sCommand.contains("<9A#COUNT#>")) { //return 9A ID string
            return "<AA#D126XL#1#0010-0000000050#0011-0000000040#>";
        }

        if (sCommand.contains("<9A#DIAGTEST#0##>")) { //return 9A ID string
            return "<AA#OK#>";
        }

        if (sCommand.contains("<9A#ID#>")) { //return 9A ID string
            return "<AA#JL012S#A10160V,62,63#4,S,S#00#10,11,12,13,17#31,32#5C,5E#70,71,72,73,74,7E,7F#93,99,9A,9B,9C,9E,9F###101,105,106,107,108,109#113######335#348#350,352,354,356,357,359,35A,35F#########>";
        }

        if (sCommand.contains("<9A#KEY#>")) {
            return "<AA#9999#MFK1#057A#D#PMFK1#2590#D####50B0#D##>";
        }

        if (sCommand.contains("<9A#CLEAR_LOG#>")) {
            return "<AA#DONE#>";
        } else return "";
    }

}
