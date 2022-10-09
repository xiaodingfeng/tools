package org.xiaobai.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

public class JasyptUtil {

    public static void testEncrypt() {

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("0000");
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "0000";
        String encryptText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptText);
    }

    public static void testDecrypt() {

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("0000");
        standardPBEStringEncryptor.setConfig(config);
        String encryptText = "r5c/qpcCtAH7ADfHmyI/7GAQ4251dQbjLLal/7IedII=";
        String plainText = standardPBEStringEncryptor.decrypt(encryptText);
        System.out.println(plainText);
    }

    public static void main(String[] args) {

        System.out.println("========加密后的密文========");
        testEncrypt();
        System.out.println("========解密后的明文========");
        testDecrypt();
    }
}

