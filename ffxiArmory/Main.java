/**
 * Class: Main
 * @author Darrell Percey
 * Main method for the program to run, builds the DisplayUpdates
 * and all the main application GUI as well as building the sorted list.
 */

package ffxiArmory;


public class Main {

	public static void main(String[] args) throws Exception {
		String [] testList;
		SQLAccess fullList = new SQLAccess();
		testList = fullList.updateList("select * from weapons ORDER BY name");
		GuiBuilder mainFrame = new GuiBuilder(testList);
		mainFrame.setVisible(true);
		DisplayUpdates updatePanel = new DisplayUpdates();
		updatePanel.setVisible(true);
	}
}
