
package com.deathrow.mymachine;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Vector;
import java.security.MessageDigest;
import java.util.Base64.Encoder;
import java.util.Base64;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;

public class FileSearchName {

	public static Vector<Vector<String>>  rowMove = new Vector<Vector<String>>();
	public static HashMap<String, String> movHashMap = new HashMap<String, String>();
	int dupNum = 0;
	int regNum = 0;
	static int dupCount = 0;
	static int moveCount = 0;
    static int fileNumber = 1;
    static String errorMessage ="";
	
	public static void searchFile(String folderToSearch, String searchName){
		try{
	            int i = 1;
	            DuplicateFileRemoverTwo mv = new DuplicateFileRemoverTwo();
	            long expectedSize = 1073741824;
	            File dir = new File(folderToSearch);
	            File[] fileList = dir.listFiles((FileFilter) new PrefixFileFilter(searchName, IOCase.INSENSITIVE));
	            for(File oneFile : fileList){
	                if(oneFile.isFile()){
	                	MessageDigest md = MessageDigest.getInstance("MD5");
						byte[] fileBytes = Files.readAllBytes(oneFile.toPath());
						String fileString = Base64.getEncoder().encodeToString(md.digest(fileBytes));
						Vector<String> row = new Vector<String>();
	                	movHashMap.put(fileString, oneFile.getAbsolutePath());
						row.add(Integer.toString(fileNumber++));
						row.add(oneFile.getName());
						long n1 = oneFile.length();
						row.add(mv.format(n1,0));
						rowMove.add(row);
	                }
	        	}
	        }catch (IOException | NoSuchAlgorithmException x){
				System.out.println("Error Message : "  + x.getMessage());
				errorMessage = errorMessage.concat("Error Message : "  + x.getMessage());
				JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
	        }
	}

	public Vector<Vector<String>> getRowMove() {
		return rowMove;
	}

	public HashMap<String, String> getMovHashMap() {
		return movHashMap;
	}
	
    public static void MoveFile(boolean state, String folderToPath){
		if(state == true){
			try{
					int numOfMovFiles = 0;
					for(String moveHashName : movHashMap.keySet()){
					Path pathOfFile = Paths.get(movHashMap.get(moveHashName));
					Path newdirectory = Paths.get(folderToPath);
					        
					String movingFileName = movHashMap.get(moveHashName);
					Files.move(pathOfFile, newdirectory.resolve(pathOfFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					numOfMovFiles++;
					}
				}catch(IOException x){
					System.out.println("Error Message : "  + x.getMessage());
					errorMessage = errorMessage.concat("Error Message : "  + x.getMessage());
					JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
				}
		}
	}
	
}
