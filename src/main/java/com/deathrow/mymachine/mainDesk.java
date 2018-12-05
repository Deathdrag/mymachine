package com.deathrow.mymachine;

import java.sql.Connection;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JDesktopPane;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Clock;
import java.time.Duration;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.commons.io.FilenameUtils;

import com.dewcis.utils.tableModel;

public class mainDesk extends JPanel implements  ActionListener{
	Logger log = Logger.getLogger(mainDesk.class.getName());

	Connection db = null;
	String mySql = "";
	
	List<JButton> btns;
	List<JTextField> txfs;
	List<JComboBox> cmbs;

	public  HashMap<String, String> hashmapFilesMP4 = new HashMap<String, String>();
	
	Map<String, String> fields;
	List<String> fieldNames;

	Vector<Vector<String>>  rowUnique,rowMove;
	Vector<String> columnMove,columnUnique;

	String folderToScan=null, folderToSearch= null,folderTo=null;
	String errorMessage ="";
	JFileChooser jFolder;
	int fileCount = 1;
	int dupCount = 0;
	private static JDialog d;

	JTabbedPane tabbedPane = new JTabbedPane();
	JTabbedPane searchPane = new JTabbedPane();
	FileSearchName mov = new FileSearchName();
	DuplicateFileRemoverTwo dup = new DuplicateFileRemoverTwo();
	JPanel nonRegPanel, dupPanel, mp3Panel, movePanel, logPanel, filterPanel, duplPanel, uniPanel;
	JPanel bttnMvPanel, buttonPanel, mvPanel, bttnMp3Panel, statusPanel, devicePanel, searchPanel;
	JTable tableReg, tableNon, tableUniq,	tableMv;
	DefaultTableModel mvModel, uniqModel;

	public mainDesk(Connection db) {
		super(new BorderLayout());
		this.db = db;

		btns = new ArrayList<JButton>();
		txfs = new ArrayList<JTextField>();
		cmbs = new ArrayList<JComboBox>();

		// AutoDetect2 auto = new AutoDetect2();
		// auto.autoDetect2();

		dupPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Duplicate Files", dupPanel);
		
		buttonPanel = new JPanel(new GridLayout(0, 4));

		addButton("Folder", 150, 20, 100, 25, true);
		addButton("Scan Duplicate", 300, 20, 150, 25, true);
		// addButton("Delete Duplicate", 500, 20, 150, 25, true);

		buttonPanel.add(btns.get(0));
		buttonPanel.add(btns.get(1));
		// buttonPanel.add(btns.get(2));

		dupPanel.add(buttonPanel, BorderLayout.PAGE_END);

		
		

		//movePanel panel
		movePanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Move Users Files", movePanel);

		bttnMvPanel = new JPanel(new GridLayout(0, 4));

		addButton("Folder Search", 150, 20, 100, 25, true);
		addButton("Search", 300, 20, 100, 25, true);
		addButton("Folder To", 500, 20, 150, 25, true);
		addButton("Move", 700, 20, 150, 25, true);

		bttnMvPanel.add(btns.get(2));
		bttnMvPanel.add(btns.get(3));
		bttnMvPanel.add(btns.get(4));
		bttnMvPanel.add(btns.get(5));

		movePanel.add(bttnMvPanel, BorderLayout.PAGE_END);

		searchPanel = new JPanel(new GridLayout(0, 8));
		addSearch("Search File Name", "");
		movePanel.add(searchPanel, BorderLayout.PAGE_START);

		// mvPanel = new JPanel(null);
		// mvPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Search Files"));
		// mvPanel.setBounds(5, 50, 200, 200);
		// movePanel.add(mvPanel);

		columnMove = new Vector<String>();
		columnMove.add("Number"); columnMove.add("File Name");  columnMove.add("Size");

		mvModel = new DefaultTableModel(rowMove,columnMove);
		tableMv = new JTable(mvModel);
		JScrollPane scrollPaneMove = new JScrollPane(tableMv);
		movePanel.add(scrollPaneMove, BorderLayout.CENTER);

		// //log user panel
		// logPanel = new JPanel(new BorderLayout());
		// tabbedPane.addTab("users Logs", logPanel);

		mp3Panel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Convert Files to MP3", mp3Panel);
		
		bttnMp3Panel = new JPanel(new GridLayout(0, 2));

		addButton("File Select", 150, 20, 100, 25, true);
		addButton("Convert", 300, 20, 150, 25, true);

		bttnMp3Panel.add(btns.get(6));
		bttnMp3Panel.add(btns.get(7));

		columnUnique = new Vector<String>();
		columnUnique.add("Number"); columnUnique.add("Files"); columnUnique.add("Size"); columnUnique.add("File Type");

		uniqModel = new DefaultTableModel(rowUnique,columnUnique);
		tableUniq = new JTable(uniqModel);
		JScrollPane scrollPaneDupl = new JScrollPane(tableUniq);
		mp3Panel.add(scrollPaneDupl, BorderLayout.CENTER);

		mp3Panel.add(bttnMp3Panel, BorderLayout.PAGE_END);

		super.add(tabbedPane, BorderLayout.CENTER);
	}

	public void addButton(String btTitle, int x, int y, int w, int h, boolean enabled) {
		JButton btn = new JButton(btTitle);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(this);
		btn.setEnabled(enabled);
		// buttonPanel.add(btn);
		btns.add(btn);
	}

