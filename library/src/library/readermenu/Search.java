package library.readermenu;

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

import library.menu.ReaderMenu;

@SuppressWarnings("serial")
public class Search extends JFrame {
	
	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JButton iDButton;
	private JButton titleButton;
	private JButton pubNameButton;
	private JButton backButton;
	
	
	public Search() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public Search(Connection connection, String uid) {
		this.uid = uid;
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Search");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search by");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		iDButton = new JButton("ID");
		iDButton.addActionListener(e -> {
			IDButtonListener();
		});

		titleButton = new JButton("Title");
		titleButton.addActionListener(e -> {
			titleButtonListener();
		});

		pubNameButton = new JButton("Publisher");
		pubNameButton.addActionListener(e -> {
			pubNameButtonListener();
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
	        				.addComponent(iDButton, 200, 200, 200)
	        				.addComponent(titleButton, 200, 200, 200)
	        				.addComponent(pubNameButton, 200, 200, 200)
	        				.addComponent(backButton, 200, 200, 200)
	        				)
	        		);
	        
	        layout.setVerticalGroup(layout.createSequentialGroup()
	        		.addGap(30)
	        		.addComponent(welLabel)
	        		.addComponent(iDButton)
	        		.addComponent(titleButton)
					.addComponent(pubNameButton)
					.addComponent(backButton)
	        		);
	}
	
	private void IDButtonListener() {
		dispose();
		SearchID searchID = new SearchID(connection, uid);
		searchID.setVisible(true);
	}
	
	private void titleButtonListener() {
		dispose();
		SearchTitle searchTitle = new SearchTitle(connection, uid);
		searchTitle.setVisible(true);
	}
	
	private void pubNameButtonListener() {
		dispose();
		SearchPublisher searchPublisher = new SearchPublisher(connection, uid);
		searchPublisher.setVisible(true);
	}
	
	private void backButtonListener() {
		dispose();  
		ReaderMenu readerMenu = new ReaderMenu(connection, uid);
		readerMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		Search test = new Search();
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
