package com.goyoung.crypto.hsmsim;

import java.io.IOException;

import com.goyoung.crypto.hsmsim.commands.*;
import com.goyoung.crypto.hsmsim.commands.crypto.EncryptPINANSIFormat0;
import com.goyoung.crypto.hsmsim.commands.crypto.GenerateCheckDigits;
import com.goyoung.crypto.hsmsim.commands.crypto.GenerateNewInitialKeyforPINPadUsingVISADUKPT;
import com.goyoung.crypto.hsmsim.commands.crypto.GenerateVISAWorkingKey;
import com.goyoung.crypto.hsmsim.commands.crypto.GenerateWorkingKeyAnyType;
import com.goyoung.crypto.hsmsim.commands.crypto.GetApplicationKeyCheckDigits;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslateAnEncryptedKeyBetweenECBAndCBCModes;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslateCommunicationsKeyforLocalStorageUsingASpecificVariant;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslatePIN;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslateWorkingKeyForDistribution;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslateWorkingKeyForLocalStorage;
import com.goyoung.crypto.hsmsim.commands.crypto.TranslateWorkingKeyforLocalStorageUnderCurrentMFKToPendingMFK;
import com.goyoung.crypto.hsmsim.commands.utility.ConfirmResettoFactoryState;
import com.goyoung.crypto.hsmsim.commands.utility.EchoTestMessage;
import com.goyoung.crypto.hsmsim.commands.utility.GetBatteryLifeRemaining;
import com.goyoung.crypto.hsmsim.commands.utility.GetIDofCurrentImage;
import com.goyoung.crypto.hsmsim.commands.utility.GetSystemConfigurationInformation;
import com.goyoung.crypto.hsmsim.commands.utility.GetSystemDateandTime;
import com.goyoung.crypto.hsmsim.commands.utility.GetSystemInformation;
import com.goyoung.crypto.hsmsim.commands.utility.ResettoFactoryState;
import com.goyoung.crypto.hsmsim.commands.utility.ReturnIPAddressOfHSM;
import com.goyoung.crypto.hsmsim.commands.utility.SPConfigurationStatus;
import com.goyoung.crypto.hsmsim.commands.utility.TCPIPSocketInformation;

public class CommandProcessor {

    public static String Go(String sCommand) throws Exception {

        /*
         * Conversions Commands:
         */

        if (sCommand.contains("<93#")) {
            return GenerateRandomNumber.go(sCommand);
        }

        if (sCommand.contains("<10#")) {
            try {
                return GenerateWorkingKeyAnyType.Go(sCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (sCommand.contains("<11#")) {
            return TranslateWorkingKeyForDistribution.Go(sCommand);
        }

        if (sCommand.contains("<13#")) {
            return TranslateWorkingKeyForLocalStorage.Go(sCommand);
        }

        //TODO finish implementing GenerateVISAWorkingKey
        if (sCommand.contains("<18#")) {
            return GenerateVISAWorkingKey.Go(sCommand);
        }

        //TODO finish implementing TranslateCommunicationsKeyforLocalStorage
        if (sCommand.contains("<19#")) {
            return GenerateVISAWorkingKey.Go(sCommand);
        }

        if (sCommand.contains("<1226#>")) {
            return GetApplicationKeyCheckDigits.Go(sCommand);
        }
        //TODO finish implementing TranslateWorkingKeyforDistributiontoNonHSMSimNode
        if (sCommand.contains("<1A#")) {
            return GenerateNewInitialKeyforPINPadUsingVISADUKPT.Go(sCommand);
        }

        //TODO finish implementing TranslateCommunicationsKeyforLocalStorageUsingASpecificVariant
        if (sCommand.contains("<1D#")) {
            return TranslateCommunicationsKeyforLocalStorageUsingASpecificVariant.Go(sCommand);
        }

        if (sCommand.contains("<1E#")) {
            return GenerateNewInitialKeyforPINPadUsingVISADUKPT.Go(sCommand);
        }

        if (sCommand.contains("<7E#")) {
            return GenerateCheckDigits.Go(sCommand);
        }

        //TODO finish implementing TranslateWorkingKeyforLocalStorageUnderCurrentMFKToPendingMFK
        if (sCommand.contains("<9E#")) {
            return TranslateWorkingKeyforLocalStorageUnderCurrentMFKToPendingMFK.Go(sCommand);
        }

        if (sCommand.contains("<30#")) {
            return EncryptPINANSIFormat0.Go(sCommand);
        }

        if (sCommand.contains("<31#")) {
            return TranslatePIN.Go(sCommand);
        }

        //TODO finish implementing TranslateAnEncryptedKeyBetweenECBAndCBCModes
        if (sCommand.contains("<113#")) {
            return TranslateAnEncryptedKeyBetweenECBAndCBCModes.Go(sCommand);
        }

        /*
         * Appliance utility functions
         */
        if (sCommand.contains("<9A#")) {
            return SPConfigurationStatus.go(sCommand);
        }

        if (sCommand.contains("<1227#RESET_TO_FACTORY_STATE#>")) {
            return ResettoFactoryState.Go(sCommand);
        }

        if (sCommand.contains("<1228#")) {
            return ConfirmResettoFactoryState.Go(sCommand);
        }

        if (sCommand.contains("<00#")) {
            return EchoTestMessage.Go(sCommand);
        }

        if (sCommand.contains("<1101#>")) {
            return GetIDofCurrentImage.Go(sCommand);
        }

        if (sCommand.contains("<1110#>")) {
            return GetSystemConfigurationInformation.Go(sCommand);
        }

        if (sCommand.contains("<1111#>")) {
            return GetSystemDateandTime.Go(sCommand);
        }

        if (sCommand.contains("<1120#>")) {
            return GetSystemInformation.Go(sCommand);
        }

        if (sCommand.contains("<1216#1#>")) {
            return GetBatteryLifeRemaining.Go(sCommand);
        }

        if (sCommand.contains("<1221#>")) {
            return ReturnIPAddressOfHSM.Go(sCommand);
        }

        if (sCommand.contains("<1223#>")) {
            return TCPIPSocketInformation.Go(sCommand);
        } else {
            return "";
        }
    }
}
