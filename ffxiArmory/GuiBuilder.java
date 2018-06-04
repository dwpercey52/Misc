/**
 * Class: GuiBuilder
 * @author Darrell Percey
 * This is the main frame which contains two smaller frames TopPanel
 * and BottomPanel. The top panel handles the search drop list while the 
 * bottom panel handles all of the item information such as links, augments,
 * and pictures of the item. This is done for organization. 
 */
package ffxiArmory;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GuiBuilder extends JFrame {
	
	String[] fullList;
	
	//Constructors for GuiBuilder class
	public GuiBuilder() {
		setTitle("FFXI Armory");
		initUI();
	}
	
	public GuiBuilder(String[] list) {
		setTitle("FFXI Armory");
		fullList = list;
		initUI();
	}

	
	/* Function: initUI()
	 * Builds the overall block that will call the Top and bottom panels
	 * This will connect all of the panels together to make the full
	 * GUI.
	 */
	private void initUI() {
		
		//Setup main frame and two panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		BottomPanel botPan = new BottomPanel();
		TopPanel topPan = new TopPanel(botPan, fullList);
		

		mainPanel.add(topPan);
		mainPanel.add(botPan);
		add(mainPanel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		
	}
}



/**
 * Class: DatabaseThread
 * @author Darrell Percey
 * Database thread allows for multi-threading of any query to the server
 * When doing a query to the server there is a slight moment of delay retrieving and 
 * displaying the information. By multi-threading the query we allow the GUI not to appear
 * "frozen" for a split second. 
 */
class DatabaseThread extends Thread{
	
	JComboBox items;
	String classes;
	String tables;
	String types;
	String zones;
	
	
	public DatabaseThread(JComboBox items, String classes, String tables, String types, String zones ) {
		this.items = items;
		this.classes = classes;
		this.tables = tables;
		this.types = types;
		this.zones = zones;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SQLAccess infoGrab = new SQLAccess();
		String[] itemList = {};
		String query = BuildString(classes, tables, types, zones);
	//	System.out.println(query);
		try {
			itemList = infoGrab.updateList(query);
			DefaultComboBoxModel model = new DefaultComboBoxModel(itemList);
			items.setModel(model);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
	
	/* Function: BuildString(String,String,String,String) / Return String
	 * This methods allows us to build a query for the MySQL server by taking
	 * all of the values that the user has selected in the dropdown menus and 
	 * building a query string from it.
	 */
	private String BuildString(String classes, String table, String type, String zone){
			
		String temp = "";
		zone = checkName(zone);
			
		if(table == "All") {
			temp = "select name from weapons";
			temp = temp + BuildInnerString(classes, type, zone,"", 1);
			temp = temp + " " + "ORDER BY name";
		}
		else if(table == "Weapon"){
			temp = "select name from weapons";
			temp = temp + BuildInnerString(classes, type, zone,"isWeap=true", 0);
			temp = temp + " " + "ORDER BY name";
		}
		else if(table == "Armor"){
			temp = "select name from weapons";
			temp = temp + BuildInnerString(classes, type, zone,"isWeap=false", 0);
			temp = temp + " " + "ORDER BY name";
		}
		return temp;
			
	}
		
	
	/* Function: checkName(String) / Return String
	 * Checks for "'" in the strings to place the "\\" in it's place
	 * so the MySQL database will understand it.
	 */
	private String checkName(String input) {
		String temp = "";
		int tempNum;
		
		tempNum = input.indexOf('\'');
		if(tempNum != -1) {
			temp = input.substring(0, tempNum) + "\\" + input.substring(tempNum, input.length()); 
		}
		else {
			temp = input;
		}
		
		return temp;
	}
	
	/* Function: BuildInnerString(String,String,String,String) / Return String
	 * A piece of buildString above. They work together to finish an entire string.
	 */
	private String BuildInnerString(String classes, String type, String zone, String addFil, int filter) {
		String temp = "";
		String classStr = "";
		String typeStr = "";
		String zoneStr = "";
		boolean zoneB = false;
		boolean classB = false;
		boolean typeB = false;
			
			
			
		if(classes != "All") {
			classStr = " " + classes + "=true ";
			classB = true;
		}
			
		if(type != "All") {
			typeStr = " type='" + type + "' ";
			typeB = true;
		}
			
		if(zone != "All") {
			zoneStr = " zone='" + zone + "' ";
			zoneB = true;
		}
			
			
		if(classB == true && typeB == true && zoneB == true ) {
			temp = " WHERE " + classStr + " AND " + typeStr +  " AND " + zoneStr;
		}
		else if(classB == true && typeB == true) {
			temp = " WHERE " + classStr + " AND " + typeStr;
		}
		else if(classB == true && zoneB == true){
			temp = " WHERE " + classStr + " AND " + zoneStr;
		}
		else if(typeB == true && zoneB == true) {
			temp = " WHERE " + typeStr + " AND " + zoneStr;
		}
		else if(classB == true) {
			temp = " WHERE " + classStr;
		}
		else if(typeB == true) {
			temp = " WHERE " + typeStr;
		}
		else if(zoneB == true) {
			temp = " WHERE " + zoneStr;
		}
		else {
			temp = "";
		}
			
		if(filter == 0 && (zoneB == true || typeB == true || classB == true)) {
			temp = temp + " AND " + addFil;
		}
		else if(filter == 0) {
			temp = " WHERE " + addFil;
		}
			
		return temp;
	}
}




/**
 * Class: TopPanel
 * @author Darrell Percey
 * This is the upper layer of the program that holds all of the scroll boxes
 * These will allow the user to filter what items they are looking for by
 * zone, class, type, and weap/armor.
 */
@SuppressWarnings("serial")
class TopPanel extends JPanel{
	
	//Predefined strings for sorting the items in the program
	String[] zoneList = {"All", "Escha Zi'tah", "Escha Ru'aun", "Reisenjima"};
	String[] classList = {"All", "WAR", "MNK", "WHM", "BLM", "RDM", "THF", "PLD", "DRK", "BST", "BRD", "RNG", "SAM", "NIN", "DRG",
			"SMN", "BLU", "COR", "PUP", "DNC", "SCH", "GEO", "RUN"};
	String[] tableList = {"All", "Weapon", "Armor"};
	String[] typeList = {"All", "Hand-to-Hand", "Dagger", "Sword", "Great Sword", "Axe", "Great Axe", "Scythe", "Polearm", "Katana", "Great Katana", "Staff",
			"Club", "Ranged", "Ammo", "Shield", "Head", "Neck", "Body", "Hands", "Waist", "Legs", "Feet", "Back", "Earrings", "Rings"};
	//Test List
	String[] itemList;
	
	
	//BottomPanel reference and scrollboxes
	BottomPanel updatePic;
	final JComboBox<String> classes = new JComboBox<>(classList);
	final JComboBox<String> tables = new JComboBox<>(tableList);
	final JComboBox<String> types = new JComboBox<>(typeList);
	final JComboBox<String> zone = new JComboBox<>(zoneList);
	
	
	JButton loginButt = new JButton("Login");
	JComboBox loginMenu = new JComboBox();
	JButton saveButt = new JButton("Save");
	JButton deleteButt = new JButton("Delete");
	//final SQLAccess infoGrab = new SQLAccess();
	
	
	
	public TopPanel() {
		initUI();
	}
	
	
	public TopPanel(BottomPanel bot, String[] firstList) {
		updatePic = bot;
		itemList = firstList;
		initUI();
	}
	
	
	
	/*Function: initUI()
	 * Sets all the listeners and borders for all the TopPanel
	 * components.
	 */
	private void initUI() {
		
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(580,190));
		//Declare static JComboBox list

		
		//Declare and set borders for JComboBox Class, Slot, Type, Name
		//Update the Item JComboBox
		final JComboBox<String> items = new JComboBox<>(itemList);
 		
 		classes.setBorder(BorderFactory.createTitledBorder("Class"));
 		zone.setBorder(BorderFactory.createTitledBorder("Zone"));
 		tables.setBorder(BorderFactory.createTitledBorder("Slot"));
 		types.setBorder(BorderFactory.createTitledBorder("Type"));
 		items.setBorder(BorderFactory.createTitledBorder("Name"));
 		
 		
 		/*
 		 * multithreads request queries to the SQL server when a filter is picked from
 		 * any of the scroll boxes below
 		 */
 		classes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabaseThread updateThread = new DatabaseThread(items, classes.getSelectedItem().toString(), 
						tables.getSelectedItem().toString(), types.getSelectedItem().toString(), zone.getSelectedItem().toString());
				//Calls thread
				updateThread.start();
				
			}
		});
		
		tables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabaseThread updateThread = new DatabaseThread(items, classes.getSelectedItem().toString(), 
						tables.getSelectedItem().toString(), types.getSelectedItem().toString(), zone.getSelectedItem().toString());
				//Calls thread
				updateThread.start();
			}
		});
		
		//Set up type filter actionlistener
		types.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabaseThread updateThread = new DatabaseThread(items, classes.getSelectedItem().toString(), 
						tables.getSelectedItem().toString(), types.getSelectedItem().toString(), zone.getSelectedItem().toString());
				//Calls thread
				updateThread.start();
			}
		});
		
		//Set up zone filter actionlistener
		zone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabaseThread updateThread = new DatabaseThread(items, classes.getSelectedItem().toString(), 
						tables.getSelectedItem().toString(), types.getSelectedItem().toString(), zone.getSelectedItem().toString());
				//Calls thread
				updateThread.start();
			}
		});
		
		/*
		 * END OF Scroll boxes
		 */
		
		items.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Call for new info on item name
				String name = new String();
				name = items.getSelectedItem().toString();
				PicThread picUp = new PicThread(updatePic, name);
				picUp.start();
				
			}
		});
		
		loginButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//open frame;
				loginButt.setVisible(false);
				loginMenu.setVisible(true);
				saveButt.setVisible(true);
				deleteButt.setVisible(true);
				
			}
		});
		
		
		loginButt.setPreferredSize(new Dimension(580, 30));
		loginMenu.setPreferredSize(new Dimension(550, 30));
		saveButt.setPreferredSize(new Dimension(290, 30));
		deleteButt.setPreferredSize(new Dimension(280,30));
		loginMenu.setVisible(false);
		saveButt.setVisible(false);
		deleteButt.setVisible(false);
		
		//Pick preferred size for FlowLayout manager
		classes.setPreferredSize(new Dimension(140,50));
		tables.setPreferredSize(new Dimension(140,50));
		types.setPreferredSize(new Dimension(140,50));
		zone.setPreferredSize(new Dimension(140,50));
		items.setPreferredSize(new Dimension(550,50));
		
		add(classes);
		add(tables);
		add(types);
		add(zone);
		add(items);
		add(loginButt);
		add(loginMenu);
		add(saveButt);
		add(deleteButt);
		
	}
}





