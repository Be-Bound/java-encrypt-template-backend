package com.bebound.template.cryto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class CrytoUtils {

    public static void init() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            keyGenerator.init(168);
            SecretKey secretKey = keyGenerator.generateKey();
            Logger.getAnonymousLogger().info("secretKey = " + secretKey.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
