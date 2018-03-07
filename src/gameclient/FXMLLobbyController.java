
package gameclient;

import game.GameConstants;
import gamesim.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


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
        gateway = new GameGateway();
        
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = gateway.getPlayerNumber();
                System.out.println("help"+player);
                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    //myName = "PLAYER 1";
                    //otherName = "PLAYER 2";
                    Platform.runLater(() -> {
                        lblTitle.setText("PLAYER 1");
                        lblStatus.setText("Waiting for player 2 to join");
                    });
                    // Receive startup notification from the server
                    gateway.getPlayerNumber(); // Whatever read is ignored WHAT????????
                    // The other player has joined
                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. Game will start in 10 seconds"));
                }
                else if (player == PLAYER2) {

                    Platform.runLater(() -> {
                    lblTitle.setText("PLAYER 2");
                    lblStatus.setText("Game will start in 10 seconds");
                     });
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            
            
            try {
                Thread.sleep(10*1000); // Sleep for 5 seconds 
            } catch (InterruptedException ex) { }
            
            Platform.runLater(() -> {
              lobby.getScene().getWindow().hide();  
              Stage stage = new Stage();
              GamePane root = new GamePane();
              Simulation sim = new Simulation(300, 250, 2, 2);
              root.setShapes(sim.setUpShapes());
        
              Scene scene = new Scene(root, 300, 250);
              root.setOnKeyPressed(e -> {
                switch (e.getCode()) {
                    case DOWN:
                        sim.moveInner(0, 3);
                        break;
                    case UP:
                        sim.moveInner(0, -3);
                        break;
                    case LEFT:
                        sim.moveInner(-3, 0);
                        break;
                    case RIGHT:
                        sim.moveInner(3, 0);
                        break;
                    case S:
                        sim.movep2(0,3);
                        break;
                    case W:
                        sim.movep2(0,-3);
                        break;
                    case A:
                        sim.movep2(-3,0);
                        break;
                    case D:
                        sim.movep2(3,0);
                        break;
                    }
                });

                root.requestFocus(); 

                stage.setTitle("Game Physics");
                stage.setScene(scene);
                stage.setOnCloseRequest((event)->System.exit(0));
                stage.show();
            });
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
