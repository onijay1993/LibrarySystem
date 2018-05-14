package library.adminmenu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
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
public class TopBorrowers extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private JLabel branchLabel;
	private JComboBox<String> branchBox;
	private JButton searchButton;
	private DefaultTableModel borrowersModel;
	private JTable borrowersTable;
	private JScrollPane borrowersPane;
	private JButton backButton;
	
	
	public TopBorrowers() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public TopBorrowers(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Top 10 Borrowers");
		setSize(700, 600);
        setLocation((screenWidth - 700) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Top 10 Borrowers");
		welLabel.setFont(new Font("Arial", 0, 30));
		
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
			searchButtonListener();
		});
		
		String[][] datas = {};
		String[] titles = {"No.", "Reader ID", "Count"};
		borrowersModel = new DefaultTableModel(datas, titles);
		borrowersTable = new JTable(borrowersModel);
		borrowersTable.setEnabled(false);
		borrowersPane = new JScrollPane(borrowersTable);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		borrowersTable.setDefaultRenderer(Object.class, r);
		
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
                        		.addComponent(branchLabel)
                				.addComponent(branchBox, 200, 200, 200)
                				)
        				.addComponent(searchButton)
        				.addComponent(borrowersPane)
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
						.addComponent(branchLabel)
						.addComponent(branchBox, 30, 30, 30)
						)
				.addComponent(searchButton)
				.addComponent(borrowersPane)
				.addGroup(layout.createParallelGroup()
						.addComponent(backButton)
						)
        		);
	}
	
	private void searchButtonListener() {
		List<String> id = new ArrayList<>();
		List<String> count = new ArrayList<>();
		
		String branchName = branchBox.getSelectedItem().toString();
		String branchID = "";
		for (int i = 0; i < branchName.length(); i++) {
			if (branchName.charAt(i) == '\'') {
				String tmp = branchName.substring(0, i);
				branchName = tmp + "\'" + branchName.substring(i);
				i++;
			}
		}
		
		try {
            String qBranch = "SELECT * FROM branch WHERE name = '" + branchName + "'";
            ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
            if (rBranch.next()) {
				branchID = rBranch.getString("branch_id");
			}
            
            String qBorrow = "SELECT reader_id, COUNT(*) as count FROM borrow WHERE branch_id = '"
            				+ branchID + "' GROUP BY reader_id ORDER BY count DESC";
            ResultSet rBorrow = connection.createStatement().executeQuery(qBorrow);
            while (rBorrow.next()) {
				id.add(rBorrow.getString("reader_id"));
				count.add(rBorrow.getString("count"));
			}
            
            borrowersModel.setRowCount(0);
            if (id.size() == 0) {
				
			}
            else {
            	for (int i = 0; i < id.size() && i < 10; i++) {
            		borrowersModel.addRow(new String[] {Integer.toString(i+1), id.get(i), count.get(i)});
            	}
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
		TopBorrowers test = new TopBorrowers();
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
