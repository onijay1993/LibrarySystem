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

import library.adminmenu.*;

@SuppressWarnings("serial")
public class AdminMenu extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JButton addBookButton;
	private JButton searchBookButton;
	private JButton addReaderButton;
	private JButton branchButton;
	private JButton top10BorrowersButton;
	private JButton top10BooksButton;
	private JButton averageFineButton;
	private JButton quitButton;
	
	public AdminMenu() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AdminMenu(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Administrative Functions Menu");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Administrative Functions Menu");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		addBookButton = new JButton("Add book");
		addBookButton.addActionListener(e -> {
			addBookButtonListener();
		});
		
		searchBookButton = new JButton("Search book");
		searchBookButton.addActionListener(e -> {
			searchBookButtonListener();
		});
		
		addReaderButton = new JButton("Add reader");
		addReaderButton.addActionListener(e -> {
			addReaderButtonListener();
		});
		
		branchButton = new JButton("Branch");
		branchButton.addActionListener(e -> {
			branchButtonListener();
		});
		
		top10BorrowersButton = new JButton("Top 10 borrowers");
		top10BorrowersButton.addActionListener(e -> {
			top10BorrowersButtonListener();
		});
		
		top10BooksButton = new JButton("Top 10 books");
		top10BooksButton.addActionListener(e -> {
			top10BooksButtonListener();
		});
		
		averageFineButton = new JButton("Average fine");
		averageFineButton.addActionListener(e -> {
			averageFineButtonListener();
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
	                        				.addComponent(addBookButton, 200, 200, 200)
	                        				.addComponent(searchBookButton, 200, 200, 200)
	                        				.addComponent(addReaderButton, 200, 200, 200)
	                        				.addComponent(branchButton, 200, 200, 200)
	        								)
	        						.addGroup(layout.createParallelGroup()
	                        				.addComponent(top10BorrowersButton, 200, 200, 200)
	                        				.addComponent(top10BooksButton, 200, 200, 200)
	                        				.addComponent(averageFineButton, 200, 200, 200)
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
							.addComponent(addBookButton)
							.addComponent(top10BorrowersButton)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(searchBookButton)
							.addComponent(top10BooksButton)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(addReaderButton)
							.addComponent(averageFineButton)
							)
					.addGroup(layout.createParallelGroup()
							.addComponent(branchButton)
							.addComponent(quitButton)
							)
	        		);
	}
	
	private void addBookButtonListener() {
		dispose();
		AddBook jump = new AddBook(connection);
		jump.setVisible(true);
	}
	
	private void searchBookButtonListener() {
		dispose();
		SearchBook jump = new SearchBook(connection);
		jump.setVisible(true);
	}
	
	private void addReaderButtonListener() {
		dispose();
		AddReader jump = new AddReader(connection);
		jump.setVisible(true);
	}
	
	private void branchButtonListener() {
		dispose();
		Branch jump = new Branch(connection);
		jump.setVisible(true);
	}
	
	private void top10BorrowersButtonListener() {
		dispose();
		TopBorrowers jump = new TopBorrowers(connection);
		jump.setVisible(true);
	}
	
	private void top10BooksButtonListener() {
		dispose();
		TopBooks jump = new TopBooks(connection);
		jump.setVisible(true);
	}
	
	private void averageFineButtonListener() {
		dispose();
		AverageFine jump = new AverageFine(connection);
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
		AdminMenu test = new AdminMenu();
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
