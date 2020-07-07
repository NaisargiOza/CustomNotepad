package Notepadpackage;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JOptionPane;

public class Function_File {
   GUI gui;
   String fileName;
   String fileAddress;
   
   public Function_File(GUI gui) {
	   this.gui=gui;
   }
   
   public void New() {
	   gui.textArea.setText("");
	   gui.window.setTitle("New");
	   fileName=null;
	   fileAddress=null;
   }
   
   public void Open() {
	   FileDialog fd=new FileDialog(gui.window,"Open",FileDialog.LOAD);
	   fd.setVisible(true);
	   if(fd.getFile()!=null) {
		   fileName=fd.getFile();
		   fileAddress=fd.getDirectory();
		   gui.window.setTitle(fileName);
	   }
	   try {
		   BufferedReader br=new BufferedReader(new FileReader(fileAddress + fileName));
		   String line="";
		   while((line=br.readLine())!=null) {
			   gui.textArea.append(line + "\n");
		   }
		   br.close();
	   }catch(Exception e) {
		   JOptionPane.showMessageDialog(null,"No file is selected!","Error",JOptionPane.ERROR_MESSAGE);
	   }
   }
   
   public void save() {
	   if(fileName==null) {
		   saveAs();
	   }
	   else {
		   try {
		   FileWriter fw=new FileWriter(fileAddress + fileName);
		   fw.write(gui.textArea.getText());
		   gui.window.setTitle(fileName);
		   fw.close();
		   }catch(Exception e) {
			   JOptionPane.showMessageDialog(null,"No file is selected!","Error",JOptionPane.ERROR_MESSAGE);  
		   }
	   }
   }
   
   public void saveAs() {
	   FileDialog fd=new FileDialog(gui.window,"Save",FileDialog.SAVE);
	   fd.setVisible(true);
	   if(fd.getFile()!=null) {
		   fileName=fd.getFile();
		   fileAddress=fd.getDirectory();
		   gui.window.setTitle(fileName);
	   }
	   try {
		   FileWriter fw=new FileWriter(fileAddress + fileName);
		   fw.write(gui.textArea.getText());
		   fw.close();
	   }catch(Exception e) {
		   JOptionPane.showMessageDialog(null,"Could not save","Error",JOptionPane.ERROR_MESSAGE);  
	   }
   }
   
   public void Exit() {
	   try {
		   int input=JOptionPane.showConfirmDialog(null,"Do you want to save before closing?","Wait!",JOptionPane.YES_NO_OPTION);
		   if(input==JOptionPane.YES_OPTION) {
			   save();
		   }
		   fileName=null;
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   System.exit(0);
   }
}
