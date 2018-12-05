// package com.deathrow.mymachine;

// import java.io.File;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Vector;
// import java.security.MessageDigest;
// import javax.swing.JOptionPane;
// import java.util.Base64;
// import java.nio.file.Files;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.security.NoSuchAlgorithmException;
// import java.util.ArrayList;
// import java.awt.BorderLayout;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import javax.swing.JProgressBar;
// import java.util.List;
// import javax.swing.SwingWorker;
// import java.util.List;
// import java.util.concurrent.ExecutionException;
// import javax.swing.JPanel;

// public class DuplicateFileRemoverTwo{
	
// 	public  HashMap<String, String> hashmapFilesMP4 = new HashMap<String, String>();
// 	public  HashMap<String, String> hashmapFilesMP3 = new HashMap<String, String>();
// 	public  Vector<Vector<String>>  rowUnique = new Vector<Vector<String>>();
// 	public  Vector<Vector<String>>  rowFileUnique = new Vector<Vector<String>>();
// 	public  Vector<Vector<String>>  rowDelete = new Vector<Vector<String>>();


// 	int dupNum = 0;
// 	int regNum = 0;
// 	int dupCount = 0;
// 	int fileCount;
// 	int fileSize = 0 ;
// 	JFrame frame;

// 	public  void dupScan(String[] filesListMP) {
//         frame = new JFrame("Progress...");
//         JPanel panel = new JPanel();
//         JLabel label = new JLabel("Loading...");
//         JProgressBar jpb = new JProgressBar();
//         jpb.setIndeterminate(false);
//         int max = 100;
//         jpb.setMaximum(max);
//         panel.add(label);
//         panel.add(jpb);
//         frame.add(panel);
//         frame.pack();
//         frame.setSize(250,90);
//         frame.setLocationRelativeTo(null);
//         frame.setVisible(true);
//         frame.setResizable(false);
//         frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//         // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         new Task_IntegerUpdate(jpb, max, label,filesListMP).execute();
//     }

//     class Task_IntegerUpdate extends SwingWorker<Void, Integer> {

//         JProgressBar jpb;
//         int max;
//         JLabel label;
//         String[] filesListMP;

//         public Task_IntegerUpdate(JProgressBar jpb, int max, JLabel label,  String[] filesListMP) {
//             this.jpb = jpb;
//             this.max = max;
//             this.label = label;
//             this.filesListMP = filesListMP;
//         }

//         @Override
//         protected void process(List<Integer> chunks) {
//             int i = chunks.get(chunks.size()-1);
//             jpb.setValue(i); // The last value in this array is all we care about.
//             label.setText("Loading " + i + "%  of  " + max +"%" );
//         }

//         @Override
//         protected Void doInBackground() throws Exception {
// 	        try{  
// 	            long expectedSize = 314572800;
// 				File[] fileList = filesListMP;
// 				int fileNumber = 0;
// 				int fileUnique = 1;
// 				int fileUniqNonUniq = 1;
// 				fileSize = fileList.length;  
// 	            System.out.println("Total Number Of Files : "  + fileSize);   
				
// 				// rowUnique.clear();
// 				for(File oneFile : fileList){

// 					if(oneFile.isFile()){
// 						if(fileCount <=100) {
// 		                    long size = oneFile.length();
// 		                    if(size<=expectedSize){
// 							MessageDigest md = MessageDigest.getInstance("MD5");
// 							byte[] fileBytes = Files.readAllBytes(oneFile.toPath());
// 							String fileString = Base64.getEncoder().encodeToString(md.digest(fileBytes));
// 							Vector<String> row = new Vector<String>();
// 							Vector<String> uniquerow = new Vector<String>(); 
// 								if(hashmap.containsKey(fileString)){ 
// 									String original = hashmap.get(fileString);
// 									String duplicate = oneFile.getAbsolutePath();
// 									dupHashMap.put(Integer.toString(dupCount++),duplicate);
// 									fileNumber++;
// 									row.add(Integer.toString(fileUniqNonUniq++));
// 									row.add(oneFile.getName());
// 									long n2 = oneFile.length();
// 									row.add(format(n2,0));
// 									row.add("YES");
// 									fileCount = fileNumber*100/fileSize;
// 								}else{
// 									hashmap.put(fileString, oneFile.getAbsolutePath());
// 									fileNumber++;
// 									row.add(Integer.toString(fileUniqNonUniq++));
// 									row.add(oneFile.getName());
// 									long n1 = oneFile.length();
// 									row.add(format(n1,0));
// 									row.add("NO");
// 									uniquerow.add(Integer.toString(fileUnique++));
// 									uniquerow.add(oneFile.getName());
// 									uniquerow.add(format(n1,0));
// 									uniquerow.add("NO");
// 									rowFileUnique.add(uniquerow);
// 									fileCount = fileNumber*100/fileSize;
// 								}

