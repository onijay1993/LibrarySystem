package library.adminmenu;

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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import library.menu.AdminMenu;

@SuppressWarnings("serial")
public class SearchBook extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel titleLabel;
	private JComboBox<String> titleBox;
	private JButton searchButton;
	private DefaultTableModel bookModel;
	private JTable bookTable;
	private JScrollPane bookPane;
	private JButton backButton;
	
	
	public SearchBook() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public SearchBook(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Search Book");
		setSize(700, 600);
        setLocation((screenWidth - 700) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Search Book");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		titleLabel = new JLabel("Title");
		
		titleBox = new JComboBox<>();
		try {
			String qBookinfo = "SELECT * FROM book_info";
			ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
			while (rBookinfo.next()) {
				titleBox.addItem(rBookinfo.getString("title"));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		titleBox.addActionListener(e -> {
			searchButtonListener();
		});
		
		String[][] datas = {};
		String[] titles = {"Book ID", "Status"};
		bookModel = new DefaultTableModel(datas, titles);
		bookTable = new JTable(bookModel);
		bookTable.setEnabled(false);
		bookPane = new JScrollPane(bookTable);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		bookTable.setDefaultRenderer(Object.class, r);
		
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
                        		.addComponent(titleLabel)
                				.addComponent(titleBox, 400, 400, 400)
                				)
        				.addComponent(searchButton)
        				.addComponent(bookPane)
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
						.addComponent(titleLabel)
						.addComponent(titleBox, 30, 30, 30)
						)
				.addComponent(searchButton)
				.addComponent(bookPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		try {
			List<String> bookIDs = new ArrayList<>();
			List<String> bookStatus = new ArrayList<>();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currDate = dateFormat.format(new Date());
			String resDate = "";
			String returnDate = "";
			
			String title = titleBox.getSelectedItem().toString();
			for (int i = 0; i < title.length(); i++) {
				if (title.charAt(i) == '\'') {
					String tmp = title.substring(0, i);
					title = tmp + "\'" + title.substring(i);
					i++;
				}
			}
            String qBookinfo = "SELECT * FROM book_info WHERE title = '" + title + "'";
            ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
            
            while (rBookinfo.next()) {
               	String qBook = "SELECT * FROM book WHERE isbn = '" + rBookinfo.getString("isbn") + "'";
            	ResultSet rBook = connection.createStatement().executeQuery(qBook);
            	
            	while (rBook.next()) {
            		String bookID = rBook.getString("book_id");
            		bookIDs.add(bookID);
            		
            		String qReserve = "SELECT * FROM reserve WHERE book_id = '"
            						+ bookID + "' ORDER BY date DESC";
            		ResultSet rReserve = connection.createStatement().executeQuery(qReserve);
            		if (rReserve.next()) {
						resDate = rReserve.getString("date");
					}
            		
            		String qBorrow = "SELECT * FROM borrow WHERE book_id = '"
            						+ bookID + "' ORDER BY b_date DESC";
            		ResultSet rBorrow = connection.createStatement().executeQuery(qBorrow);
            		if (rBorrow.next()) {
						returnDate = rBorrow.getString("r_date");
					}
            		
            		if (resDate != null && resDate==currDate) {
						bookStatus.add("Reserved");
					}
            		
            		else if (returnDate == null) {
						bookStatus.add("Borrowed");
					}
            		else {
						bookStatus.add("Available");
					}
            	}
            }
            
            bookModel.setRowCount(0);
    		for (int i = 0; i < bookIDs.size(); i++) {
    			bookModel.addRow(new String[] {bookIDs.get(i), bookStatus.get(i)});
    		}
    		
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
		SearchBook test = new SearchBook();
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
