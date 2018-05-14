package library.menu;

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
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JButton readerMenuButton;
	private JButton adminMenuButton;
	private JButton quitButton;
	
	public MainMenu() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Main Menu");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Welcome to the library system!");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		readerMenuButton = new JButton("Reader");
		readerMenuButton.addActionListener(e -> {
			readerMenuButtonListener();
        });
        
        adminMenuButton = new JButton("Admin");
        adminMenuButton.addActionListener(e -> {
        	adminMenuButtonListener();
        });
        
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
        	quitButtonListener();
        });
        
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(40)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addComponent(readerMenuButton, 200, 200, 200)
        				.addComponent(adminMenuButton, 200, 200, 200)
        				.addComponent(quitButton, 200, 200, 200)
        				)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
        		.addComponent(welLabel)
        		.addGap(20)
        		.addComponent(readerMenuButton)
        		.addGap(20)
				.addComponent(adminMenuButton)
				.addGap(20)
				.addComponent(quitButton)
        		);
	}
	
	private void readerMenuButtonListener() {
		dispose();
		ReaderLogin readerLogin = new ReaderLogin(connection);
		readerLogin.setVisible(true);
	}
	
	private void adminMenuButtonListener() {
		dispose();
		AdminLogin adminLogin = new AdminLogin(connection);
		adminLogin.setVisible(true);
	}
	
	private void quitButtonListener() {
        int ask = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit the program?",
                "Confirm quit", JOptionPane.YES_NO_OPTION);
        if (ask == JOptionPane.YES_OPTION) {
        	System.exit(0);
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
    
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		MainMenu test = new MainMenu();
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
}
