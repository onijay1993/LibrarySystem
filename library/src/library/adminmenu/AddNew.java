package library.adminmenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class AddNew extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel isbnLabel;
	private JTextField isbnText;
	private JLabel titleLabel;
	private JTextField titleText;
	private JLabel authorLabel;
	private JTextField authorText;
	private JLabel pubLabel;
	private JComboBox<String> pubBox;
	private JLabel pubDateLabel;
	private JTextField pubDateText;
	private JLabel branchLabel;
	private JComboBox<String> branchBox;
	private JLabel positionLabel;
	private JComboBox<String> positionBox;
	private JButton backButton;
	private JButton addButton;
	
	private List<String> positions;
	
	public AddNew() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AddNew(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Add New Book");
		setSize(500, 500);
        setLocation((screenWidth - 500) / 2, (screenHeight - 500) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Add New Book");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		isbnLabel = new JLabel("ISBN");
		
		isbnText = new JTextField();
		
		titleLabel = new JLabel("Title");
		
		titleText = new JTextField();
		
		authorLabel = new JLabel("Author");
		
		authorText = new JTextField();
		
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
		
		pubDateLabel = new JLabel("Publication Date");
		
		pubDateText = new JTextField();
		pubDateText.setText("yyyy-MM-dd");
		
		branchLabel = new JLabel("Branch");
		
		branchBox = new JComboBox<>();
		try {
			String qBranch = "SELECT * FROM branch";
			ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
			while (rBranch.next()) {
				branchBox.addItem(rBranch.getString("name"));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		branchBox.addActionListener(e -> {
			branchBoxListener();
		});
		
		positionLabel = new JLabel("Position");
		
		positions = new ArrayList<>();
		for (int i = 1; i < 1000; i++) {
			for (int j = 1; j < 100; j++) {
				positions.add(new DecimalFormat("000").format(i) + 'A' + new DecimalFormat("00").format(j));
			}
		}
		
		positionBox = new JComboBox<>();
		try {
			String qLocation = "SELECT * FROM location WHERE branch_id = '1'";
			ResultSet rLocation = connection.createStatement().executeQuery(qLocation);
			while (rLocation.next()) {
				if (positions.contains(rLocation.getString("locator"))) {
					positions.remove(rLocation.getString("locator"));
				}
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		for (int i = 0; i < positions.size(); i++) {
			positionBox.addItem(positions.get(i));
		}
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
		addButton = new JButton("Add");
		addButton.addActionListener(e -> {
			addButtonListener();
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
        								.addComponent(isbnLabel, 200, 200, 200)
        								.addComponent(titleLabel, 200, 200, 200)
        		        				.addComponent(authorLabel, 200, 200, 200)
        		        				.addComponent(pubLabel, 200, 200, 200)
        								.addComponent(pubDateLabel, 200, 200, 200)
        								.addComponent(branchLabel, 200, 200, 200)
        								.addComponent(positionLabel, 200, 200, 200)
        		        				.addComponent(backButton, 200, 200, 200)
        								)
        						.addGroup(layout.createParallelGroup()
        								.addComponent(isbnText, 200, 200, 200)
        								.addComponent(titleText, 200, 200, 200)
        		        				.addComponent(authorText, 200, 200, 200)
        		        				.addComponent(pubBox, 200, 200, 200)
        		        				.addComponent(pubDateText, 200, 200, 200)
        								.addComponent(branchBox, 200, 200, 200)
        								.addComponent(positionBox, 200, 200, 200)
        		        				.addComponent(addButton, 200, 200, 200)
        								)
        						)
						)
				);
	        
		layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(isbnLabel)
						.addComponent(isbnText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(titleLabel)
						.addComponent(titleText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(authorLabel)
						.addComponent(authorText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(pubLabel)
						.addComponent(pubBox, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(pubDateLabel)
						.addComponent(pubDateText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(branchLabel)
						.addComponent(branchBox, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(positionLabel)
						.addComponent(positionBox, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						.addComponent(addButton)
						)
        		);
	}
	
	private void branchBoxListener() {
		try {
			positions = new ArrayList<>();
			for (int i = 1; i < 1000; i++) {
				for (int j = 1; j < 100; j++) {
					positions.add(new DecimalFormat("000").format(i) + 'A' + new DecimalFormat("00").format(j));
				}
			}
			positionBox.removeAllItems();
			String branchName = branchBox.getSelectedItem().toString();
			for (int i = 0; i < branchName.length(); i++) {
				if (branchName.charAt(i) == '\'') {
					String tmp = branchName.substring(0, i);
					branchName = tmp + "\'" + branchName.substring(i);
					i++;
				}
			}
			
			String qBranch = "SELECT * FROM branch WHERE name = '" + branchName + "'";
			ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
			while (rBranch.next()) {
				String qLocation = "SELECT * FROM location WHERE branch_id = '"
								+ rBranch.getString("branch_id") + "'";
				ResultSet rLocation = connection.createStatement().executeQuery(qLocation);
				while (rLocation.next()) {
					if (positions.contains(rLocation.getString("locator"))) {
						positions.remove(rLocation.getString("locator"));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
		for (int i = 0; i < positions.size(); i++) {
			positionBox.addItem(positions.get(i));
		}
	}
	
	private void backButtonListener() {
		dispose();  
		AddBook addBook = new AddBook(connection);
		addBook.setVisible(true);
	}
	
	private void addButtonListener() {
		String isbn = isbnText.getText();
		String title = titleText.getText();
		String authorID = "";
		String authorName = authorText.getText();
		String pubID = "";
		String pubName = pubBox.getSelectedItem().toString();
		String pubDate = pubDateText.getText();
		String branchID = "";
		String branchName = branchBox.getSelectedItem().toString();
		String position = positionBox.getSelectedItem().toString();
		String bookID = "";
		try {
			
			for (int i = 0; i < title.length(); i++) {
				if (title.charAt(i) == '\'') {
					String tmp = title.substring(0, i);
					title = tmp + "\'" + title.substring(i);
					i++;
				}
			}
			
			String qAuthor = "SELECT * FROM author WHERE name = '" + authorName + "'";
			ResultSet rAuthor = connection.createStatement().executeQuery(qAuthor);
			if (rAuthor.next()) {
				authorID = rAuthor.getString("author_id");
			}
			else {
				qAuthor = "SELECT * FROM author ORDER BY author_id DESC";
				rAuthor = connection.createStatement().executeQuery(qAuthor);
				if (rAuthor.next()) {
					authorID = Integer.toString(Integer.parseInt(rAuthor.getString("author_id")) + 1);
				}
				String sqlAuthor = "INSERT INTO author (author_id, name)"
								+"VALUES (?, ?)";
	        	PreparedStatement psAuthor = connection.prepareStatement(sqlAuthor);
	        	psAuthor.setString(1, authorID);
	        	psAuthor.setString(2, authorName);
	        	psAuthor.execute();
			}
			
			String qPub = "SELECT * FROM publisher WHERE publisher_name = '" + pubName + "'";
			ResultSet rPub = connection.createStatement().executeQuery(qPub);
			if (rPub.next()) {
				pubID = rPub.getString("publisher_id");
			}
			
			String qBranch = "SELECT * FROM branch WHERE name = '" + branchName + "'";
			ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
			if (rBranch.next()) {
				branchID = rBranch.getString("branch_id");
			}
			
    		String qBook = "SELECT * FROM book ORDER BY book_id DESC";
    		ResultSet rBook = connection.createStatement().executeQuery(qBook);
    		if (rBook.next()) {
        		bookID = Integer.toString(Integer.parseInt(rBook.getString("book_id")) + 1);
			}
    		
    		
        	String sqlBook = "INSERT INTO book (isbn, book_id)"
					+ "VALUES (?, ?)";
        	PreparedStatement psBook = connection.prepareStatement(sqlBook);
        	psBook.setString(1, isbn);
        	psBook.setString(2, bookID);
        	psBook.execute();
        	
        	String sqlBookinfo = "INSERT INTO book_info (isbn, title, publisher_id, publisher_date, author_id)"
        						+ "VALUES (?, ?, ?, ?, ?)";
        	PreparedStatement psBookinfo = connection.prepareStatement(sqlBookinfo);
        	psBookinfo.setString(1, isbn);
        	psBookinfo.setString(2, title);
        	psBookinfo.setString(3, pubID);
        	psBookinfo.setString(4, pubDate);
        	psBookinfo.setString(5, authorID);
        	psBookinfo.execute();
        	
        	String qCopy = "SELECT * FROM copy WHERE branch_id = '" + branchID + "' AND isbn = '" + isbn + "'";
        	ResultSet rCopy = connection.createStatement().executeQuery(qCopy);
        	
        	if (rCopy.next()){
				String sqlCopy = "UPDATE copy SET num_copy = num_copy + 1 WHERE isbn = '" + isbn
								+ "' AND branch_id = '" + branchID + "'";
				connection.createStatement().execute(sqlCopy);
			}
        	
        	else {
            	String sqlCopy = "INSERT INTO copy (branch_id, isbn, num_copy)"
            			+ "VALUES (?, ?, ?)";
            	PreparedStatement psCopy = connection.prepareStatement(sqlCopy);
            	psCopy.setString(1, branchID);
            	psCopy.setString(2, isbn);
            	psCopy.setString(3, "1");
            	psCopy.execute();
			}
        	
        	String sqlLocation = "INSERT INTO location (book_id, branch_id, locator)"
        			+ "VALUES (?, ?, ?)";
        	PreparedStatement psLocation = connection.prepareStatement(sqlLocation);
        	psLocation.setString(1, bookID);
        	psLocation.setString(2, branchID);
        	psLocation.setString(3, position);
        	psLocation.execute();
			
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
    	JOptionPane.showMessageDialog(this, "Add successful!", "Sucess",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AddNew test = new AddNew();
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
