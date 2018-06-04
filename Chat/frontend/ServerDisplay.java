package frontend;
import java.io.IOException;

import backend.Server;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerDisplay extends Application{


	private Server serv;
	private BorderPane lay;
	private ScrollPane textScroll;
	private TextArea updateLog;
	private TextArea clientList;
	private Button startButton = new Button("Start");
	private Button stopButton = new Button("Stop");
	private Button clientButton = new Button("Clients");
	private VBox buttonHolder;
	private int portNum = 60000;
	
	public static void main(String[] args) {
		Application.launch(ServerDisplay.class, args);
		
	}
	
	public void start(Stage stage) throws IOException {
		
		//First layout for port information
		VBox startBox = setUpPort(stage);
	
		
		//Main layout setup
		lay = new BorderPane();
		setUpVBox();
		lay.setRight(buttonHolder);
		setUpLog();
		lay.setCenter(textScroll);
		
		
		
		//FXMLLoader loadin = new FXMLLoader();
		//Parent root = loadin.load(getClass().getResource("ServerFXML.fxml"));
	//	ServerController cont = loadin.getController();
	//	ServerController control = new ServerController();
	//	loadin.setController(control);
	//	serv = new Server(control);
	//	control.setServer(serv);
		
		
		
		stage.setTitle("Server Display");
//		stage.setScene(new Scene(lay, 600, 400));
		stage.setScene(new Scene(startBox, 300,300));
		stage.show();
	}
	
	public VBox setUpPort(Stage stage) {
		VBox portBox = new VBox(30);
		portBox.setAlignment(Pos.TOP_CENTER);
		Label question = new Label("Pick a port number for the server. \n(Default: 60000)");
		Button portButton = new Button("Setup");
		portButton.setOnAction(e -> stage.setScene(new Scene(lay, 600, 400)));
		portBox.getChildren().addAll(question, portButton);
		
		return portBox;
	}
	
	public void setUpVBox() {
		
		//Design and structure of the VBox on the right side for holding the buttons
		buttonHolder = new VBox(20, startButton, stopButton, clientButton);
		buttonHolder.setAlignment(Pos.TOP_CENTER);
		buttonHolder.setPadding(new Insets(20, 0, 20, 0));
		buttonHolder.setPrefSize(100, 400);
		buttonHolder.setMinSize(100, Region.USE_COMPUTED_SIZE);
		buttonHolder.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
		//Structure of the buttons in the Vbox on the right side
		startButton.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
		stopButton.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
		clientButton.setMinSize(Region.USE_PREF_SIZE,Region.USE_PREF_SIZE);
		startButton.setPrefSize(80, 30);
		stopButton.setPrefSize(80, 30);
		clientButton.setPrefSize(80, 30);
	}
	
	//Setup sizing for the center block of the blockpane
	public void setUpLog() {
		//gives the textarea a scroll pane to contiune to build
		textScroll = new ScrollPane();
		textScroll.setMinSize(1, Region.USE_PREF_SIZE);
		textScroll.setPrefSize(500, 400);
		textScroll.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		
		//Makes the textarea 
		updateLog = new TextArea();
		updateLog.setEditable(false);
		updateLog.setMinSize(1, Region.USE_PREF_SIZE);
		updateLog.setPrefSize(500, 400);
		updateLog.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		textScroll.setContent(updateLog);
	}
}
