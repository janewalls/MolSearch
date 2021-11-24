import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SQLConnectFrame extends JFrame implements ActionListener {

	private JLabel hostLab, userLab, passLab;
	private JTextField hostText, userText;
	private JPasswordField passText;
	private JButton connect;
	private JPanel centre, south;

	private String hostEntry;
	private String userEntry;
	private String passEntry;
	
	private Connect cnt;
	private Connection conn;
	
	private SDBMaker maker;
	private CompoundList cList;
	private AtomList aList;
	private BondList bList;
	private StatsList sList;
	
	public SQLConnectFrame(SDBMaker maker) {
		this.maker = maker;
		this.cList = maker.getcList();
		this.aList = maker.getaList();
		this.bList = maker.getbList();
		this.sList = maker.getsList();
		
		// Creates pop up GUI
		this.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setSize(380,250);
	      setLocation(100,100);
	      setTitle("SQL Access");
	      
	
	    
	    centre = new JPanel();
	    south = new JPanel();
	  // Adds Labels for log in    
	    hostLab = new JLabel("Enter Host URL: ");
	    userLab = new JLabel("Enter Username: ");
	    passLab = new JLabel("Enter Password: ");
	  
	 // Adds text fields for log in
	    hostText = new JTextField(100);
	    userText = new JTextField(50);
	    passText = new JPasswordField(50);
	
	 // Creates connect button   
	    connect = new JButton("Connect");
	    connect.addActionListener(this);
	
	 // Adds all components to GUI
	    centre.setLayout(new GridLayout(0,2));
	    
	    centre.add(hostLab);
	    centre.add(hostText);
	    centre.add(userLab);
	    centre.add(userText);
	    centre.add(passLab);
	    centre.add(passText);
	    south.add(connect);
	 
	    
	    this.add(centre, BorderLayout.CENTER);
	    this.add(south, BorderLayout.SOUTH);
	      
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == connect) {
			
	// Gets text from field
			hostEntry = hostText.getText();
			userEntry = userText.getText();;
			passEntry = passText.getText();
			
			
			// closes window
			this.setVisible(false);
			
			String build = "";
			
			try {
		// Gets connection
				cnt = new Connect(hostEntry, userEntry, passEntry);
				conn = cnt.getConnection();
				Statement statement = conn.createStatement();
				
				maker.appendInputArea("\nSQL Connected.\n");
				
		// ----- Adds Compound data ------
			if(conn != null) {	
				for (int i=0; i<cList.getSize(); i++) {
					if(cList.getCompound(i).getFormatted() != null)
					{
						build = cList.getCompound(i).sqlUploadFormat();
						statement.addBatch(build);
					
					}
				}
				statement.executeBatch();
				statement.close();
				maker.appendInputArea("\nCompund data in server.\n");
	
		// ----- Adds Atom data ------
				statement = conn.createStatement();
			for (int i=0; i<aList.getSize(); i++)
			{
				if(aList.getAtom(i).getFormatted() != null)
				{
					build = aList.getAtom(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);
					
				}
			}
			statement.executeBatch();
			statement.close();
			maker.appendInputArea("\nAtom data in server.\n");
		
		// ----- Adds Bond data ------
			statement = conn.createStatement();
			
			for (int i=0; i<bList.getSize(); i++)
			{
				if(bList.getBond(i).getFormatted() != null)
				{
					build = bList.getBond(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);
					
				}
			}
			
			statement.executeBatch();
			statement.close();
			maker.appendInputArea("\nBond data in server.\n");
		
		// ----- Adds stats data ------
			statement = conn.createStatement();
			
			for (int i=0; i<sList.getSize(); i++)
			{
				if(sList.getStat(i).getFormatted() != null)
				{
					build = sList.getStat(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);

				}
			}
			statement.executeBatch();
			statement.close();
			maker.appendInputArea("\nParameter data in server.\n");
			
			}else {
				maker.appendInputArea("issue connecting");
			}
			}catch(SQLException e) {
				maker.appendInputArea("Issue adding to SQL");
			}
		}
	}

	public String getHostEntry() {
		return hostEntry;
	}

	public String getUserEntry() {
		return userEntry;
	}

	public String getPassEntry() {
		return passEntry;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	
}
