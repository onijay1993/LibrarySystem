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

import library.readermenu.*;

@SuppressWarnings("serial")
public class ReaderMenu extends JFrame {
	
	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JButton searchButton;
	private JButton checkoutButton;
	private JButton returnButton;
	private JButton reserveButton;
	private JButton fineButton;
	private JButton reserveListButton;
	private JButton publisherButton;
	private JButton quitButton;
	
	public ReaderMenu() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public ReaderMenu(Connection connection, String uid) {
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
        
		setTitle("Reader Functions Menu");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Welcome, Reader " + uid);
		welLabel.setFont(new Font("Arial", 0, 30));
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			searchButtonListener();
		});
		
		checkoutButton =  new JButton("Checkout");
		checkoutButton.addActionListener(e -> {
			checkoutButtonListener();
		});
		
		returnButton = new JButton("Return");
		returnButton.addActionListener(e -> {
			returnButtonListener();
		});
		
		reserveButton = new JButton("Reserve");
		reserveButton.addActionListener(e -> {
			reserveButtonListener();
		});
		
		fineButton = new JButton("Fine");
		fineButton.addActionListener(e -> {
			fineButtonListener();
		});
		
		reserveListButton = new JButton("Reserved List");
		reserveListButton.addActionListener(e -> {
			reserveListButtonListener();
		});
		
		publisherButton = new JButton("Publisher");
		publisherButton.addActionListener(e -> {
			publisherButtonListener();
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
        				.addGroup(layout.createSequentialGroup()
        						.addGroup(layout.createParallelGroup()
                        				.addComponent(searchButton, 200, 200, 200)
                        				.addComponent(checkoutButton, 200, 200, 200)
                        				.addComponent(returnButton, 200, 200, 200)
                        				.addComponent(reserveButton, 200, 200, 200)
        								)
        						.addGroup(layout.createParallelGroup()
                        				.addComponent(fineButton, 200, 200, 200)
                        				.addComponent(reserveListButton, 200, 200, 200)
                        				.addComponent(publisherButton, 200, 200, 200)
                        				.addComponent(quitButton, 200, 200, 200)
        								)
        						)
        				)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(searchButton)
						.addComponent(fineButton)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(checkoutButton)
						.addComponent(reserveListButton)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(returnButton)
						.addComponent(publisherButton)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(reserveButton)
						.addComponent(quitButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		dispose();
		Search jump = new Search(connection, uid);
		jump.setVisible(true);
	}
	
	private void checkoutButtonListener() {
		dispose();
		Checkout jump = new Checkout(connection, uid);
		jump.setVisible(true);
	}
	
	private void returnButtonListener() {
		dispose();
		Return jump = new Return(connection, uid);
		jump.setVisible(true);
	}
	
	private void reserveButtonListener() {
		dispose();
		Reserve jump = new Reserve(connection, uid);
		jump.setVisible(true);
	}
	
	private void fineButtonListener() {
		dispose();
		Fine jump = new Fine(connection, uid);
		jump.setVisible(true);
	}
	
	private void reserveListButtonListener() {
		dispose();
		ReservedList jump = new ReservedList(connection, uid);
		jump.setVisible(true);
	}
	
	private void publisherButtonListener() {
		dispose();
		Publisher jump = new Publisher(connection, uid);
		jump.setVisible(true);
	}
	
	private void quitButtonListener() {
        int ask = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit the program?",
                "Confirm quit", JOptionPane.YES_NO_OPTION);
        if (ask == JOptionPane.YES_OPTION) {
        	System.exit(0);
        }
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		ReaderMenu test = new ReaderMenu();
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
