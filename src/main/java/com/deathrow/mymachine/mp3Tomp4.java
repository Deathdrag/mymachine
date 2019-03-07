package com.deathrow.mymachine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.security.MessageDigest;
import javax.swing.JOptionPane;
import java.util.Base64;
import java.nio.file.Files;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import java.util.List;
import javax.swing.SwingWorker;
import java.util.List;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Mp3ToMp4{
	
	public  HashMap<String, String> hashmapFilesMP4 = new HashMap<String, String>();
	public  HashMap<String, String> hashmapFilesMP3 = new HashMap<String, String>();
	public  Vector<Vector<String>>  rowUnique = new Vector<Vector<String>>();
	public  Vector<Vector<String>>  rowFileUnique = new Vector<Vector<String>>();
	public  Vector<Vector<String>>  rowDelete = new Vector<Vector<String>>();


	int dupNum = 0;
	int regNum = 0;
	int dupCount = 0;
	int fileCount;
	int fileSize = 0 ;
	JFrame frame;

	public  void mp3mp4(File[] fileList) {
       frame = new JFrame("Progress...");
       JPanel panel = new JPanel();
       JLabel label = new JLabel("Loading...");
       JProgressBar jpb = new JProgressBar();
       jpb.setIndeterminate(false);
       int max = 100;
       jpb.setMaximum(max);
       panel.add(label);
       panel.add(jpb);
       frame.add(panel);
       frame.pack();
       frame.setSize(250,90);
       frame.setLocationRelativeTo(null);
       frame.setVisible(true);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       new Task_IntegerUpdateMp3(jpb, max, label,fileList).execute();
   }

   class Task_IntegerUpdateMp3 extends SwingWorker<Void, Integer> {

       JProgressBar jpb;
       int max;
       JLabel label;
       File[] fileList;

       public Task_IntegerUpdateMp3(JProgressBar jpb, int max, JLabel label, File[] fileList) {
           this.jpb = jpb;
           this.max = max;
           this.label = label;
           this.fileList = fileList;
       }

       @Override
       protected void process(List<Integer> chunks) {
           int i = chunks.get(chunks.size()-1);
           jpb.setValue(i); // The last value in this array is all we care about.
           label.setText("Loading " + i + "%  of  " + max +"%" );
       }

       @Override
       protected Void doInBackground() throws Exception {
	        try{  
	            long expectedSize = 314572800;
				int fileNumber = 0;
				int fileUnique = 1;
				int fileUniqNonUniq = 1;
				fileSize = fileList.length;  
	            System.out.println("Total Number Of Files : "  + fileSize);
	            String userName = System.getProperty("user.name");
	            new File("C:\\Users\\"+userName+"\\Music\\DragMP4TOMP3").mkdirs();
				
				// rowUnique.clear();
				for(File oneFile : fileList){

					if(oneFile.isFile()){
						if(fileCount <=100) {
		                    long size = oneFile.length();
		                    if(size<=expectedSize){

								String line;
								Path pathDir = Paths.get("C:\\Users\\"+userName+"\\Music\\DragMP3");

								if (Files.notExists(pathDir)) {
									
                                   new File("C:\\Users\\"+userName+"\\Music\\DragMP3").mkdirs();
									
								}

								int last = oneFile.getName().lastIndexOf(".");
								String fileName = last >= 1 ? oneFile.getName().substring(0, last) : oneFile.getName();
								System.out.println("file name ="+fileName);


								File fileExist = new File("C:\\Users\\"+userName+"\\Music\\DragMP3\\"+fileName+".mp3");
								if (fileExist.delete()) {
									System.out.println("delete file ="+fileName);
								}


								File source = new File(oneFile.getAbsolutePath());
								String ext = FilenameUtils.getExtension(oneFile.getAbsolutePath());
								File dest   = new File("C:\\Users\\"+userName+"\\Music\\DragMP4TOMP3\\Dragfile."+ext);

								FileUtils.copyFile(source, dest);
                                                              

								String mp4File = dest.getAbsolutePath();
								System.out.println(mp4File);
								String mp3File = "C:\\Users\\"+userName+"\\Music\\DragMP3\\Dragfile.mp3";

								// ffmpeg -i input.mp4 output.avi as it's on www.ffmpeg.org
								String cmd = "ffmpeg -i " + mp4File+ " " + mp3File;
								Process prs = Runtime.getRuntime().exec(cmd);
								BufferedReader in = new BufferedReader(
								new InputStreamReader(prs.getErrorStream()));
								while ((line = in.readLine()) != null) {
								System.out.println(line);
								}
								prs.waitFor();
								System.out.println("Video converted successfully!");
								in.close();
								
								fileNumber++;
								Vector<String> row = new Vector<String>();
								row.add(Integer.toString(fileUniqNonUniq++));
								row.add(oneFile.getName());
								long n1 =mp3File.length();
								row.add(format(n1,0));
								row.add("mp3");
								fileCount = fileNumber*100/fileSize;
								rowUnique.add(row);

								Path pathOfFile = Paths.get(mp3File);
								Files.move(pathOfFile, pathOfFile.resolveSibling(fileName+".mp3"));
								dest.delete();
								

			                }
		                     
						}
						
					}
				   
				   int i = fileCount;
				   publish(i);
				}

				File dir = new File("C:\\Users\\"+userName+"\\Music\\DragMP4TOMP3");
				if (dir.isDirectory()==true) {
					dir.delete();
				}
			}catch (IOException x){
				System.out.println("Error Message : "  + x.getMessage());
				String errorMessage = "Error Message : "  + x.getMessage();
				JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
			}
           return null;
       }

       @Override
       protected void done() {
           try {
               get();
               frame.dispose();
			
				JOptionPane.showMessageDialog(null, "Total Number of Files converted is "+fileSize,"File mp4 to mp3",JOptionPane.INFORMATION_MESSAGE);
           } catch (ExecutionException | InterruptedException e) {
               e.printStackTrace();
           }
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

	public Vector<Vector<String>> getRowUnique() {
		return rowUnique;
	}
	

   
}