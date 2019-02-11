package com.bebound.template.cryto;

public class CrytoUtils {

    public static String decyptByDefault(String encryptedMsg){
        String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        return AESCrypt.decrypt(key, initVector, encryptedMsg);
    }

}