	public void addSearch(String fieldTitle, String fieldValue) {
		JLabel lbTitle = new JLabel(fieldTitle + " : ");
		searchPanel.add(lbTitle);
		
		JTextField tfDevice = new JTextField();
		tfDevice.setText(fieldValue);
		searchPanel.add(tfDevice);
		txfs.add(tfDevice);
	}

	private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand().equals("Folder")) {

			jFolder = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int r = jFolder.showOpenDialog(null);

			// if the user selects a file 
            if (r == JFileChooser.APPROVE_OPTION){ 
                folderToScan = jFolder.getSelectedFile().getAbsolutePath();
				System.out.println("Folder Path: " + folderToScan);
            } 
			
		}else if(ev.getActionCommand().equals("Scan Duplicate")) {
	        dup.dupScan(folderToScan);
        }else if(ev.getActionCommand().equals("Delete Duplicate")) {

        	int a=JOptionPane.showConfirmDialog(null, "Are you sure? You want to Delete", "Delete", JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION){
				dup.delDuplicates(true);

				HashMap<String, String> duplicate = dup.getDupHashMap();
				HashMap<String, String> unique = dup.getHashmap();

				rowUnique = dup.getRowUnique();
				
			}  
        }else if(ev.getActionCommand().equals("Folder Search")) {

			JFileChooser jFolderSearch = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jFolderSearch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int r = jFolderSearch.showOpenDialog(null);

			// if the user selects a file 
            if (r == JFileChooser.APPROVE_OPTION){ 
                folderToSearch = jFolderSearch.getSelectedFile().getAbsolutePath();
			System.out.println("Folder To Search : " + folderToSearch);
            } 
        }else if(ev.getActionCommand().equals("Search")) {
        	int numCorrectFields =0;
        	final int NUM_FIELDS =2;
        	String searchValue =null;
        	String folderDirectory =null;
        	
			if (txfs.get(0).getText().isEmpty()){
				errorMessage = errorMessage.concat("Search Text Value is missing.\n");
				txfs.get(0).setText("");
				txfs.get(0).setBorder(new LineBorder(Color.red));
			}else{
				txfs.get(0).setBorder(null);
				numCorrectFields++;
				searchValue = txfs.get(0).getText();

			}

			if (folderToSearch == null){
				errorMessage = errorMessage.concat("Press The Folder Search Button To Select Folder To Search.\n");
				btns.get(3).setBorder(new LineBorder(Color.red));
			}else{
				btns.get(3).setBorder(null);
				numCorrectFields++;
				folderDirectory = folderToSearch;
				
			}

			if (numCorrectFields < NUM_FIELDS){
				JOptionPane.showMessageDialog(null,errorMessage,"Incoplete/Invalid Data Entered!",JOptionPane.ERROR_MESSAGE );
			}else{
				mov.searchFile(folderDirectory,searchValue);

				HashMap<String, String> movHashMap = mov.getMovHashMap();
				JOptionPane.showMessageDialog(null,movHashMap.size()+" Files With the name "+ searchValue + " Found","Files Details",JOptionPane.INFORMATION_MESSAGE);

				rowMove = mov.getRowMove();
				mvModel.setDataVector(rowMove, columnMove);
				mvModel.fireTableDataChanged();
			}
        }else if(ev.getActionCommand().equals("Folder To")) {

			JFileChooser jFolderTo = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jFolderTo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int r = jFolderTo.showOpenDialog(null);

			// if the user selects a file 
            if (r == JFileChooser.APPROVE_OPTION){ 
                folderTo = jFolderTo.getSelectedFile().getAbsolutePath();
			System.out.println("Folder To Move : " + folderTo);
            } 
        }else if(ev.getActionCommand().equals("Move")) {
        	int numCorrectFields =0;
        	final int NUM_FIELDS =1;
        	String folderDirectoryTo =null;
        	if (folderTo == null){
				errorMessage = errorMessage.concat("Press The Folder To Button To Select Folder To Search.\n");
				btns.get(5).setBorder(new LineBorder(Color.red));
			}else{
				btns.get(5).setBorder(null);
				numCorrectFields++;
				folderDirectoryTo = folderTo;
			}

			if (numCorrectFields < NUM_FIELDS){
				JOptionPane.showMessageDialog(null,errorMessage,"Incoplete/Invalid Data Entered!",JOptionPane.ERROR_MESSAGE );
			}else{
				int a=JOptionPane.showConfirmDialog(null, "Are you sure? You want to Move", "Move", JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION){
					mov.MoveFile(true,folderTo);
				}  
			}
        }else if(ev.getActionCommand().equals("File Select")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(true);
			chooser.showOpenDialog(null);
			File[] fileList = chooser.getSelectedFiles();
			Vector<String> row = new Vector<String>();
			rowUnique = new Vector<Vector<String>>();
			int fileUniqNonUniq = 1;
			for(File oneFile : fileList){
				if(oneFile.isFile()){
					String duplicate = oneFile.getAbsolutePath();
					hashmapFilesMP4.put(Integer.toString(dupCount++),duplicate);
					row.add(Integer.toString(fileUniqNonUniq++));
					row.add(oneFile.getName());
					long n2 = oneFile.length();
					row.add(dup.format(n2,0));
					row.add(getFileExtension(oneFile));
					rowUnique.add(row);
				}
				
			}

			uniqModel.setDataVector(rowUnique, columnUnique);
			tableUniq.repaint();
			tableUniq.revalidate();

			System.out.println("fileList : " + Arrays.toString(fileList));
        }
	}
	
}
