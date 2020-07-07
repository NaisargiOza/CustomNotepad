package Notepadpackage;

import java.awt.*;

public class Function_Format{
     
	GUI gui;
	Font arial,comicSansMS,timesNewRoman;
	String selectedFont;
	int size;
	
	public Function_Format(GUI gui) {
		this.gui=gui;
		/*arial=new Font("Arial",Font.PLAIN,16);
		comicSansMS=new Font("Comic Sans MS",Font.PLAIN,16);
		timesNewRoman=new Font("Times New Roman",Font.PLAIN,16);*/
	}
	
	void wordWrap() {
		if(gui.wordWrapOn==false) {
			gui.wordWrapOn=true;
			gui.textArea.setLineWrap(true);
			gui.textArea.setWrapStyleWord(true);
			gui.iWrap.setText("Word Wrap : On");
		}
		else if(gui.wordWrapOn==true) {
			gui.wordWrapOn=false;
			gui.textArea.setLineWrap(false);
			gui.textArea.setWrapStyleWord(false);
			gui.iWrap.setText("Word Wrap : Off");
		}
	}
	
	void createFont(int fontsize) {
		/*arial=new Font("Arial",Font.PLAIN,fontsize);
		comicSansMS=new Font("Comic Sans MS",Font.PLAIN,fontsize);
		timesNewRoman=new Font("Times New Roman",Font.PLAIN,fontsize);*/
		this.size=fontsize;
		setFont(selectedFont);
	}
	
	void setFont(String font) {
		selectedFont=font;
		switch(selectedFont) {
		  case "Arial" : 
			  //gui.textArea.setFont(arial);
			  gui.textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, size));
			  System.out.println("In Arial method");
			  break;
		  case "Comic Sans MS" : 
			  //gui.textArea.setFont(comicSansMS);
			  gui.textArea.setFont(new Font("Serif", Font.PLAIN, size));
			  System.out.println("In CSMS method");
			  break;
		  case "Times New Roman" : 
			  gui.textArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN, size));
			  System.out.println("In TNR method");
			  break;
		}
	}
}
