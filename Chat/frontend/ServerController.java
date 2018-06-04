package frontend;



import backend.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerController {
	
	@FXML Button startButton;
	@FXML TextArea text;
	public Server serv;
	
	
	ServerController(){
		
	}
	
	public void onEvent() {
		text.setText("FXML");
	}
	
	public void setServer(Server inServ) {
		serv = inServ;
	}
}
