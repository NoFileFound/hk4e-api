package org.httpsrv.thirdparty;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.httpsrv.conf.Config;

public class TwilioApi {
    public static void sendSms(String toPhoneNumber, String messageBody) {
        if(Config.getPropertiesVar().twilioToken.isEmpty()) return;

        Twilio.init(Config.getPropertiesVar().twilioSID, Config.getPropertiesVar().twilioToken);
        Message.creator(new com.twilio.type.PhoneNumber(toPhoneNumber), new com.twilio.type.PhoneNumber(Config.getPropertiesVar().twilioPhone), messageBody).create();
    }
}