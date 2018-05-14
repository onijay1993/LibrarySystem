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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import library.menu.ReaderMenu;

@SuppressWarnings("serial")
public class ReservedList extends JFrame{
	
	private Connection connection;
	private String uid;
	
	JLabel welLabel;
	private DefaultTableModel reserveModel;
	private JTable reserveTable;
	private JScrollPane reservePane;
	JButton backButton;
	
	public ReservedList() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public ReservedList(Connection connection, String uid) {
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
        
		setTitle("Reserved List");
		setSize(800, 600);
        setLocation((screenWidth - 800) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		welLabel = new JLabel("Reserved List");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		buildReserveTable();
		
		backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			returnButtonListener();
		});
		
		
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGap(40)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
        				.addComponent(welLabel)
        				.addComponent(reservePane)
        				.addComponent(backButton)
        				)
        		.addGap(40)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(40)
				.addComponent(welLabel)
				.addComponent(reservePane)
				.addComponent(backButton)
        		);
	}
	
	private void buildReserveTable() {
		String[][] datas = {};
		String[] titles = {"Book ID", "Borrow Title","Availability"};
		reserveModel = new DefaultTableModel(datas, titles);
		reserveTable = new JTable(reserveModel);
		
		reserveTable.setEnabled(false);
		reservePane = new JScrollPane(reserveTable);
		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		reserveTable.setDefaultRenderer(Object.class, r);
		
		try {
			String qReserve = "SELECT * FROM reserve WHERE reader_id = '" + uid + "'";
			ResultSet rReserve = connection.createStatement().executeQuery(qReserve);
			List<String> bookIDs = new ArrayList<>();
			List<String> bookTitles = new ArrayList<>();
			List<String> availibities = new ArrayList<>();
			
			while (rReserve.next()) {
				String availibity = "unavailable";
				DateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
				Date currentDate = dateFormat1.parse(dateFormat1.format(new Date()));
				Date cancelDate = dateFormat1.parse("18:00");
				
				if (currentDate.before(cancelDate)) {
			        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			        currentDate = dateFormat2.parse(dateFormat2.format(new Date()));
					Date reserveDate = dateFormat2.parse(rReserve.getString("date"));
					
					if (!currentDate.after(reserveDate)) {
						availibity = "available";
					}
				}
				bookIDs.add(rReserve.getString("book_id"));
				availibities.add(availibity);
				
				String qBook = "SELECT * FROM book WHERE book_id = '" + rReserve.getString("book_id") + "'";
				ResultSet rBook = connection.createStatement().executeQuery(qBook);
				
				while (rBook.next()) {
					String qBookinfo = "SELECT * FROM book_info WHERE isbn = '"
										+ rBook.getString("isbn") + "'";
					ResultSet rBookinfo = connection.createStatement().executeQuery(qBookinfo);
					
					while (rBookinfo.next()) {
						bookTitles.add(rBookinfo.getString("title"));
					}
				}
			}
			
			for (int i = 0; i < bookIDs.size(); i++) {
				reserveModel.addRow(new String[] {bookIDs.get(i), bookTitles.get(i), availibities.get(i)});
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
	}
	
	private void returnButtonListener() {
		dispose();
		ReaderMenu readerMenu = new ReaderMenu(connection, uid);
		readerMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		ReservedList test = new ReservedList();
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
