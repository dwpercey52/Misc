/**
 * Class: DisplayUpdates
 * @author Darrell Percey
 * On the launch of the program this Jframe will display all the
 * updates pulled from the server to give the user a notice.
 */

package ffxiArmory;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Dimension;
import java.awt.FlowLayout;

public class DisplayUpdates extends JFrame{
	
	
	JPanel mainPanel = new JPanel();
	String updateText;
	JTextArea textBox;
	
	
	public DisplayUpdates() throws Exception {
		initUI();
	}
	
	/* Function: initUI()
	 * Build the base UI and push text update into it.
	 */
	public void initUI() throws Exception {
		SQLAccess grabUpdate = new SQLAccess();
		
		
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.setPreferredSize(new Dimension(230,180));
		updateText = grabUpdate.updateNotice("SELECT * FROM updateNotice where id = 1");
		textBox = new JTextArea(updateText);
		textBox.setPreferredSize(new Dimension(200,170));
		textBox.setBorder(BorderFactory.createTitledBorder("Notes"));
		textBox.setEditable(false);
		textBox.setLineWrap(isEnabled());
		textBox.setWrapStyleWord(isEnabled());
		
		mainPanel.add(textBox);
		add(mainPanel);
		
		setResizable(false);
		setTitle("Update");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
		
	}


}
