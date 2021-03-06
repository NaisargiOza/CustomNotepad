package Notepadpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

public class GUI implements ActionListener,KeyListener{
  
	JFrame window;
	JTextArea textArea;
	JScrollPane scrollPane;
	boolean wordWrapOn=false;
	JMenuBar menuBar;
	JMenu menuFile,menuEdit,menuFormat,menuColor;
	JMenuItem iNew,iOpen,iSave,iSaveAs,iExit;
	JMenu menuFont,menuFontSize;
	JMenuItem iWrap,iFontArial,iFontCSMS,iFontTNR,iFontSize8,iFontSize12,iFontSize16,iFontSize20,iFontSize24;
	JMenuItem iColor1,iColor2,iColor3;
	JMenuItem iUndo,iRedo;
	JTextField textField;
	JButton button;
	JLabel label;
	Highlighter.HighlightPainter myHighlighterPainter;
	
	Function_File f=new Function_File(this);
	Function_Format format=new Function_Format(this);
	Function_Color color=new Function_Color(this);
	Function_Edit edit=new Function_Edit(this);
	UndoManager um=new UndoManager();
	
	public static void main(String[] args) {
	  new GUI();
  }
	
    public GUI() {
    	try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	createWindow();
        createTextArea();
        createMenuBar();
        createPanel();
        createFileMenu();
        createEditMenu();
        createFormatMenu();
        createColorMenu();
       
        format.selectedFont="Arial";
        format.createFont(16);
        format.wordWrap();
        color.changeColor("White"); 
    }
    
