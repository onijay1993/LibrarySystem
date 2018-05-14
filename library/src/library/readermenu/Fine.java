package library.readermenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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
public class Fine extends JFrame{
	
	private Connection connection;
	private String uid;
	
	JLabel welLabel;
	private DefaultTableModel fineModel;
	private JTable fineTable;
	private JScrollPane finePane;
	JButton backButton;
	
	public Fine() {
		makeConnection();
		this.uid = "10000";
		initial();
		setVisible(true);
	}
	
	public Fine(Connection connection, String uid) {
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
        
		setTitle("Fine");
		setSize(800, 600);
        setLocation((screenWidth - 800) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		welLabel = new JLabel("Fine");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		buildFineTable();
		fineTable.setEnabled(false);
		finePane = new JScrollPane(fineTable);
		
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
        				.addComponent(finePane)
        				.addComponent(backButton)
        				)
        		.addGap(40)
        		);
        
        layout.setVerticalGroup(layout.createSequentialGroup()
        		.addGap(40)
				.addComponent(welLabel)
				.addComponent(finePane)
				.addComponent(backButton)
        		);
	}
	
	private void buildFineTable() {
		String[][] datas = {};
		String[] titles = {"Book ID", "Borrow Date","Return Date", "Days passed", "Fine"};
		fineModel = new DefaultTableModel(datas, titles);
		fineTable = new JTable(fineModel);
		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		fineTable.setDefaultRenderer(Object.class, r);
		
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        long pass = 0;
		
		try {
			String qBorrow = "SELECT * FROM borrow WHERE reader_id = '" + uid + "'";
			ResultSet rBorrow = connection.createStatement().executeQuery(qBorrow);
			
			while (rBorrow.next()) {
				if (rBorrow.getString("r_date") != null && Float.parseFloat(rBorrow.getString("fee"))>0) {
					pass = dateFormat.parse(rBorrow.getString("r_date")).getTime()
							- dateFormat.parse(rBorrow.getString("b_date")).getTime();
					pass = pass / (1000*60*60*24);
					
					fineModel.addRow(new String[] {rBorrow.getString("book_id"), rBorrow.getString("b_date"),
							rBorrow.getString("r_date"), Long.toString(pass), rBorrow.getString("fee")});
				}
				else {
					pass = currentDate.getTime()
							- dateFormat.parse(rBorrow.getString("b_date")).getTime();
					pass = pass / (1000*60*60*24);
					float fine = 0;
					if (pass > 20) {
						pass = pass - 20;
						fine = (float) (0.2 * pass); 
					}
					else
						fine = 0;
					
					fineModel.addRow(new String[] {rBorrow.getString("book_id"), rBorrow.getString("b_date"),
							"--", Long.toString(pass), Float.toString(fine)});
				}
				
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
		Fine test = new Fine();
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
