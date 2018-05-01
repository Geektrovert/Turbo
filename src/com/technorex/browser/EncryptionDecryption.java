package com.technorex.browser;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

class EncryptionDecryption {
    /**
     * The @param key must be at least 8 bytes
     * @param key encryption key
     * @param in the string
     * @param out file output stream
     * @throws Exception for illegalKeyException
     */
    static void encrypt(String key, String in, File out) throws Exception{
        InputStream inputStream = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream,cipher);
        write(cipherInputStream,fileOutputStream);
    }
    /**
     * The @param key must be at least 8 bytes
     * @param key decryption key
     * @param in the string
     * @param out file output stream
     * @throws Exception for illegalKeyException
     */
    public static void decrypt(String key, File in, File out) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(in);
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream,cipher);
        write(fileInputStream,cipherOutputStream);
    }
    private static void write(InputStream inputStream, OutputStream outputStream) throws Exception{
        byte[] buffer = new byte[64];
        int numberOfBytesRead;
        while ((numberOfBytesRead = inputStream.read(buffer))!= -1){
            outputStream.write(buffer,0,numberOfBytesRead);
        }
        outputStream.close();
        inputStream.close();
    }
}
