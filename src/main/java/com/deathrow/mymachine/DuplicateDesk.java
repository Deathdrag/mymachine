package com.deathrow.mymachine;

import java.sql.Connection;
import java.io.InputStream;
import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.util.HashMap;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JDesktopPane;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import org.json.JSONObject;
import org.json.JSONArray;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class DuplicateDesk implements ActionListener {
	Logger log = Logger.getLogger(DuplicateDesk.class.getName());

	
	JFrame eFrame;
	JDialog eDialog;
	
	List<JButton> btns;

	DuplicateFileRemoverTwo dup = new DuplicateFileRemoverTwo();
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel nonRegPanel, buttonPanel, dupPanel;
	JTable tableUniq;
	DefaultTableModel uniqModel;

	Vector<String> columnUnique;
	Vector<Vector<String>>  rowUnique;
	Vector<Vector<String>>  rowFileUnique;
	HashMap<String, String> dupHashMap;

	public DuplicateDesk(Vector<Vector<String>>  rowUnique, String button, HashMap<String, String> dupHashMap, Vector<Vector<String>>  rowFileUnique) {
		
		btns = new ArrayList<JButton>();
		rowUnique = rowUnique;
		this.dupHashMap = dupHashMap;
		this.rowFileUnique = rowFileUnique;
		dupPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Duplicate Files", dupPanel);

		buttonPanel = new JPanel(new GridLayout(0, 4));
		addButton("Close", 350, 20, 150, 25, true);
		buttonPanel.add(btns.get(0));
		if (button=="Delete") {
			addButton("Delete Duplicate", 150, 20, 150, 25, true);
			buttonPanel.add(btns.get(1));
		}
		

		dupPanel.add(buttonPanel, BorderLayout.PAGE_END);

		columnUnique = new Vector<String>();
		columnUnique.add("Number"); columnUnique.add("Files"); columnUnique.add("Size"); columnUnique.add("Status");

		uniqModel = new DefaultTableModel(rowUnique,columnUnique);
		// uniqModel.setDataVector(rowUnique, columnUnique);
		tableUniq = new JTable(uniqModel);
		JScrollPane scrollPaneDupl = new JScrollPane(tableUniq);
		dupPanel.add(scrollPaneDupl, BorderLayout.CENTER);

		
		// Load on main form
		eFrame = new JFrame("Duplicate");
		eDialog = new JDialog(eFrame , "Duplicate Files", true);
		eDialog.setSize(700, 500);
		eDialog.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		eDialog.setVisible(true);
	}
	
	
	public void addButton(String btTitle, int x, int y, int w, int h, boolean enabled) {
		JButton btn = new JButton(btTitle);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(this);
		btn.setEnabled(enabled);
		// buttonPanel.add(btn);
		btns.add(btn);
	}

	public  void delDuplicates(boolean state){
		if(state == true){
			try{
				ArrayList<String> delList = new ArrayList<>();
				int numOfDelFiles = 0;
				for(String dupHashName : dupHashMap.keySet())
				{
					Path pathOfFile = Paths.get(dupHashMap.get(dupHashName));
					String dupFileName = dupHashMap.get(dupHashName);
					Files.delete(pathOfFile);
					delList.add(dupHashName);
					numOfDelFiles++;
				}

				for(int i=0; i<delList.size();i++){
					dupHashMap.remove(delList.get(i));
				}


				JOptionPane.showMessageDialog(null,numOfDelFiles + " of Duplicate Files Are Deleted","Files Details",JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception x){
				System.out.println("Error Message : "  + x.getMessage());
				String errorMessage = "Error Message : "  + x.getMessage();
				JOptionPane.showMessageDialog(null, errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE );
			}
		}
	}


	public void actionPerformed(ActionEvent ev) {

		if(ev.getActionCommand().equals("Delete Duplicate")) {

        	int a=JOptionPane.showConfirmDialog(null, "Are you sure? You want to Delete", "Delete", JOptionPane.YES_NO_OPTION);
			if(a==JOptionPane.YES_OPTION){
				delDuplicates(true);
				
				uniqModel.setDataVector(rowFileUnique, columnUnique);
				tableUniq.repaint();
				tableUniq.revalidate();
			}  
        }else if(ev.getActionCommand().equals("Close")) {
        	eDialog.dispose();
        }
	}

}
