package library.adminmenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import library.menu.AdminMenu;

@SuppressWarnings("serial")
public class AverageFine extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel readerLabel;
	private JComboBox<String> readerBox;
	private JButton searchButton;
	private JLabel fineLabel;
	private JTextField fineText;
	private JButton backButton;
	
	
	public AverageFine() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public AverageFine(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Average FIne");
		setSize(500, 300);
        setLocation((screenWidth - 500) / 2, (screenHeight - 300) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Average FIne");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		readerLabel = new JLabel("Reader ID");
		
		readerBox = new JComboBox<>();
		try {
			String qReader = "SELECT * FROM reader";
			ResultSet rReader = connection.createStatement().executeQuery(qReader);
			while (rReader.next()) {
				readerBox.addItem(rReader.getString("reader_id"));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		readerBox.addActionListener(e -> {
			searchButtonListener();
		});
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> {
			searchButtonListener();
		});
		
		fineLabel = new JLabel("Average Fine");
		
		fineText = new JTextField();
		fineText.setEditable(false);
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			backButtonListener();
		});
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(75)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addGroup(layout.createSequentialGroup()
                        		.addComponent(readerLabel)
                				.addComponent(readerBox, 200, 200, 200)
                				)
        				.addComponent(searchButton)
        				.addGroup(layout.createSequentialGroup()
                				.addComponent(fineLabel)
                				.addComponent(fineText, 200, 200, 200)
        						)
        				.addComponent(backButton, 200, 200, 200)
        				)
        		.addGap(75)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(30)
				.addComponent(welLabel)
				.addGap(20)
				.addGroup(layout.createParallelGroup()
						.addComponent(readerLabel)
						.addComponent(readerBox, 30, 30, 30)
						)
				.addComponent(searchButton)
				.addGroup(layout.createParallelGroup()
						.addComponent(fineLabel)
						.addComponent(fineText, 30, 30, 30)
						)
				.addComponent(backButton)
        		);
	}
	
	private void searchButtonListener() {
		String readerID = readerBox.getSelectedItem().toString();
		double sum = 0;
		int count = 0;
		double average;
		try {
			String qBorrow = "SELECT * FROM borrow WHERE reader_id = '" + readerID + "'";
			ResultSet rBorrow = connection.createStatement().executeQuery(qBorrow);
			while (rBorrow.next()) {
				count++;
				sum += Double.parseDouble(rBorrow.getString("fee"));
			}
			if (count == 0)
				average = 0;
			else
				average = sum / count;
			fineText.setText(new DecimalFormat("##0.00").format(average));
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
	}
	
	private void backButtonListener() {
		dispose();  
		AdminMenu adminMenu = new AdminMenu(connection);
		adminMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		AverageFine test = new AverageFine();
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
