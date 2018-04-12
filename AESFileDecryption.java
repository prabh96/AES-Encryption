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
import java.io.IOException;
import java.io.File;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class AESFileDecryption {
    public static void decrypt(String path,String pwd) throws Exception {

            String password = pwd;
            String fileNameWithOutExt = path.replaceFirst("[.][^.]+$", "");
            byte[] salt = {
            (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
            (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
        };
        
        FileInputStream fis = new FileInputStream(path);
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt,65536,128);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher cipher;
        byte bytes[]=new byte[16];
        int q=fis.read(bytes, 0, 16);
        System.out.println(q);
        IvParameterSpec ivspec = new IvParameterSpec(bytes);
        
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
        
        
        FileOutputStream fos = new FileOutputStream(fileNameWithOutExt);
        try{
            
        byte[] in   = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }
        
        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);
        fis.close();
        fos.flush();
        fos.close();
        File f=new File(path);
        boolean x=f.delete();
        JOptionPane.showMessageDialog(null,"File Decrypted.");
    }
    catch(IOException | BadPaddingException | IllegalBlockSizeException e)
    {
        fis.close();
        fos.flush();
        fos.close();
        File f=new File(fileNameWithOutExt);
        boolean x=f.delete();
        System.out.print(x);
        JOptionPane.showMessageDialog(null,e+"\n"+"Wrong password!!!");
    }
    }
}