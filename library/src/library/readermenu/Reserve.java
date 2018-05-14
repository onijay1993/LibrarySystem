package library.readermenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import library.menu.ReaderMenu;

@SuppressWarnings("serial")
public class Reserve extends JFrame{
	
	private Connection connection;
	private String uid;
	
	JLabel welLabel;
	JLabel iDLabel;
	JTextField iDText;
	JButton backButton;
	JButton ReverseButton;
	
	public Reserve() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public Reserve(Connection connection, String uid) {
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
        
		setTitle("Reserve");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		welLabel = new JLabel("Reserve");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		iDLabel = new JLabel("Book ID");
		
		iDText = new JTextField();
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
		ReverseButton = new JButton("Reserve");
		ReverseButton.addActionListener(e -> {
			ReverseButtonListener();
		});
		
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(40)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addGroup(layout.createSequentialGroup()
        						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        				.addComponent(iDLabel)
                        				.addComponent(backButton, 200, 200, 200)
        								)
        						.addGroup(layout.createParallelGroup()
                        				.addComponent(iDText, 200, 200, 200)
                        				.addComponent(ReverseButton, 200, 200, 200)
        								)
        						)
        				)
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
						.addComponent(backButton)
						.addComponent(ReverseButton)
						)
        		);
	}
	
	private void backButtonListener() {
		dispose();  
		ReaderMenu readerMenu = new ReaderMenu(connection, uid);
		readerMenu.setVisible(true);
	}
	
	private void ReverseButtonListener() {
        int ask = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to Reserve?",
                "Confirm return", JOptionPane.YES_NO_OPTION);
        if (ask == JOptionPane.YES_OPTION) {
        	String bookID = iDText.getText();
        	
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String df = dateFormat.format(date);
            
        	try {
        		String sql = "INSERT INTO reserve (reader_id, book_id, date)"
        					+ "VALUES (?, ?, ?)";
	        	PreparedStatement ps = connection.prepareStatement(sql);
	        	ps.setString(1, uid);
	        	ps.setString(2, bookID);
	        	ps.setString(3, df);
	        	ps.execute();
        		
			} catch (Exception e) {
				System.err.println("ERROR: "+e);
			}
        	
        	JOptionPane.showMessageDialog(this, "Reserve successful!", "Thank You",
					JOptionPane.INFORMATION_MESSAGE);
        }
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		Reserve test = new Reserve();
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
