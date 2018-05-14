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
public class Branch extends JFrame {
	
	private Connection connection;
	
	private JLabel welLabel;
	private DefaultTableModel branchModel;
	private JTable branchTable;
	private JScrollPane branchPane;
	private JButton backButton;
	
	
	public Branch() {
		makeConnection();
		initial();
		setVisible(true);
	}
	
	public Branch(Connection connection) {
		this.connection = connection;
		initial();
		setVisible(true);
	}
	
	
	private void initial() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        
		setTitle("Branch");
		setSize(700, 600);
        setLocation((screenWidth - 700) / 2, (screenHeight - 600) / 2);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		welLabel = new JLabel("Branch");
		welLabel.setFont(new Font("Arial", 0, 30));
		
		String[][] datas = {};
		String[] titles = {"Name", "Location"};
		branchModel = new DefaultTableModel(datas, titles);
		branchTable = new JTable(branchModel);
		branchTable.setEnabled(false);
		branchPane = new JScrollPane(branchTable);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		branchTable.setDefaultRenderer(Object.class, r);
		
		try {
			List<String> branchName = new ArrayList<>();
			List<String> branchLocation = new ArrayList<>();
			
            String qBranch = "SELECT * FROM branch";
            ResultSet rBranch = connection.createStatement().executeQuery(qBranch);
            
            while (rBranch.next()) {
            	branchName.add(rBranch.getString("name"));
            	branchLocation.add(rBranch.getString("location"));
            }
            
            branchModel.setRowCount(0);
    		for (int i = 0; i < branchName.size(); i++) {
    			branchModel.addRow(new String[] {branchName.get(i), branchLocation.get(i)});
    		}
    		
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		}
		
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
        				.addComponent(branchPane)
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
				.addComponent(branchPane)
				.addGap(20)
				.addComponent(backButton)
        		);
	}
	
	
	private void backButtonListener() {
		dispose();  
		AdminMenu adminMenu = new AdminMenu(connection);
		adminMenu.setVisible(true);
	}
	
	public static void main(String[] args) {
		initGobalFont(new Font("Arial", Font.PLAIN, 20));
		Branch test = new Branch();
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
