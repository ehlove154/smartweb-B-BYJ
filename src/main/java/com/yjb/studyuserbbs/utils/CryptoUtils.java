package com.yjb.studyuserbbs.utils;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* password 암호화 로직 */

@UtilityClass
public class CryptoUtils {
    public static String hashSha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            return String.format("%0128x", new BigInteger(1, md.digest()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
