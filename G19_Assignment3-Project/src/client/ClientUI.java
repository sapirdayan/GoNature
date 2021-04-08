package client;
import javafx.application.Application;

import javafx.stage.Stage;


import gui.LandingPageContoller;


public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage primaryStage1 = new Stage();
		 chat= new ClientController("localhost", 5555);
		 
		 //chat= new ClientController("192.168.1.126", 5555);
		
		 
		 
		 LandingPageContoller landingPageContoller=new LandingPageContoller();
		 landingPageContoller.start(primaryStage1);
	}
	
	
}
