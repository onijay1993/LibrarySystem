package library.adminmenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import library.menu.AdminMenu;

@SuppressWarnings("serial")
public class AddBook extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JButton existingButton;
	private JButton newButton;
	private JButton backButton;
	
	
	public AddBook() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AddBook(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Add Book");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Add Book");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		existingButton = new JButton("Existing Book");
		existingButton.addActionListener(e -> {
			existingButtonListener();
		});
		
		newButton = new JButton("New Book");
		newButton.addActionListener(e -> {
			newButtonListener();
		});
		
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
	        
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGap(150)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(welLabel)
						.addComponent(existingButton, 200, 200, 200)
						.addComponent(newButton, 200, 200, 200)
						.addComponent(backButton, 200, 200, 200)
						)
				);
	        
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGap(30)
				.addComponent(welLabel)
				.addComponent(existingButton)
				.addComponent(newButton)
				.addComponent(backButton)
				);
	}
	
	private void existingButtonListener() {
		dispose();  
		AddExisting addExisting = new AddExisting(connection);
		addExisting.setVisible(true);
	}
	
	private void newButtonListener() {
		dispose();  
		AddNew addNew = new AddNew(connection);
		addNew.setVisible(true);
	}
	
	private void backButtonListener() {
		dispose();  
		AdminMenu adminMenu = new AdminMenu(connection);
		adminMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AddBook test = new AddBook();
		test.setVisible(true);
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
	
    private void makeConnection() {
        try{
           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryme"
           		+ "?autoReconnect=true&useSSL=false", "library", "1234");
           
       }catch(Exception e){
           System.err.println("ERROR: "+e);
       }
   }
}