// 								rowUnique.add(row);

// 			                }
		                     
// 						}
// 						// else if(oneFile.isDirectory()){
// 						// 	dupScan(oneFile.getAbsolutePath());
// 						// }
						
// 					}
				   
// 				   int i = fileCount;
// 				   publish(i);
// 				}
// 			}catch (IOException | NoSuchAlgorithmException x){
// 				System.out.println("Error Message : "  + x.getMessage());
// 				String errorMessage = "Error Message : "  + x.getMessage();
// 				JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
// 			}
//             return null;
//         }

//         @Override
//         protected void done() {
//             try {
//                 get();
//                 frame.dispose();
// 	            HashMap<String, String> duplicate = getDupHashMap();
// 				HashMap<String, String> unique = getHashmap();

// 				Vector<Vector<String>> rowUnique = getRowUnique();

// 				String message ="";
// 				message = message.concat("Number of Unique Files : " + unique.size() +".\n");
// 				message = message.concat("Number of Duplicate Files : " + duplicate.size());
// 				JOptionPane.showMessageDialog(null,message,"Files Details",JOptionPane.INFORMATION_MESSAGE);
// 				if (duplicate.size()>0) {
// 					DuplicateDesk dp = new DuplicateDesk(rowUnique, "Delete", duplicate, rowFileUnique);
// 				}else {
// 					DuplicateDesk dp = new DuplicateDesk(rowUnique,"No Delete",duplicate, rowFileUnique);
// 				}
				 
//             } catch (ExecutionException | InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }


//     }
	
// 	public static String format(double bytes, int digits) {
//         String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
//         int index = 0;
//         for (index = 0; index < dictionary.length; index++) {
//             if (bytes < 1024) {
//                 break;
//             }
//             bytes = bytes / 1024;
//         }
//         return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
//     }

// 	public HashMap<String, String> getDupHashMap() {
// 		return dupHashMap;
// 	}

// 	public HashMap<String, String> getHashmap() {
// 		return hashmap;
// 	}

// 	public Vector<Vector<String>> getRowUnique() {
// 		return rowUnique;
// 	}
	
// 	public  void delDuplicates(boolean state){
// 		if(state == true){
// 			try{
// 				ArrayList<String> delList = new ArrayList<>();
// 				int numOfDelFiles = 0;
// 				for(String dupHashName : dupHashMap.keySet())
// 				{
// 					Path pathOfFile = Paths.get(dupHashMap.get(dupHashName));
// 					String dupFileName = dupHashMap.get(dupHashName);
// 					Files.delete(pathOfFile);
// 					delList.add(dupHashName);
// 					numOfDelFiles++;
// 				}

// 				for(int i=0; i<delList.size();i++){
// 					dupHashMap.remove(delList.get(i));
// 				}

// 				JOptionPane.showMessageDialog(null,numOfDelFiles + " of Duplicate Files Are Deleted","Files Details",JOptionPane.INFORMATION_MESSAGE);
// 			}catch(Exception x){
// 				System.out.println("Error Message : "  + x.getMessage());
// 				String errorMessage = "Error Message : "  + x.getMessage();
// 				JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
// 			}
// 		}
// 	}

    
// }