    public void createWindow() {
    	window=new JFrame("Custom notepad");
    	window.setSize(800,600);
    	window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void createTextArea() {
    	textArea=new JTextArea();
        label=new JLabel();
    	
    	textArea.getDocument().addUndoableEditListener(
    			new UndoableEditListener() {
    				public void undoableEditHappened(UndoableEditEvent e) {
    					um.addEdit(e.getEdit());
    				}
    			});
    	textArea.getDocument().addDocumentListener(new DocumentListener() {
    		  public void changedUpdate(DocumentEvent e) {
    		    warn();
    		  }
    		  public void removeUpdate(DocumentEvent e) {
    		    warn();
    		  }
    		  public void insertUpdate(DocumentEvent e) {
    		    warn();
    		  }

    		  public void warn() {  
    			  //String words[]=textArea.getText().split(" ");
    			  removeHighlights();
    		      String k[]=textArea.getText().split("\n");
    		      int count=0;
    		      for(String s:k)
    		      { count+=s.split(" ").length; }
    		      if(wordWrapOn==false) {
    		      label.setText("Lines: "+k.length+" Words: "+count);
    		      //label.setText("Lines: "+k.length+" Words: "+words.length);
    		      }
    		      else
    			  { try {
    		    	AttributedString text = new AttributedString(textArea.getText());
    			    FontRenderContext frc = textArea.getFontMetrics(textArea.getFont())
    			        .getFontRenderContext();
    			    AttributedCharacterIterator charIt = text.getIterator();
    			    LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
    			    float formatWidth = (float) textArea.getSize().width;
    			    lineMeasurer.setPosition(charIt.getBeginIndex());
                    int noLines = 0;
    			    while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
    			      lineMeasurer.nextLayout(formatWidth);
    			      noLines++;
    			    }
    			    noLines+=k.length;
    			    noLines-=1;
    			    label.setText("Lines: "+noLines+" Words: "+count);}catch(Exception e) {
    			    	e.printStackTrace();
    			    }
    			  }
    		  }
    	});
    	scrollPane=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane,BorderLayout.CENTER);
    	window.add(label,BorderLayout.SOUTH);
        textArea.addKeyListener(this);
    	window.setVisible(true);
    	myHighlighterPainter=new MyHighlighterPainter(Color.yellow);
    }
    
    public void highlight(String pattern) {
    	try {
    		Highlighter hi=textArea.getHighlighter();
    		Document doc=textArea.getDocument();
    		String text=doc.getText(0,doc.getLength());
    		int pos=0,c=0;
    		while((pos=text.toUpperCase().indexOf(pattern.toUpperCase(),pos))>=0) {
    			hi.addHighlight(pos,pos+pattern.length(),myHighlighterPainter);
    			pos+=pattern.length();
    			c++;
    		}
    		label.setText("Count: "+c);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void createMenuBar() {
    	menuBar=new JMenuBar();
        window.setJMenuBar(menuBar);
        menuFile=new JMenu("File");
        menuBar.add(menuFile);
        menuEdit=new JMenu("Edit");
        menuBar.add(menuEdit);
        menuFormat=new JMenu("Format");
        menuBar.add(menuFormat);
        menuColor=new JMenu("Color");
        menuBar.add(menuColor);
    }
    
    void createPanel() {
       textField=new JTextField(15);
       button=new JButton("Search");
       //textField.setSize( new Dimension( 50, 10 ) );
       //textField.setColumns(10);
       //textField.setMaximumSize( textField.getPreferredSize() );
       Dimension d = textField.getPreferredSize();
       textField.setMaximumSize(new Dimension(120, d.height -7));
       Dimension db = button.getPreferredSize();
       button.setMaximumSize(new Dimension(18,db.height-10));
       textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
       menuBar.add(Box.createHorizontalGlue());
       menuBar.add(textField);
       menuBar.add(button);
       button.addActionListener(this);
       button.setActionCommand("Search");
    }
    
    void createFileMenu() {
    	iNew=new JMenuItem("New");
    	iNew.addActionListener(this);
    	iNew.setActionCommand("New");
    	menuFile.add(iNew);
    	iOpen=new JMenuItem("Open");
    	iOpen.addActionListener(this);
    	iOpen.setActionCommand("Open");
    	menuFile.add(iOpen);
    	iSave=new JMenuItem("Save");
    	iSave.addActionListener(this);
    	iSave.setActionCommand("Save");
    	menuFile.add(iSave);
    	iSaveAs=new JMenuItem("SaveAs");
    	iSaveAs.addActionListener(this);
    	iSaveAs.setActionCommand("SaveAs");
    	menuFile.add(iSaveAs);
    	iExit=new JMenuItem("Exit");
    	iExit.addActionListener(this);
    	iExit.setActionCommand("Exit");
    	menuFile.add(iExit);
    }

    public void createEditMenu() {
    	iUndo=new JMenuItem("Undo");
    	iUndo.addActionListener(this);
    	iUndo.setActionCommand("Undo");
    	menuEdit.add(iUndo);
    	
    	iRedo=new JMenuItem("Redo");
    	iRedo.addActionListener(this);
    	iRedo.setActionCommand("Redo");
    	menuEdit.add(iRedo);
    }
    
    void createFormatMenu() {
    	iWrap=new JMenuItem("Word Wrap : Off");
    	iWrap.addActionListener(this);
    	iWrap.setActionCommand("Word Wrap");
    	menuFormat.add(iWrap);
    	
    	menuFont=new JMenu("Font");
    	menuFormat.add(menuFont);
    	iFontArial=new JMenuItem("Arial");
    	iFontArial.addActionListener(this);
    	iFontArial.setActionCommand("Arial");
    	menuFont.add(iFontArial);
    	iFontCSMS=new JMenuItem("Comic Sans MS");
    	iFontCSMS.addActionListener(this);
    	iFontCSMS.setActionCommand("Comic Sans MS");
    	menuFont.add(iFontCSMS);
    	iFontTNR=new JMenuItem("Monospaced");
    	iFontTNR.addActionListener(this);
    	iFontTNR.setActionCommand("Times New Roman");
    	menuFont.add(iFontTNR);
    	
    	menuFontSize=new JMenu("Font Size");
    	menuFormat.add(menuFontSize);
    	iFontSize8=new JMenuItem("8");
    	iFontSize8.addActionListener(this);
    	iFontSize8.setActionCommand("size8");
    	menuFontSize.add(iFontSize8);
    	iFontSize12=new JMenuItem("12");
    	iFontSize12.addActionListener(this);
    	iFontSize12.setActionCommand("size12");
    	menuFontSize.add(iFontSize12);
    	iFontSize16=new JMenuItem("16");
    	iFontSize16.addActionListener(this);
    	iFontSize16.setActionCommand("size16");
    	menuFontSize.add(iFontSize16);
    	iFontSize20=new JMenuItem("20");
    	iFontSize20.addActionListener(this);
    	iFontSize20.setActionCommand("size20");
    	menuFontSize.add(iFontSize20);
    	iFontSize24=new JMenuItem("24");
    	iFontSize24.addActionListener(this);
    	iFontSize24.setActionCommand("size24");
    	menuFontSize.add(iFontSize24);
    }
    
    public void createColorMenu() {
    	iColor1=new JMenuItem("White");
    	iColor1.addActionListener(this);
    	iColor1.setActionCommand("White");
    	menuColor.add(iColor1);
    	
    	iColor2=new JMenuItem("Black");
    	iColor2.addActionListener(this);
    	iColor2.setActionCommand("Black");
    	menuColor.add(iColor2);
    	
    	iColor1=new JMenuItem("Blue");
    	iColor1.addActionListener(this);
    	iColor1.setActionCommand("Blue");
    	menuColor.add(iColor1);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		String str=e.getActionCommand();
		switch(str) {
		  case "New": f.New();break;
		  case "Open": f.Open();break;
		  case "Save": f.save();break;
		  case "SaveAs": f.saveAs();break;
		  case "Exit": f.Exit();break;
		  case "Undo": edit.undo();break;
		  case "Redo": edit.redo();break;
		  case "Word Wrap": format.wordWrap();break;
		  case "Arial": format.setFont(str);break;
		  case "Comic Sans MS": format.setFont(str);break;
		  case "Times New Roman": format.setFont(str);break;
		  case "size8": format.createFont(8);break;
		  case "size12": format.createFont(12);break;
		  case "size16": format.createFont(16);break;
		  case "size20": format.createFont(20);break;
		  case "size24": format.createFont(24);break;
		  case "White": color.changeColor(str);break;
		  case "Black": color.changeColor(str);break;
		  case "Blue": color.changeColor(str);break;
		  case "Search": highlight(textField.getText());break;
		}
	}
    
	public void removeHighlights() {
		Highlighter hi=textArea.getHighlighter(); 
		Highlighter.Highlight[] hilights=hi.getHighlights();
		for(int i=0;i<hilights.length;i++) {
			if(hilights[i].getPainter() instanceof MyHighlighterPainter) {
				hi.removeHighlight(hilights[i]);
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent ev) {
		if(((int)ev.getKeyChar())==19) {
			f.save();
			System.out.println("Ctrl+s");
		}
		if(((int)ev.getKeyChar())==15) {
			f.Open();
			System.out.println("Ctrl+o");
		}
		if(((int)ev.getKeyChar())==14) {
			f.New();
			System.out.println("Ctrl+n");
		}
		if(((int)ev.getKeyChar())==25) {
			edit.redo();
			System.out.println("Ctrl+y");
		}
		if(((int)ev.getKeyChar())==26) {
			edit.undo();
			System.out.println("Ctrl+z");
		}
		if(((int)ev.getKeyChar())==3) {
			textArea.copy();
			System.out.println("Ctrl+c");
		}
		/*if(((int)ev.getKeyChar())==22) {
			textArea.paste();
			System.out.println("Ctrl+v");
		}*/
		if(((int)ev.getKeyChar())==24) {
			textArea.cut();
			System.out.println("Ctrl+x");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
