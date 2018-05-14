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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class SearchPublisher extends JFrame {
	
	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JLabel pubLabel;
	private JComboBox<String> pubBox;
	private JButton searchButton;
	private JScrollPane resultPane;
	private JTextArea resultText;
	private JButton backButton;
	private JButton goSearchTitleButton;
	
	
	public SearchPublisher() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public SearchPublisher(Connection connection, String uid) {
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
        
		setTitle("Search by Publisher");
		setSize(500, 600);
        setLocation((screenWidth - 500) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search by Publisher");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		pubLabel = new JLabel("Publisher");
		
		pubBox = new JComboBox<>();
		try {
			String qPublisher = "SELECT * FROM publisher";
			ResultSet rPublisher = connection.createStatement().executeQuery(qPublisher);
			while (rPublisher.next()) {
				pubBox.addItem(rPublisher.getString("publisher_name"));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		pubBox.addActionListener(e -> {
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
		
		goSearchTitleButton = new JButton("Go to search Title");
		goSearchTitleButton.addActionListener(e -> {
			goSearchTitleButtonListener();
		});
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(30)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addGroup(layout.createSequentialGroup()
                        		.addComponent(pubLabel)
                				.addComponent(pubBox, 200, 200, 200)
                				)
        				.addComponent(searchButton)
        				.addComponent(resultPane)
        				.addGroup(layout.createSequentialGroup()
                				.addComponent(backButton, 200, 200, 200)
                				.addComponent(goSearchTitleButton, 200, 200, 200)
        						)

        				)
        		.addGap(30)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(pubLabel)
						.addComponent(pubBox, 30, 30, 30)
						)
				.addComponent(searchButton)
				.addComponent(resultPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						.addComponent(goSearchTitleButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		String publisher = pubBox.getSelectedItem().toString();
		String result = "";
		
		try {
            String qPublisher = "SELECT * FROM publisher WHERE publisher_name = '" + publisher + "'";
            ResultSet rPublisher = connection.createStatement().executeQuery(qPublisher);
            
            String publisherid = "";
            while (rPublisher.next()) {
            	publisherid = rPublisher.getString("publisher_id");
            }
            
            String qBookinfo = "SELECT * FROM book_info WHERE publisher_id = '" + publisherid + "'";
            ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
            
            while (rBookinfo.next()) {
            	result += rBookinfo.getString("title") + "\n";
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
	
	private void goSearchTitleButtonListener() {
		SearchTitle searchTitle = new SearchTitle(connection, uid);
		searchTitle.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		SearchPublisher test = new SearchPublisher();
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
