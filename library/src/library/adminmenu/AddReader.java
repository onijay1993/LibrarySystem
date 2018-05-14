package library.adminmenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import library.menu.AdminMenu;

@SuppressWarnings("serial")
public class AddReader extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel nameLabel;
	private JTextField nameText;
	private JLabel addressLabel;
	private JTextField addressText;
	private JLabel phoneLabel;
	private JTextField phoneText;
	private JButton backButton;
	private JButton addButton;
	
	
	public AddReader() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AddReader(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Add Reader");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Add Reader");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		nameLabel = new JLabel("Name");
		
		nameText = new JTextField();
		
		addressLabel = new JLabel("Address");
		
		addressText = new JTextField();
		
		phoneLabel = new JLabel("Phone");
		
		phoneText = new JTextField();
		
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
        								.addComponent(nameLabel, 200, 200, 200)
        								.addComponent(addressLabel, 200, 200, 200)
        		        				.addComponent(phoneLabel, 200, 200, 200)
        		        				.addComponent(backButton, 200, 200, 200)
        								)
        						.addGroup(layout.createParallelGroup()
        								.addComponent(nameText, 200, 200, 200)
        								.addComponent(addressText, 200, 200, 200)
        		        				.addComponent(phoneText, 200, 200, 200)
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
						.addComponent(nameLabel)
						.addComponent(nameText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(addressLabel)
						.addComponent(addressText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(phoneLabel)
						.addComponent(phoneText, 30, 30, 30)
						)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						.addComponent(addButton)
						)
        		);
	}
	
	private void addButtonListener() {
		String readerID = "";
		String name = nameText.getText();
		String address = addressText.getText();
		String phone = phoneText.getText();
		try {
			String qReader = "SELECT * FROM reader ORDER BY reader_id DESC";
			ResultSet rReader = connection.createStatement().executeQuery(qReader);
			if (rReader.next()) {
				readerID = Integer.toString(Integer.parseInt(rReader.getString("reader_id")) + 1);
			}
			
        	String sqlReader = "INSERT INTO reader (reader_id, name, address, phone)"
        			+ "VALUES (?, ?, ?, ?)";
        	PreparedStatement psReader = connection.prepareStatement(sqlReader);
        	psReader.setString(1, readerID);
        	psReader.setString(2, name);
        	psReader.setString(3, address);
        	psReader.setString(4, phone);
        	psReader.execute();
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
    	JOptionPane.showMessageDialog(this, "Add successful!", "Sucess",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void backButtonListener() {
		dispose();  
		AdminMenu adminMenu = new AdminMenu(connection);
		adminMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AddReader test = new AddReader();
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
