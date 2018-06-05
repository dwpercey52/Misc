
import java.io.IOException;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ServerDisplay extends Application implements EventHandler<ActionEvent>{


	private Server serv;
	private BorderPane lay;
	private ScrollPane textScroll;
	private TextArea updateLog;
	//private TextArea clientList;
	private Button startButton = new Button("Start");
	private Button stopButton = new Button("Stop");
	private Button clientButton = new Button("Clients");
	private VBox buttonHolder;
	private int portNum = 60000;
	private boolean isStarted = false;
	
	
	public static void main(String[] args) {
		launch(args);
	
		
	//	Application.launch(ServerDisplay.class, args);
		
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
		
		//Add button listeners
		startButton.setOnAction(this);
		stopButton.setOnAction(this);
		clientButton.setOnAction(this);
		
		//launch stage layout
		stage.setTitle("Server Display");
		stage.setScene(new Scene(startBox, 300, 160));
		stage.show();
	}
	
	
	public VBox setUpPort(Stage stage) {
		//Setup VBox for server startup
		VBox portBox = new VBox(30);
		HBox portBox2 = new HBox(20);
		VBox portTotal = new VBox(10);
		portBox.setAlignment(Pos.TOP_CENTER);
		portBox2.setAlignment(Pos.TOP_CENTER);
		portTotal.setAlignment(Pos.TOP_CENTER);
		
		//Label for top box
		Label question = new Label("Pick a port number for the server. \n (Pick: 49152-65535)");
		Button portButton = new Button("Setup");
		
		//Label and textfield for bottom box
		Label portLabel = new Label("Port: ");
		TextField portInput = new TextField("Port");
		Label error = new Label("Not a valid port. (49152-65535)");
		error.setTextFill(Color.web("#ee2222"));
		error.setVisible(false);
		
		//Setups up the port of which the server will be hosted.
		portButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String temp = portInput.getText();
				if(isValidPort(temp)) {
					portNum = Integer.parseInt(temp);
					System.out.println("Port number is " + portNum);
					stage.setScene(new Scene(lay, 600, 400));
					try {
						serverSetUp();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					System.out.println("Not a valid port.");
					error.setVisible(true);
				}
			}
		});
		
		//Adds children to the appropriate parent object.
		portBox.getChildren().addAll(question);
		portBox2.getChildren().addAll(portLabel, portInput);
		portTotal.getChildren().addAll(portBox, portBox2, portButton, error);
		return portTotal;
	}
	
	private void serverSetUp() throws IOException {
		serv = new Server(portNum, this);
	}
	
	public boolean isValidPort(String port) {
		boolean isValid = false;
		int x;
		
		try {
			x = Integer.parseInt(port);
			if(x >= 49152 && x <= 65535) {
				isValid = true;
			}
		}catch(NumberFormatException e){
			isValid = false;
		}
		
		return isValid;
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

	
	@Override
	public void handle(ActionEvent e) {
		
		if(e.getSource() == startButton) {
			
			if(isStarted == false) {
				System.out.println("You are starting up the server.");
				updateLog.appendText("Server is starting up... \n");
				serv.start();
				isStarted = true;
			}
			else {
				updateLog.appendText("Server is already launched. \n");
			}
		}
		else if(e.getSource() == stopButton) {
			if(isStarted == true) {
				System.out.println("You are shutting down the server.");
				updateLog.appendText("Server is shutting down... \n");
				isStarted = false;
			}
			else {
				updateLog.appendText("Server is already shutdown. \n");
			}
		}
		
		
	}
	
	public void newMessage() {
		
	}
	
	public void newClient() {
		
	}
	
}
