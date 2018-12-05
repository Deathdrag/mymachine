package com.deathrow.mymachine;

import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class AutoDetect2 {

    public static void autoDetect2() {
    String[] letters = new String[]{ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
                                    "N", "O" , "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    File[] drives = new File[letters.length];
    boolean[] isDrive = new boolean[letters.length];

    // init the file objects and the initial drive state
        for ( int i = 0; i < letters.length; ++i ) {
            drives[i] = new File(letters[i]+":/");

            isDrive[i] = drives[i].canRead();
        }

    System.out.println("FindDrive: waiting for devices...");

    // loop indefinitely
     while(true){
            // check each drive 
            for ( int i = 0; i < letters.length; ++i ){
            FileSystemView fsv = FileSystemView.getFileSystemView();
            boolean pluggedIn = drives[i].canRead();
            boolean pluggedIn1 = drives[i].canWrite();
            
                // if the state has changed output a message
                if ( pluggedIn != isDrive[i] ){
                        if ( pluggedIn && pluggedIn1){
                            long n1 = drives[i].getTotalSpace();
                            long n2 = drives[i].getUsableSpace();
                            long n3 = drives[i].getFreeSpace();
                            long n4 = (drives[i].getTotalSpace()) - (drives[i].getUsableSpace());
                            JOptionPane.showMessageDialog(null,"Drive: " + letters[i]+"\nDisplay name: " + fsv.getSystemDisplayName(drives[i])
                                                         +"\nIs drive: " + fsv.isDrive(drives[i])+"\nIs floppy: " + fsv.isFloppyDrive(drives[i])
                                                         +"\nTotal space: " +format(n1,0) +"\nFree space: " +format(n3,0)+"\nUsable space: " +format(n2,0) +"\nUsed space: " +format(n4,0),"New Drive Detected!",JOptionPane.ERROR_MESSAGE );
                           
                        }else
                            JOptionPane.showMessageDialog(null,"Drive: " + letters[i],"Drive Removed!",JOptionPane.ERROR_MESSAGE );

                        isDrive[i] = pluggedIn;
                        
                }
            }

        // wait before looping
        try { Thread.sleep(100); }
        catch (InterruptedException e) { /* do nothing */ }

        }
    }
    public static String format(double bytes, int digits) {
        String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }
    
}
