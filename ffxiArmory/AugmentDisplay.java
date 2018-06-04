/**
 * Class: AugmentDisplay
 * @author Darrell Percey
 * If the user selects Augment button while an item is picked
 * then it will display the augment information of the item if 
 * it is availiable.
 */

package ffxiArmory;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AugmentDisplay extends JFrame {
	
		
	JPanel mainPanel = new JPanel();
	String augLink = "No Augments";
	JTextArea textBox;
	JLabel augPic;
	String errorDisp = "No augments available. If this item should have augments please report this error.";
		
	public AugmentDisplay(String item) throws Exception {
		augLink = item;
		initUI();
	}
		
		public void initUI() throws IOException{
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		//Make sure the augment information is there, else display it's not possible to display
		if(augLink.equalsIgnoreCase("No Augments") || augLink == "" || augLink == null) {
			textBox = new JTextArea(errorDisp);
			textBox.setPreferredSize(new Dimension(200,100));
			textBox.setEditable(false);
			textBox.setLineWrap(isEnabled());
			textBox.setWrapStyleWord(isEnabled());
			mainPanel.setPreferredSize(new Dimension(200,100));
			mainPanel.add(textBox);
		}
		else {
			augPic = new JLabel();
			URL urlConn = new URL(augLink);
			HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.53 Safari/525.19");
			BufferedImage imgOne = ImageIO.read(conn.getInputStream());	
			mainPanel.setPreferredSize(new Dimension(imgOne.getWidth()+10, imgOne.getHeight()+10));
			augPic.setPreferredSize(new Dimension(imgOne.getWidth()+5, imgOne.getHeight()+5));
			augPic.setIcon(new ImageIcon(imgOne));
			mainPanel.add(augPic);
		}	
		
		
		add(mainPanel);
			
		setResizable(false);
		setTitle("Augments");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
}

