package poinsot.framework;

import java.io.FileReader;

public class Start {
	private MenuItems menuItems;
	public Start(FileReader fileReader){
		this.menuItems = new MenuItems(fileReader);
	}

	public void init(){
		menuItems.iniciar();
	}
}
