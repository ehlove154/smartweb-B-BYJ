package com.yjb.test250425.services;

import com.yjb.test250425.results.CryptoResult;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class CryptoService {
    public Pair<CryptoResult, String> cryptoHash(String op, String plaintext) {
        if (op == null) {
            return Pair.of(CryptoResult.FAILURE, "Invalid operation");
        }
        String result = switch (op) {
            case "sha1" -> CryptoService.hashSha1(plaintext);
            case "sha256" -> CryptoService.hashSha256(plaintext);
            case "sha512" -> CryptoService.hashSha512(plaintext);
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };
        return Pair.of(CryptoResult.SUCCESS, result);
    }
    
    public static String hashSha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashSha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
