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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

@SuppressWarnings("serial")
public class ReaderLogin extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel readerLabel;
	private JTextField readerText;
	private JButton loginButton;
	
	public ReaderLogin() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public ReaderLogin(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Reader Login");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Reader Login");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		readerLabel = new JLabel("Reader Card Number");
		
		readerText = new JTextField();
		readerText.addActionListener(e -> {
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
        		.addGap(150)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addComponent(readerLabel, 200, 200, 200)
        				.addComponent(readerText, 200, 200, 200)
        				.addComponent(loginButton, 200, 200, 200)
        				)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
        		.addComponent(welLabel)
        		.addGap(20)
        		.addComponent(readerLabel)
        		.addGap(20)
				.addComponent(readerText,30, 30, 30)
				.addGap(20)
				.addComponent(loginButton)
        		);
	}
	
	private void loginButtonListener() {
		String uid = readerText.getText();
        String query = "SELECT * FROM reader WHERE reader_id = '" + uid + "'";
        
        try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
            if(resultSet.next() == false){
            	JOptionPane.showMessageDialog(this, "The user \"" +  uid + "\" doesn't exist"
            			, "LOGIN ERROR", JOptionPane.ERROR_MESSAGE);
            }else{
                dispose();
                ReaderMenu readerMenuFrame = new ReaderMenu(connection, uid);
                readerMenuFrame.setVisible(true);
            }
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		ReaderLogin test = new ReaderLogin();
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
