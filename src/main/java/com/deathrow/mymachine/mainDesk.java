package com.deathrow.mymachine;

import java.sql.Connection;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPasswordField;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import java.security.MessageDigest;
import java.util.Base64.Encoder;
import java.util.Base64;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;

import org.json.JSONObject;
import org.json.JSONArray;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class mainDesk extends JPanel implements  ActionListener{
	static final Logger log = Logger.getLogger(mainDesk.class.getName());

	Connection db = null;
	String mySql = "";
	long id = 0;
	
	List<JButton> btns;
	List<JTextField> txfs;
	List<JLabel> lbls;
	List<JTextField> detalis;
	List<JPasswordField> pwDetalis;
	List<JComboBox> cmb;
	List<JLabel> lblPhoto;
	List<JDesktopPane> dsk;

	public  HashMap<String, String> hashmapFilesMP4 = new HashMap<>();
	
	Map<String, String> fields;
	List<String> fieldNames;
	Map<String, String> userFields;
	Vector<Vector<String>>  rowUnique = new Vector<Vector<String>>();
	Vector<Vector<String>>  rowMove;
	Vector<String> columnMove,columnUnique;

	String folderToScan=null, folderToSearch= null,folderTo=null;
	String errorMessage ="";
	JFileChooser jFolder;
	File[] fileList = null;
	int fileCount = 1;
	int dupCount = 0;
	private static JDialog d;

	JTabbedPane tabbedPane = new JTabbedPane();
	JTabbedPane searchPane = new JTabbedPane();
	AutoDetect2 auto = new AutoDetect2();
	FileSearchName mov = new FileSearchName();
	DuplicateFileRemoverTwo dup = new DuplicateFileRemoverTwo();
	Mp3ToMp4 mp3 = new Mp3ToMp4();
	JPanel nonRegPanel, dupPanel, mp3Panel, movePanel, moveFilePanel, regPanel;
	JPanel bttnMvPanel, buttonPanel, mvPanel, bttnMp3Panel, cambtPanel, bttnCamPanel, bttnMoveFilePanel, camPanel, debPanel, bttnRegPanel, detailPanel, searchPanel, fpPanel;
	JTable tableReg, tableNon, tableUniq,	tableMv;
	DefaultTableModel mvModel, uniqModel;

	public mainDesk(Connection db) {
		super(new BorderLayout());
		this.db = db;

		new Task_IntegerUpdate().execute();
		// new Task_SpeechRecognizerMain().execute();

		btns = new ArrayList<JButton>();
		lbls = new ArrayList<JLabel>();
		txfs = new ArrayList<JTextField>();
		detalis = new ArrayList<JTextField>();
		pwDetalis = new ArrayList<JPasswordField>();
		cmb = new ArrayList<JComboBox>();
		userFields = new HashMap<String, String>();

		//Duplicate File Panel
		dupPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Duplicate Files", dupPanel);
		
		buttonPanel = new JPanel(new GridLayout(0, 4));

		addButton(buttonPanel, "Folder", 150, 20, 100, 25, true);
		addButton(buttonPanel, "Scan Duplicate", 300, 20, 150, 25, true);
		// addButton("Delete Duplicate", 500, 20, 150, 25, true);

		// buttonPanel.add(btns.get(0));
		// buttonPanel.add(btns.get(1));
		// buttonPanel.add(btns.get(2));

		dupPanel.add(buttonPanel, BorderLayout.PAGE_END);


		//move panel
		movePanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Move Users Files", movePanel);

		bttnMvPanel = new JPanel(new GridLayout(0, 4));

		addButton(bttnMvPanel, "Folder Search", 150, 20, 100, 25, true);
		addButton(bttnMvPanel, "Search", 300, 20, 100, 25, true);
		addButton(bttnMvPanel, "Folder To", 500, 20, 150, 25, true);
		addButton(bttnMvPanel, "Move", 700, 20, 150, 25, true);

		// bttnMvPanel.add(btns.get(2));
		// bttnMvPanel.add(btns.get(3));
		// bttnMvPanel.add(btns.get(4));
		// bttnMvPanel.add(btns.get(5));

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

		//move File Panel
		moveFilePanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Move Files to respective Folders", moveFilePanel);

		bttnMoveFilePanel = new JPanel(new GridLayout(0, 2));

		addButton(bttnMoveFilePanel,"File Select", 150, 20, 100, 25, true);
		addButton(bttnMoveFilePanel, "Convert", 300, 20, 150, 25, true);

		// bttnMoveFilePanel.add(btns.get(6));
		// bttnMoveFilePanel.add(btns.get(7));

		moveFilePanel.add(bttnMoveFilePanel, BorderLayout.PAGE_END);

		// mp3 Panel
		mp3Panel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Convert Files to MP3", mp3Panel);
		
		bttnMp3Panel = new JPanel(new GridLayout(0, 2));

		addButton(bttnMp3Panel, "File Select", 150, 20, 100, 25, true);
		addButton(bttnMp3Panel, "Convert", 300, 20, 150, 25, true);

		// bttnMp3Panel.add(btns.get(8));
		// bttnMp3Panel.add(btns.get(9));

		columnUnique = new Vector<String>();
		columnUnique.add("Number"); columnUnique.add("Files"); columnUnique.add("Size"); columnUnique.add("File Type");

		uniqModel = new DefaultTableModel(rowUnique,columnUnique);
		tableUniq = new JTable(uniqModel);
		JScrollPane scrollPaneDupl = new JScrollPane(tableUniq);
		mp3Panel.add(scrollPaneDupl, BorderLayout.CENTER);

		mp3Panel.add(bttnMp3Panel, BorderLayout.PAGE_END);

		//User Registration Panel
		regPanel = new JPanel(null);
		tabbedPane.addTab("User Registration", regPanel);

		detailPanel = new JPanel(null);
		addPanel(regPanel, detailPanel, "User Details",5, 15, 800, 235);

		Vector<String> role = new Vector<String>();
		role.add("None");
		role.add("Player");
		role.add("User1");
		role.add("User2");

		Vector<String> genderCMB = new Vector<String>();
		genderCMB.add("None");
		genderCMB.add("Male");
		genderCMB.add("Female");


		// fields = new LinkedHashMap<String, String>();
		// fields.put("entity_id", "Entity ID");
		// fields.put("surname", "Surname");
		// fields.put("first_name", "First Name");
		// fields.put("middle_name", "Middle Name");
		// fields.put("date_of_birth", "D.o.B");
		// fields.put("gender", "Gender");
		// fields.put("phone", "Phone Number");
		// fields.put("user_name", "user name");
		// fields.put("user_password", "Password");
		// fields.put("picture_file", "Picture");
		// fields.put("bio_metric_finger_print1", "Finger Print 1");
		// fields.put("bio_metric_finger_print2", "Finger Print 2");
		// fields.put("admin_role", "Role");
		// fields.put("details", "Details");
		// fieldNames = new ArrayList<String>(fields.keySet());
		
		
		addField("surname", "Surname", 400, 10, 105, 20, 150);
		addField("first_name", "First Name", 10, 40, 105, 20, 150);
		addField("middle_name", "Middle Name", 400, 40, 105, 20, 150);
		addField("phone", "Phone Number", 10, 70, 105, 20, 150);
		addCombox("gender", "Gender", genderCMB, 400, 70, 105, 20, 150);
		addPassword("user_password", "Password", 10, 100, 105, 20, 150);
		addField("user_name", "user name", 400, 100, 105, 20, 150);
		addField("details", "Details", 10, 130, 105, 20, 150);
		addCombox("admin_role", "Role", role, 400, 130, 105, 20, 150);
		addField("Email", "Email", 10, 160, 105, 20, 150);
		addField("national_id", "National ID", 10, 10, 105, 20, 150);
		
		bttnRegPanel = new JPanel(new GridLayout(0, 3));

		addButton(bttnRegPanel, "Save",  150, 5, 70, 15, true);
		addButton(bttnRegPanel, "Delete", 300, 5, 70, 15, true);
		addButton(bttnRegPanel, "New",  450, 5, 70, 15, true);

		debPanel = new JPanel(new BorderLayout());
		addPanel(detailPanel, debPanel, "",5, 200, 700, 30);
		debPanel.add(bttnRegPanel, BorderLayout.CENTER);

		// Fingerprint panel
		fpPanel = new JPanel(null);
		addPanel(regPanel, fpPanel, "Finger Prints",5, 260, 400, 300);

		String fpFile1 = "/images/fTemplate1.png";
		String fpFile2 = "/images/fTemplate2.png";

		ImageIcon image1 = new ImageIcon();
		ImageIcon image2 = new ImageIcon();
		image1 = new ImageIcon(fpFile1);
		image2 = new ImageIcon(fpFile2);
		Image fimage1 = image1.getImage();
		Image fimage2 = image2.getImage();
		Image fnewimg1 = fimage1.getScaledInstance(180,200,  Image.SCALE_SMOOTH);
		Image fnewimg2 = fimage2.getScaledInstance(180,200,  Image.SCALE_SMOOTH);
		image1 = new ImageIcon(fnewimg1);
		image2 = new ImageIcon(fnewimg2);
				
		addFinger(image1, 10, 20, 180, 200);
		addFinger(image2, 200, 20, 180, 200);

		// Camera panel
		lblPhoto = new ArrayList<JLabel>();
		dsk = new ArrayList<JDesktopPane>();
		camPanel = new JPanel(null);
		addPanel(regPanel, camPanel, "Photo/ Camera",425, 260, 350, 330);

		ImageIcon pImage = new ImageIcon();
		// if(imageMgr.ifExists("pp_" + jStudent.getString("user_id") + ".png")) {
		// 	pImage = new ImageIcon(imageMgr.getImage("pp_" + jStudent.getString("user_id") + ".png"));
		// 	Image pimage1 = pImage.getImage();
		// 	Image pnewimg1 = pimage1.getScaledInstance(330, 240, Image.SCALE_SMOOTH);
		// 	pImage = new ImageIcon(pnewimg1);
		// }

		addDesktop(10, 30, 330, 240);
		addPhoto(pImage, 10, 30, 330, 240);

		bttnCamPanel = new JPanel(new GridLayout(0, 3));
		addButton(bttnCamPanel, "Camera",  150, 5, 70, 15, true);
		addButton(bttnCamPanel, "Capture", 300, 5, 70, 15, true);
		addButton(bttnCamPanel, "Save PIC",  450, 5, 70, 15, true);

		cambtPanel = new JPanel(new BorderLayout());
		addPanel(camPanel, cambtPanel, "",5, 290, 340, 30);
		cambtPanel.add(bttnCamPanel, BorderLayout.CENTER);

		// regPanel.add(bttnRegPanel, BorderLayout.PAGE_END);

		super.add(tabbedPane, BorderLayout.CENTER);
	}


	class Task_IntegerUpdate extends SwingWorker<Void, Integer> {

		public Task_IntegerUpdate() {
        }

        @Override
        protected void process(List<Integer> chunks) {
            int i = chunks.get(chunks.size()-1);
        }

        @Override
        protected Void doInBackground(){

	    auto.autoDetect2();

            return null;
        }

        @Override
        protected void done() {
            
        }
    }

    class Task_SpeechRecognizerMain extends SwingWorker<Void, Integer> {

		public Task_SpeechRecognizerMain() {
        }

        @Override
        protected void process(List<Integer> chunks) {
            int i = chunks.get(chunks.size()-1);
        }

        @Override
        protected Void doInBackground(){

	    SpeechRecognizerMain spch = new SpeechRecognizerMain();

            return null;
        }

        @Override
        protected void done() {
            
        }
    }

	public void addButton(JPanel nPanel, String btTitle, int x, int y, int w, int h, boolean enabled) {
		JButton btn = new JButton(btTitle);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(this);
		btn.setEnabled(enabled);
		nPanel.add(btn);
		btns.add(btn);
	}

	public void addDesktop(int x, int y, int w, int h) {
		JDesktopPane desktopCam = new JDesktopPane();
		desktopCam.setBounds(x, y, w, h);
		desktopCam.setBackground(Color.black);
		desktopCam.setVisible(false);
		camPanel.add(desktopCam);
		dsk.add(desktopCam);
	}

	public void addPhoto(ImageIcon photo, int x, int y, int w, int h) {
		JLabel photoView = new JLabel();
		photoView.setBounds(x, y, w, h);
		photoView.setIcon(photo);
		photoView.setBorder(new LineBorder(Color.black, 2));
		camPanel.add(photoView);
		lblPhoto.add(photoView);
	}

	public void addFinger(ImageIcon fingerTemplate, int x, int y, int w, int h) {
		JLabel lbFinger = new JLabel();
		lbFinger.setBounds(x, y, w, h);
		lbFinger.setIcon(fingerTemplate);
		lbFinger.setBorder(new LineBorder(Color.black, 3));
		fpPanel.add(lbFinger);
		lbls.add(lbFinger);
	}

	public void addPanel(JPanel n1Panel, JPanel n2Panel, String btTitle, int x, int y, int w, int h) {
		n2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), btTitle));
		n2Panel.setBounds(x, y, w, h);
		n1Panel.add(n2Panel);
	}

	public void addSearch(String fieldTitle, String fieldValue) {
		JLabel lbTitle = new JLabel(fieldTitle + " : ");
		searchPanel.add(lbTitle);
		
		JTextField tfDevice = new JTextField();
		tfDevice.setText(fieldValue);
		searchPanel.add(tfDevice);
		txfs.add(tfDevice);
	}

	public void addField(String fieldKey, String fieldTitle, int x, int y, int w, int h, int dw) {
		JLabel lbTitle = new JLabel(fieldTitle + " : ");
		lbTitle.setBounds(x, y, w, h);
		detailPanel.add(lbTitle);
		
		JTextField tfValue = new JTextField();
		tfValue.setBounds(x + w + 10, y, dw, h);
		detailPanel.add(tfValue);
		detalis.add(tfValue);
		// String lbValue = tfValue
		userFields.put(fieldKey, tfValue.getText());
		
	}

	public void addPassword(String fieldKey, String fieldTitle, int x, int y, int w, int h, int dw) {
		JLabel lbTitle = new JLabel(fieldTitle + " : ");
		lbTitle.setBounds(x, y, w, h);
		detailPanel.add(lbTitle);
		
		JPasswordField pfValue = new JPasswordField();
		pfValue.setBounds(x + w + 10, y, dw, h);
		detailPanel.add(pfValue);
		pwDetalis.add(pfValue);
		// String lbValue = tfValue
		userFields.put(fieldKey, pfValue.getText());
		
	}

	public void addCombox(String fieldKey, String fieldTitle, Vector<String> fieldValue, int x, int y, int w, int h, int dw) {
		JLabel lbTitle = new JLabel(fieldTitle + " : ");
		lbTitle.setBounds(x, y, w, h);
		detailPanel.add(lbTitle);
		
		JComboBox cmbValues = new JComboBox(fieldValue);
		cmbValues.setBounds(x + w + 10, y, dw, h);
		detailPanel.add(cmbValues);
		cmb.add(cmbValues);
		// String lbValue = tfValue
		userFields.put(fieldKey, (String) cmbValues.getSelectedItem());
	}

	public void insertActor(){
		mySql = "INSERT INTO admin"
    			+"(surname, first_name, middle_name, gender, phone, email, user_name, user_password, admin_role, details, national_id)"
				+"VALUES "
				+"(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    	try {
    		PreparedStatement st = db.prepareStatement(mySql, Statement.RETURN_GENERATED_KEYS);
    		{
    			// String myResults = userFields.get("entity_id");
    			// System.out.println(" myResults " + myResults);
    			
    			
    			st.setString(1, detalis.get(0).getText());
    			st.setString(2, detalis.get(1).getText());
    			st.setString(3, detalis.get(2).getText());
    			// st.setDate(5, userFields.get("date_of_birth"));
    			st.setString(4, (String) cmb.get(0).getSelectedItem());
    			st.setString(5, detalis.get(3).getText());
    			st.setString(6, detalis.get(6).getText());
    			st.setString(7, detalis.get(4).getText());
    			st.setString(8, pwDetalis.get(0).getText());
    			st.setString(9, (String) cmb.get(1).getSelectedItem());
    			st.setString(10, detalis.get(5).getText());

    			int results = Integer.parseInt(detalis.get(7).getText());
    			st.setInt(11, results);

    			int affectedRows = st.executeUpdate();
    			//check the affected rows
    			if (affectedRows > 0) {
    				//get the ID back
    				try (ResultSet rs = st.getGeneratedKeys()){
    					if (rs.next()) {
    						id = rs.getLong(1);
    					}
    				}catch (SQLException ex) {
						log.severe("Database connection SQL Error : " + ex.getMessage());
					}
    			}
    			JOptionPane.showMessageDialog(null," User Created Successfully!!! \ncheck your Email to activate your profile","Details",JOptionPane.INFORMATION_MESSAGE);
    		}

		} catch (SQLException ex) {
			log.severe("Database connection SQL Error : " + ex.getMessage());
			JOptionPane.showMessageDialog(null,ex.getMessage(),"Error Data Entered!",JOptionPane.ERROR_MESSAGE );
		}
	}

	public void deleteActor(int nationalID){
		mySql = "DELETE FROM admin WHERE national_id = ?";

		int affectedRows = 0;

    	try {
    		PreparedStatement st = db.prepareStatement(mySql);
    			st.setInt(1, nationalID);

    			affectedRows = st.executeUpdate();
    			System.out.println("the Affected Row = " + affectedRows);

		} catch (SQLException ex) {
			log.severe("Database connection SQL Error : " + ex.getMessage());
		}
	}

	public String takePhoto(String user_id){

		String encodedFile = null;

		Webcam webcam = Webcam.getDefault();

		if (webcam != null) 
		{
            try {
                BufferedImage image = webcam.getImage();
                
                base64Decoder myimage = new base64Decoder();
                ImageIO.write(image, "png", new File(""+myimage.results+"/images/"+user_id+".png"));
                
                File file = new File(""+myimage.results+"/images/"+user_id+".png");
                FileInputStream fileInputStreamReader = new FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fileInputStreamReader.read(bytes);
                encodedFile = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException ex) {
                log.severe("Server Error : " + ex.getMessage());
            }

		} else {
			JOptionPane.showMessageDialog(null,"No webcam detected");
		}

		return encodedFile;
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
			if (folderToScan == null) {
				JOptionPane.showMessageDialog(null,"You have not yet Select a folder to be scanned","Error",JOptionPane.ERROR_MESSAGE );
			}else{
				dup.dupScan(folderToScan);
			}
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
            fileList = chooser.getSelectedFiles();
            int fileSize = fileList.length;
            System.out.println("file number =  " + fileSize);
            int fileCount = 0;
            int fileUniqNonUniq = 1;
            for(File oneFile : fileList){
                if(oneFile.isFile()){
                    // String filePath = oneFile.getAbsolutePath();
                    // hashmapFilesMP4.put(Integer.toString(fileCount++),filePath);
                    Vector<String> row = new Vector<String>();
                    row.add(Integer.toString(fileUniqNonUniq++));
                    row.add(oneFile.getName());
                    long n2 = oneFile.length();
                    row.add(dup.format(n2,0));
                    row.add(getFileExtension(oneFile));
                    rowUnique.add(row);
                }
                
            }
            uniqModel.setDataVector(rowUnique, columnUnique); 
        }else if(ev.getActionCommand().equals("Convert")) {
        	mp3.mp3mp4(fileList);
        }else if(ev.getActionCommand().equals("Save")) {
        	insertActor();
        	System.out.println("the user acor id = " + id);
        }else if(ev.getActionCommand().equals("Delete")) {
        	int nationalID = Integer.parseInt(detalis.get(7).getText());
        	deleteActor(nationalID);
        }else if(ev.getActionCommand().equals("New")) {
        	detalis.get(0).setText("");
        	detalis.get(1).setText("");
        	detalis.get(2).setText("");
        	detalis.get(3).setText("");
        	detalis.get(4).setText("");
        	detalis.get(5).setText("");
        	detalis.get(6).setText("");
        	detalis.get(7).setText("");
        	pwDetalis.get(0).setText("");
        	cmb.get(0).setSelectedItem("None");
        	cmb.get(1).setSelectedItem("None");
        }else if(ev.getActionCommand().equals("Camera")) {
			Dimension[] nonStandardResolutions = new Dimension[] {WebcamResolution.HD.getSize(),};
			Webcam webcam = Webcam.getDefault();

			if (webcam != null){
				lblPhoto.get(0).setVisible(false);
				dsk.get(0).setVisible(true);

				webcam.setCustomViewSizes(nonStandardResolutions);
				webcam.setViewSize(WebcamResolution.HD.getSize());
				webcam.open(true);
				// msg.get(0).setText("Webcam Opened");

				WebcamPanel panel = new WebcamPanel(webcam, false);
				panel.setPreferredSize(WebcamResolution.QVGA.getSize());
				panel.setFPSDisplayed(false);
				panel.setFPSLimited(true);
				panel.setFPSLimit(20);
				panel.start();


				JInternalFrame window = new JInternalFrame();
				((javax.swing.plaf.basic.BasicInternalFrameUI)window.getUI()).setNorthPane(null);
				window.add(panel);
				window.pack();
				window.setMaximumSize(WebcamResolution.QVGA.getSize());
				window.setVisible(true);

				dsk.get(0).add(window);
				btns.get(4).setEnabled(false);
				btns.get(5).setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null,"No webcam detected");
				// msg.get(0).setText("No webcam detected");
			}
		}else if(ev.getActionCommand().equals("Capture")) {
			String photoTaken = takePhoto(detalis.get(7).getText());

            JSONObject jObject = new JSONObject();
            jObject.put("encoded_File", ""+photoTaken+"");

            if (photoTaken!=null) {
				Webcam webcam = Webcam.getDefault();
				webcam.close();

				dsk.get(0).setVisible(false);

				base64Decoder photoImage = new base64Decoder();
				ImageIcon pImage = new ImageIcon(""+photoImage.results+"/images/"+detalis.get(7).getText()+".PNG");
				Image imgP = pImage.getImage();
				Image newPImg = imgP.getScaledInstance(330,240,  Image.SCALE_SMOOTH);
				pImage = new ImageIcon(newPImg);

				lblPhoto.get(0).setVisible(true);
				lblPhoto.get(0).setIcon(pImage);
            }
        }
	}
	
}
