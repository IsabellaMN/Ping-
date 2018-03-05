
package gameclient;

import game.GameConstants;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class FXMLLobbyController implements Initializable, GameConstants  {

    private GameGateway gateway;
    private String myName = "";
    private String otherName = "";
    
    @FXML
    private Label lobby;
    
    @FXML 
    private Label lblTitle;
    
    @FXML
    private Label lblStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
        try {
        // Get notification from the server
        int player = gateway.getPlayerNumber();

        // Am I player 1 or 2?
        if (player == PLAYER1) {
        myName = "PLAYER 1";
        otherName = "PLAYER 2";
        Platform.runLater(() -> {
        lblTitle.setText("PLAYER 1");
        lblStatus.setText("Waiting for player 2 to join");
        });
        // Receive startup notification from the server
        gateway.getPlayerNumber(); // Whatever read is ignored
        // The other player has joined
        Platform.runLater(() ->
        lblStatus.setText("Player 2 has joined"));
        }
       }
        catch (Exception ex) {
        ex.printStackTrace();}
    }).start();
        
        new Thread (() ->{
        try{
            Platform.runLater(() ->
            lobby.setText("Welcome to This Thing I Guess"));
            while(true) 
           {
            Platform.runLater(() ->
            lobby.setVisible(true));
            try {
            Thread.sleep(500); 
            } catch (InterruptedException ex) { }
            Platform.runLater(() ->
            lobby.setVisible(false));
            try {
            Thread.sleep(500); 
            } catch (InterruptedException ex) { }
           } 
        }  catch (Exception ex) {
        ex.printStackTrace();}  
        }).start();
        
    }    
    
}
