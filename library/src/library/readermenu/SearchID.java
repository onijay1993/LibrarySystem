package library.readermenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class SearchID extends JFrame {

	private Connection connection;
	private String uid;
	
	private JLabel welLabel;
	private JLabel iDLabel;
	private JTextField iDText;
	private JButton searchButton;
	private JLabel resultLabel;
	private JButton backButton;
	
	public SearchID() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public SearchID(Connection connection, String uid) {
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
        
		setTitle("Search by ID");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search by ID");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		iDLabel = new JLabel("ID");
		
		iDText = new JTextField();
		iDText.addActionListener(e -> {
			SearchButtonListener();
		});
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			SearchButtonListener();
		});
		
		resultLabel = new JLabel();
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup()
        		.addGroup(layout.createSequentialGroup()
                		.addGap(100)
                		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                				.addComponent(welLabel)
                				.addGroup(layout.createSequentialGroup()
                						.addGroup(layout.createParallelGroup()
                								.addComponent(iDLabel)
                								)
                						.addGroup(layout.createParallelGroup()
                								.addComponent(iDText, 200, 200, 200)
                								)
                						)
                				.addGroup(layout.createSequentialGroup()
                						.addComponent(searchButton)
                						.addComponent(backButton)
                						)	
                				)
                		)
				.addComponent(resultLabel)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(iDLabel)
						.addComponent(iDText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(searchButton)
						.addComponent(backButton)
						)
				.addComponent(resultLabel)
        		);
	}
	
	private void SearchButtonListener() {
		int bookID = Integer.parseInt(iDText.getText());
		String result = "";
		
		try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            List<String> dates = new ArrayList<>();
            String df = dateFormat.format(date);
            
            String qBorrow = "SELECT * FROM borrow WHERE book_id = '" + bookID + "'";
            String qReserve = "SELECT * FROM reserve WHERE book_id = '" + bookID + "' ORDER BY date DESC";
            ResultSet rBorrow = connection.createStatement().executeQuery(qBorrow);
            ResultSet rReserve = connection.createStatement().executeQuery(qReserve);
            
            while (rReserve.next()) {
            	dates.add(rReserve.getString("date"));
            }
            if (rBorrow.next() == false) {
            	if (!dates.isEmpty() && dates.get(0) == df) {
					result = "Book has been reserved";
				}
            	else {
					result = "<html>Book is available at <br>";
					String qLocation = "SELECT * FROM location WHERE book_id = '" + bookID + "'";
					ResultSet rLocation = connection.createStatement().executeQuery(qLocation);
					while (rLocation.next()) {
						String qBranch = "SELECT * FROM branch WHERE branch_id = '"
										+ rLocation.getString("branch_id") + "'";
						ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
						while (rBranch.next()) {
							result += rBranch.getString("name") + " branch at "
									+ rLocation.getString("locator") + " location</html>";
						}
					}
				}
            }
            else {
				if (rBorrow.getString("rdate") == null) {
					result = "Book has been borrowed";
				}
				else if (!dates.isEmpty() && dates.get(0) == df) {
					result = "Book has been reserved";
				}
				else {
					result = "<html>Book is available at <br>";
					String qLocation = "SELECT * FROM location WHERE book_id = '" + bookID + "'";
					ResultSet rLocation = connection.createStatement().executeQuery(qLocation);
					while (rLocation.next()) {
						String qBranch = "SELECT * FROM branch WHERE branch_id = '"
										+ rLocation.getString("branch_id") + "'";
						ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
						while (rBranch.next()) {
							result += rBranch.getString("name") + " branch at "
									+ rLocation.getString("locator") + " location</html>";
						}
					}
				}
			}
			
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
		resultLabel.setText(result);
	}
	
	private void backButtonListener() { 
		dispose();  
		Search search = new Search(connection, uid);
		search.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		SearchID test = new SearchID();
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
