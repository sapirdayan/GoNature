package gui;

import java.net.InetAddress;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Server.ServerUI;

/**
 * A class that activates the screen of the connection to the server
 * @author Nasmat Amr
 *
 */
public class ServerPortFrameController {
	String temp = "";

	@FXML
	private Button ConnectBtn;

	@FXML
	private TextField portxt;

	@FXML
	private Label ConnLabel;

	@FXML
	private Label NameLabel;
	
	 @FXML
	    private Label PortLabel1;

	@FXML
	private Label IPLabel;

	ObservableList<String> list;


	public void ConnectFunc(ActionEvent event) throws Exception {
			ServerUI.runServer("5555");
			ConnLabel.setText("Connected");
			PortLabel1.setText("5555");
			IPLabel.setText(InetAddress.getLocalHost().getHostAddress());
			NameLabel.setText(InetAddress.getLocalHost().getHostName());
			
		}


	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
