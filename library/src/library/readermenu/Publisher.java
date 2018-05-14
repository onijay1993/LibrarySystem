package library.readermenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import library.menu.ReaderMenu;

@SuppressWarnings("serial")
public class Publisher extends JFrame {
	
	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JLabel pubLabel;
	private JComboBox<String> pubBox;
	private JButton searchButton;
	private DefaultTableModel pubModel;
	private JTable pubTable;
	private JScrollPane pubPane;
	private JButton backButton;

	
	public Publisher() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public Publisher(Connection connection, String uid) {
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
        
		setTitle("Search Publisher");
		setSize(700, 600);
        setLocation((screenWidth - 700) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search Publisher");
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
		
		String[][] datas = {};
		String[] titles = {"Book ID", "Book Title"};
		pubModel = new DefaultTableModel(datas, titles);
		pubTable = new JTable(pubModel);
		pubTable.setEnabled(false);
		pubPane = new JScrollPane(pubTable);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		pubTable.setDefaultRenderer(Object.class, r);
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			searchButtonListener();
		});
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
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
        				.addComponent(pubPane)
        				.addGroup(layout.createSequentialGroup()
                				.addComponent(backButton, 200, 200, 200)
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
				.addComponent(pubPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		try {
			List<String> bookIDs = new ArrayList<>();
			List<String> bookTitles = new ArrayList<>();
			
			String publisher = pubBox.getSelectedItem().toString();
            String qPublisher = "SELECT * FROM publisher WHERE publisher_name = '" + publisher + "'";
            ResultSet rPublisher = connection.createStatement().executeQuery(qPublisher);
            
            while (rPublisher.next()) {
                String qBookinfo = "SELECT * FROM book_info WHERE publisher_id = '"
                					+ rPublisher.getString("publisher_id") + "'";
                ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
                
                while (rBookinfo.next()) {
                	
                	String qBook = "SELECT * FROM book WHERE isbn = '" + rBookinfo.getString("isbn") + "'";
                	ResultSet rBook = connection.createStatement().executeQuery(qBook);
                	
                	while (rBook.next()) {
                		bookIDs.add(rBook.getString("book_id"));
                	}
                }
            }
            
            for (int i = 0; i < bookIDs.size(); i++) {
            	String qBook = "SELECT * FROM book WHERE book_id = '" + bookIDs.get(i) + "'";
            	ResultSet rBook = connection.createStatement().executeQuery(qBook);
            	
            	while (rBook.next()) {
            		String qBookinfo = "SELECT * FROM book_info WHERE isbn = '" + rBook.getString("isbn") + "'";
            		ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
            		
            		while (rBookinfo.next()) {
            			bookTitles.add(rBookinfo.getString("title"));
            		}
            	}
            }
            
            pubModel.setRowCount(0);
    		for (int i = 0; i < bookIDs.size(); i++) {
    			pubModel.addRow(new String[] {bookIDs.get(i), bookTitles.get(i)});
    		}
    		
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
	}
	
	private void backButtonListener() { 
		dispose();  
		ReaderMenu readerMenu = new ReaderMenu(connection, uid);
		readerMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		Publisher test = new Publisher();
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