class PicThread extends Thread{

	BottomPanel botPan;
	String item;
	String[] temp = {"","",""};
	
	public PicThread(BottomPanel picBot, String item) {
		botPan = picBot;
		this.item = item;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SQLAccess infoGrab = new SQLAccess();
		try {
			temp = infoGrab.findLink(item);
			botPan.SetItem(temp[0]);
			botPan.SetInfo(temp[1]);
			botPan.SetAug(temp[2]);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}





/**
 * Class: BottomPanel
 * @author Darrell Percey
 * The bottom panel allows for one picture panel and one panel
 * for two buttons augments and info. These will allow the user
 * to open a frame to see which augments are allowed on the item
 * the info button will have a hyperlink to BGWIKI
 */
@SuppressWarnings("serial")
class BottomPanel extends JPanel{
	
	//class-wide so they can be changed by set methods
	JButton augButt = new JButton("Augment");
	JButton infoButt = new JButton("Info");	
	JLabel picPanel = new JLabel();
	JPanel picPanHolder = new JPanel();
	
	//Pulled from SQL database for information
	public String itemLink = "";
	public String augLink = "";
	public String infoLink = "";
	
	public BottomPanel() {
		initUI();
	}
	
	private void initUI() {
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
	//	picPanHolder.setLayout(new FlowLayout(FlowLayout.CENTER)); 
		picPanHolder.setAlignmentX(CENTER_ALIGNMENT);
		picPanHolder.setAlignmentY(CENTER_ALIGNMENT);
		//Split the into two panels
		picPanel.setPreferredSize(new Dimension(550,290));
		picPanel.setAlignmentX(CENTER_ALIGNMENT);
		picPanel.setAlignmentY(CENTER_ALIGNMENT);
		
		//Bottom panel of the duo panel for the buttons
		JPanel buttonPanel = new JPanel();
		
		augButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					AugmentDisplay aug = new AugmentDisplay(augLink);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		infoButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URL(infoLink).toURI());
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				
		});
		
		//Button bottom panel layout flow for stretch 
		buttonPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(augButt);
		augButt.setPreferredSize(new Dimension(280,30));
		
		buttonPanel.add(infoButt);
		infoButt.setPreferredSize(new Dimension(280,30));
		
		picPanHolder.add(picPanel);
		add(picPanHolder);
		add(buttonPanel);
		
	}
	
	
	//use to change the picture from the JComboBox change in TopPanel
	public void SetItem(String newLink) throws IOException {
		itemLink = newLink;
	//	System.out.println(itemLink);
		URL urlConn = new URL(itemLink);
		HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.53 Safari/525.19");
		
		BufferedImage imgOne = ImageIO.read(conn.getInputStream());	
		Image sizeImgOne = imgOne.getScaledInstance(picPanel.getWidth(), picPanel.getHeight(), Image.SCALE_SMOOTH);
		
		picPanel.setIcon(new ImageIcon(sizeImgOne));
		
	}
	
	//use to update the augment button from the JComboBox in TopPanel
	public void SetAug(String newLink) {
		//set new link when calling Augment Frame
		augLink = newLink;
	}
	
	//use to update the info hyperlink from the JComboBox in TopPanel
	public void SetInfo(String newLink){
		infoLink = newLink;
	}
	
}