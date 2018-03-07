
package gameclient;

import game.GameConstants;
import gamephysics.Point;
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
                int player = gateway.getPlayerNumber();
                System.out.println("help"+player);
                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    Platform.runLater(() -> {
                        lblTitle.setText("PLAYER 1");
                        lblStatus.setText("Waiting for player 2 to join");
                    });
                    gateway.getPlayerNumber(); 
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
            } catch (Exception ex) {
              ex.printStackTrace();}
            
            try {
                Thread.sleep(10*1000); // Sleep for 10 seconds 
            } catch (InterruptedException ex) { }
            
            Platform.runLater(() -> {
              lobby.getScene().getWindow().hide();  
              Stage stage = new Stage();
              GamePane root = new GamePane();
              Simulation sim = new Simulation(300, 250, 2, 2);
              root.setShapes(sim.setUpShapes());
        
              Scene scene = new Scene(root, 300, 250);
              root.requestFocus(); 

               stage.setTitle("Game Physics");
               stage.setScene(scene);
               stage.setOnCloseRequest((event)->System.exit(0));
               stage.show();
               
                root.setOnKeyPressed(e -> {
                key = e.getCode();
                gateway.storeKey(key); 
               
              });
                
                ball = new Ball(0,0,5,5);
              new Thread(() -> {
                    try {
                        while(true)
                        {   
                        gateway.sendMove();
                        sim.setBallPos(gateway.getBall());
                        sim.setScoreP1(gateway.getScore1());
                        sim.setScoreP2(gateway.getScore2());
                        Point p1 = gateway.getPaddle1();
                        sim.setP1Pos(p1.x, p1.y);
                        Point p2 = gateway.getPaddle2();
                        sim.setP2Pos(p2.x, p2.y);
                        //sim.evolve(1.0);
                        Platform.runLater(()->sim.updateShapes());
                        try {
                            Thread.sleep(250);
                            } catch (InterruptedException ex) {} 
                            }
                    } catch (Exception ex) { 
                    ex.printStackTrace();
                    }
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
