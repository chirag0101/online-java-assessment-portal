package com.iris.OnlineCompilerBackend.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAHashingUtil {

    public static String hashUsingSHA256(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(s.getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}