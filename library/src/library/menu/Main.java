package library.menu;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Main {
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
	    JFrame mainMenu = new MainMenu();
	    mainMenu.setVisible(true);
	}
	
	public static void initGobalFont(Font font) {
	    FontUIResource fontResource = new FontUIResource(font);
	    for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof FontUIResource) {
	            //System.out.println(key);  
	            UIManager.put(key, fontResource);  
	        }
	    }
	}
}
