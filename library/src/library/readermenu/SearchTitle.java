package library.readermenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class SearchTitle extends JFrame {
	
	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JLabel titleLabel;
	private JTextField titleTextField;
	private JButton searchButton;
	private JScrollPane resultPane;
	private JTextArea resultText;
	private JButton backButton;
	private JButton goSearchIDButton;
	
	
	public SearchTitle() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public SearchTitle(Connection connection, String uid) {
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
        
		setTitle("Search by Title");
		setSize(500, 600);
        setLocation((screenWidth - 500) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search by Title");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		titleLabel = new JLabel("Title");
		
		titleTextField = new JTextField();
		titleTextField.addActionListener(e -> {
			searchButtonListener();
		});
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			searchButtonListener();
		});
		
		resultText = new JTextArea(10, 20);
		resultText.setEditable(false);
		resultPane = new JScrollPane(resultText);
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
		goSearchIDButton = new JButton("Go to search ID");
		goSearchIDButton.addActionListener(e -> {
			goSearchIDButtonListener();
		});
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(30)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addGroup(layout.createSequentialGroup()
                        		.addComponent(titleLabel)
                				.addComponent(titleTextField, 200, 200, 200)
                				)
        				.addComponent(searchButton)
        				.addComponent(resultPane)
        				.addGroup(layout.createSequentialGroup()
                				.addComponent(backButton, 200, 200, 200)
                				.addComponent(goSearchIDButton, 200, 200, 200)
        						)

        				)
        		.addGap(30)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(titleLabel)
						.addComponent(titleTextField, 30, 30, 30)
						)
				.addComponent(searchButton)
				.addComponent(resultPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						.addComponent(goSearchIDButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		String title = titleTextField.getText();
		String result = "";
		
		for (int i = 0; i < title.length(); i++) {
			if (title.charAt(i) == '\'') {
				String tmp = title.substring(0, i);
				title = tmp + "\'" + title.substring(i);
				i++;
			}
		}
		
		try {
            String qBookinfo = "SELECT * FROM book_info WHERE title like '%" + title + "%'";
            ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
            
            while (rBookinfo.next()) {
            	String qAuthor = "SELECT * FROM author WHERE author_id = '"
            					+ rBookinfo.getString("author_id") + "'";
            	ResultSet rAuthor = connection.createStatement().executeQuery(qAuthor);
            	title = rBookinfo.getString("title");
            	
            	String author = "";
            	if (rAuthor.next()) {
					author = rAuthor.getString("name");
				}
            	
            	result = "Title:\t" + title + "\nISBN:\t" + rBookinfo.getString("isbn") + "\nAuthor:\t"
            			+ author + "\n";
            	
            	String qBook = "SELECT * FROM book WHERE isbn = '" + rBookinfo.getString("isbn") + "'";
            	ResultSet rBook = connection.createStatement().executeQuery(qBook);
            	while (rBook.next()) {
            		result += "Book ID:\t" + rBook.getString("book_id") + "\n";
            	}
            }
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
		resultText.setText(result);
		resultPane.setViewportView(resultText);
	}
	
	private void backButtonListener() { 
		dispose();  
		Search search = new Search(connection, uid);
		search.setVisible(true);
	}
	
	private void goSearchIDButtonListener() {
		SearchID searchID = new SearchID(connection, uid);
		searchID.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		SearchTitle test = new SearchTitle();
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
