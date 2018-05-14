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
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class AddExisting extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel isbnLabel;
	private JComboBox<String> isbnBox;
	private JLabel branchLabel;
	private JComboBox<String> branchBox;
	private JLabel positionLabel;
	private JComboBox<String> positionBox;
	private JButton backButton;
	private JButton addButton;
	
	private List<String> positions;
	
	
	public AddExisting() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AddExisting(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Add Existing Book");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Add existing Book");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		isbnLabel = new JLabel("ISBN");
		
		isbnBox = new JComboBox<>();
		try {
			String qBook = "SELECT DISTINCT isbn FROM book ORDER BY isbn";
			ResultSet rBook = connection.createStatement().executeQuery(qBook);
			while (rBook.next()) {
				isbnBox.addItem(rBook.getString("isbn"));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
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
        								.addComponent(branchLabel, 200, 200, 200)
        								.addComponent(positionLabel, 200, 200, 200)
        		        				.addComponent(backButton, 200, 200, 200)
        								)
        						.addGroup(layout.createParallelGroup()
        								.addComponent(isbnBox, 200, 200, 200)
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
						.addComponent(isbnBox, 30, 30, 30)
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
	
	private void addButtonListener() {
    	try {
    		String bookID = "";
    		String isbn = isbnBox.getSelectedItem().toString();
    		String branchName = branchBox.getSelectedItem().toString();
    		String branchID = "";
    		String position = positionBox.getSelectedItem().toString();
    		
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
        	
        	String qBranch = "SELECT * FROM branch WHERE name = '" + branchName + "'";
        	ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
        	if (rBranch.next()) {
				branchID = rBranch.getString("branch_id");
			}
        	
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
	
	private void backButtonListener() {
		dispose();  
		AddBook addBook = new AddBook(connection);
		addBook.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AddExisting test = new AddExisting();
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
