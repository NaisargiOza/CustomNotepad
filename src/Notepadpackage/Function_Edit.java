package Notepadpackage;

public class Function_Edit {

	GUI gui;
	
	public Function_Edit(GUI gui) {
		this.gui=gui;
	}
	
	public void undo() {
		if(gui.um.canUndo() ) {
		gui.um.undo();
		}
	}
	
	public void redo() {
		if(gui.um.canRedo()) {
		gui.um.redo();
		}
	}
}
