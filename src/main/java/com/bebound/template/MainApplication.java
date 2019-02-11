package com.bebound.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@SpringBootApplication
public class MainApplication {

    /**
     * The default port with Spring is 8080: please consider to modify it in a real project
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        Logger.getAnonymousLogger().info("SERVER IS LAUNCHED");

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