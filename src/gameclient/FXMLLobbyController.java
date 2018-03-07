
package gameclient;

import game.GameConstants;
import gamesim.Ball;
import gamesim.Simulation;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class FXMLLobbyController implements Initializable, GameConstants  {

    private GameGateway gateway;
    private Simulation sim;
    private Ball ball;
    private String myName = "";
    private String otherName = "";
    private KeyCode key = null; 
    
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
                key = e.getCode();
                gateway.storeKey(key);
                //sim.setP1Pos(PLAYER1, PLAYER1);
              });
              
                root.requestFocus(); 

                stage.setTitle("Game Physics");
                stage.setScene(scene);
                stage.setOnCloseRequest((event)->System.exit(0));
                stage.show();
                
//                new Thread(() -> {
//                while (true) {
//                sim.evolve(1.0);
//                Platform.runLater(()->sim.updateShapes());
//                try {
//                    Thread.sleep(25);
//                } catch (InterruptedException ex) {}
//                }
//                }).start(); 
                
                new Thread(() -> {
                    try {
                        while(true)
                        {    
                        gateway.sendMove();
                        sim.setScoreP2(gateway.getScore2());
                        sim.setScoreP1(gateway.getScore1());
                        sim.setP1Pos(gateway.getPaddle1().x, gateway.getPaddle1().y);
                        sim.setP2Pos(gateway.getPaddle2().x, gateway.getPaddle2().y);
                        ball.setPos(gateway.getBall());
                        }
                    } catch (Exception ex) { }
                 }).start();    
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
