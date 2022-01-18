package com.tai.chef.salefood.service;

import com.tai.chef.salefood.totp.TOTP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TotpService {

    public boolean verifyCode(String totpCode, String secret) {
        String totpCodeBySecret = generateTotpBySecret(secret);

        return totpCodeBySecret.equals(totpCode);
    }

    private String generateTotpBySecret(String secret) {
        // Getting current timestamp representing 30 seconds time frame
        long timeFrame = System.currentTimeMillis() / 1000L / 30;

        // Encoding time frame value to HEX string - required by TOTP generator which is used here.
        String timeEncoded = Long.toHexString(timeFrame);

        try {
            // Encoding given secret string to HEX string - required by TOTP generator which is used here.
            char[] secretEncoded = (char[]) new Hex().encode(secret);

            // Generating TOTP by given time and secret - using TOTP algorithm implementation provided by IETF.
            return TOTP.generateTOTP(String.copyValueOf(secretEncoded), timeEncoded, "6");
        } catch (EncoderException e) {
            log.error("EncoderException while generateTotpBySecret ", e);
            return StringUtils.EMPTY;
        }
    }
}
