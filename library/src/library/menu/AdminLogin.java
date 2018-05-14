package library.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class AdminLogin extends JFrame {
	
	private Connection connection;
	
	JLabel welLabel;
	JLabel adminLabel1;
	JLabel adminLabel2;
	JTextField adminText;
	JPasswordField adminPassword;
	JButton loginButton;
	
	public AdminLogin() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AdminLogin(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Admin Login");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		welLabel = new JLabel("Admin Login");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		adminLabel1 = new JLabel("ID");
		
		adminLabel2 = new JLabel("Password");
		
		adminText = new JTextField();
		
		adminPassword = new JPasswordField();
		adminPassword.addActionListener(e -> {
			loginButtonListener();
		});
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(e -> {
			loginButtonListener();
		});
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(100)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addGroup(layout.createSequentialGroup()
        						.addComponent(adminLabel1, 100, 100, 100)
                				.addComponent(adminText, 200, 200, 200)
        						)
        				.addGroup(layout.createSequentialGroup()
        						.addComponent(adminLabel2, 100, 100, 100)
                				.addComponent(adminPassword, 200, 200, 200)
        						)
        				.addComponent(loginButton, 200, 200, 200)
        				)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
        		.addComponent(welLabel)
        		.addGap(20)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                		.addComponent(adminLabel1)
        				.addComponent(adminText, 30, 30, 30)
        				)
				.addGap(20)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                		.addComponent(adminLabel2)
        				.addComponent(adminPassword, 30, 30, 30)
        				)
				.addGap(20)
				.addComponent(loginButton)
        		);
	}
	
	private void loginButtonListener() {
		String username = adminText.getText();
		String password = new String(adminPassword.getPassword());
        String query = "SELECT password FROM librarydb.admin WHERE id = '" + username + "'";
        
        try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
            if (resultSet.next() == false){
                JOptionPane optionPane = new JOptionPane();
                optionPane.setMessage("The user \"" +  username + "\" doesn't exist");
                optionPane.setMessageType(0);
                JDialog dialog = optionPane.createDialog(null, "Error");
                dialog.setTitle("LOGIN ERROR");
                dialog.setVisible(true);
            }
            else if (resultSet.getString("password").equals(password)){
                dispose();
                AdminMenu adminMenuFrame = new AdminMenu(connection);
                adminMenuFrame.setVisible(true);
            }
            else {
            	JOptionPane.showMessageDialog(this, "Wrong password"
            			, "LOGIN ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AdminLogin test = new AdminLogin();
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
