package com.example.demo;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Service
public class EncryptDecryptService {


    // using RSA algorithm
    public static String encrypt(String message, String publicKeyString) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }


    // using RSA algorithm
    public static String decrypt(String encryptedMessage, String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString.getBytes());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
    }

// hybrid encryption
//    public static String encrypt(String message, String publicKeyString) throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256);
//        SecretKey aesKey = keyGen.generateKey();
//        Cipher aesCipher = Cipher.getInstance("AES");
//
//        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
//        byte[] encryptedMessage = aesCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
//        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString.getBytes());
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
//
//        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());
//        byte[] combined = new byte[encryptedAesKey.length + encryptedMessage.length];
//        System.arraycopy(encryptedAesKey, 0, combined, 0, encryptedAesKey.length);
//        System.arraycopy(encryptedMessage, 0, combined, encryptedAesKey.length, encryptedMessage.length);
//        return Base64.getEncoder().encodeToString(combined);
//    }



// hybrid encryption
//    public static String decrypt(String encryptedMessage, String privateKeyString) throws Exception {
//        byte[] combined = Base64.getDecoder().decode(encryptedMessage.getBytes(StandardCharsets.UTF_8));
//        int rsaKeySize = 256;
//        byte[] encryptedAesKey = new byte[rsaKeySize];
//        byte[] encryptedMessageBytes = new byte[combined.length - rsaKeySize];
//
//        System.arraycopy(combined, 0, encryptedAesKey, 0, rsaKeySize);
//        System.arraycopy(combined, rsaKeySize, encryptedMessageBytes, 0, combined.length - rsaKeySize);
//        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString.getBytes());
//        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
//
//        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
//
//
//        System.out.println("Decrypted AES key length: " + aesKeyBytes.length);
//        SecretKeySpec aesKey = new SecretKeySpec(aesKeyBytes, "AES");
//        Cipher aesCipher = Cipher.getInstance("AES");
//        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
//        byte[] decryptedMessageBytes = aesCipher.doFinal(encryptedMessageBytes);
//
//        return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
//    }
}