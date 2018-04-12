/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package safefolder;

/**
 *
 * @author Prabhjeet
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.*;
import java.security.SecureRandom;


import java.security.spec.KeySpec;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class AESFileEncryption {
    public static void encrypt(String path,String pwd) throws Exception {

        FileOutputStream outFile;
        
        try ( 
                FileInputStream inFile = new FileInputStream(path)) {
            
            String fileName=path;
            
            System.out.println(path);
            
            outFile = new FileOutputStream(fileName+".aes");
            String password = pwd;
            byte[] salt = {
            (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
            (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
        };
           
            SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt,65536,128);// user-chosen password that can be used with password-based encryption (PBE).
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");//Secret KeySpec is a class and implements inteface SecretKey
           
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[16];
            random.nextBytes(bytes);
         IvParameterSpec ivspec = new IvParameterSpec(bytes);   
            cipher.init(Cipher.ENCRYPT_MODE, secret,ivspec);//opmode,key
            outFile.write(bytes);
            byte[] input = new byte[64];
            int bytesRead;
            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null){
                    Files.write(Paths.get(fileName+".aes"), output, StandardOpenOption.APPEND);
                }
            }   byte[] output = cipher.doFinal();
            if (output != null)
            {
                Files.write(Paths.get(fileName+".aes"), output, StandardOpenOption.APPEND);
        }}
        outFile.flush();
        outFile.close();
        File f=new File(path);
        boolean x=f.delete();
        if(x){
            System.out.println("File deleted");
        }
        JOptionPane.showMessageDialog(null,"File Encrypted.");
        
    }
}