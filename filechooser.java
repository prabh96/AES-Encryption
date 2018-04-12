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
import javax.swing.*;    
   
import java.io.*;    
public class filechooser extends JFrame{    
String selectPath()   
{    
    JFileChooser fc=new JFileChooser();    
    fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
    int i=fc.showOpenDialog(this);    
    if(i==JFileChooser.APPROVE_OPTION){    
        File f=fc.getSelectedFile();
        
        String filepath=f.getPath();
        System.out.println(filepath);
        return filepath;
}
    return null;
} 
}