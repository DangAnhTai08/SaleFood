package com.tai.chef.salefood.security.token;

import com.google.gson.Gson;
import com.tai.chef.salefood.common.CommonResource;
import com.tai.chef.salefood.dto.AuthDetail;
import com.tai.chef.salefood.security.services.UserDetailsImpl;
import com.tai.chef.salefood.util.GsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.tai.chef.salefood.config.Constants.AUTHORIZATION_HEADER;
import static com.tai.chef.salefood.config.Constants.TOKEN_PARAM;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String ALPHA_NUMERIC_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final RedisTemplate<String, String> redisTemplate;

    private final CommonResource commonResource;

    public String generateToken(int len) {
        StringBuilder builder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        while (len-- != 0) {
            int character = Math.abs(secureRandom.nextInt()) % ALPHA_NUMERIC_STRING.length();
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }

    public boolean saveTokenRedis(String token, UserDetailsImpl userDetail) {
        try {
            AuthDetail authDetail = new AuthDetail(userDetail);
            redisTemplate.opsForValue().set(token, GsonUtils.toJson(authDetail),
                    commonResource.getRedisTimeout(), TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception ex) {
            log.error("FAILED while saveTokenToRedis for username = {}", userDetail.getUsername(), ex);
            return false;
        }
    }

    public String getUsernameFromToken(String authToken) {
        try {
            if (StringUtils.isBlank(authToken) || authToken.length() < commonResource.getTokenLength())
                return null;

            String jsonValue = redisTemplate.opsForValue().get(authToken);
            if (StringUtils.isBlank(jsonValue))
                return null;

            AuthDetail authDetail = new Gson().fromJson(jsonValue, AuthDetail.class);

            return Objects.nonNull(authDetail) ? authDetail.getUsername() : null;
        } catch (Exception e) {
            log.error("Invalid token {} .", authToken, e);
            return null;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        bearerToken = request.getParameter(TOKEN_PARAM);
        return StringUtils.isNotBlank(bearerToken) ? bearerToken : StringUtils.EMPTY;
    }

    public void saveTOTPRedis(String otp, String username) {
        try {
            redisTemplate.opsForValue().set("OTP_" + username, otp, 30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("FAILED while saveTOTPRedis for username = {}", username, ex);
        }
    }

    public boolean isTOTPRedis(String otp, String username) {
        try {
            String totpRedis = redisTemplate.opsForValue()
                    .get("OTP_" + (Objects.isNull(username) ? StringUtils.EMPTY : username));

            return StringUtils.isNotBlank(totpRedis) && totpRedis.equals(otp);
        } catch (Exception ex) {
            log.error("FAILED while isTOTPRedis for username = {}", username, ex);
            return false;
        }
    }

    public boolean isNumberInputOTP(String username) {
        try {
            String otpRedis = redisTemplate.opsForValue().get("OTP_NUMBER_" + username);

            return StringUtils.isNotBlank(otpRedis) && Integer.parseInt(otpRedis) > 5;
        } catch (Exception ex) {
            log.error("FAILED while isNumberInputOTP for username = {}", username, ex);
            return false;
        }
    }

    public void saveNumberInputOTP(String otp, String username) {
        try {
            String numberOTPNotCorrect = redisTemplate.opsForValue().get("OTP_NUMBER_" + username);

            int newNumber = 1 + (Objects.nonNull(numberOTPNotCorrect) ? Integer.parseInt(numberOTPNotCorrect) : 0);

            redisTemplate.opsForValue().set("OTP_NUMBER_" + username,
                    String.valueOf(newNumber),
                    60,
                    TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("FAILED while saveNumberInputOTP for username = {}", username, ex);
        }
    }
}
