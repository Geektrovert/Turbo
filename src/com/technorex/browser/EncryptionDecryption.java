package com.technorex.browser;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

class EncryptionDecryption {
    private final static String key = "abcdefgh";
    /**
     * The @param key must be at least 8 bytes
     * @param in the string
     * @param out file output stream
     * @throws Exception for illegalKeyException
     */
    static void encrypt(String in, File out) throws Exception{
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
     * @param in the string
     * @throws Exception for illegalKeyException
     */
    static String decrypt(File in) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(in);
        File temp = new File(System.getProperty("user.dir")+"\\src\\data\\nts\\tmp");
        FileOutputStream fileOutputStream = new FileOutputStream(temp);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKey,SecureRandom.getInstance("SHA1PRNG"));
        CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream,cipher);
        write(fileInputStream,cipherOutputStream);
        Scanner scanner = new Scanner(temp);
        String data = "";
        while (scanner.hasNextLine())
            data = data.concat(scanner.nextLine()+"\n");
        scanner.close();
        if(temp.delete()) return data;
        return data;
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
