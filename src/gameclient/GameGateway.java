
package gameclient;

import gamephysics.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.input.KeyCode;


public class GameGateway implements game.GameConstants {
    
    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private KeyCode keyPressed = KeyCode.SPACE;
    
    public GameGateway() {
        try {
        Socket socket = new Socket("localhost", 8000);
        outputToServer = new PrintWriter(socket.getOutputStream());
        inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
        ex.printStackTrace();
        }
    }
    
    public int getPlayerNumber() throws IOException
    {
        return Integer.parseInt(inputFromServer.readLine());
    }   
    
    public int getScore1() throws IOException
    {   
        int score1 = Integer.parseInt(inputFromServer.readLine());
        return score1;
    }        
    
    public int getScore2() throws IOException
    {
        int score2 = Integer.parseInt(inputFromServer.readLine());
        return score2;
    }   
    
    public Point getPaddle1() throws IOException
    {
        String[] coordinates = inputFromServer.readLine().split(", ");
        Point paddle1 = new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
        return paddle1;
    }
    
    public Point getPaddle2() throws IOException
    {
        String[] coordinates = inputFromServer.readLine().split(", ");
        Point paddle2 = new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
        return paddle2;
    } 
    
    public Point getBall() throws IOException
    {
        String iRead = inputFromServer.readLine();
        String[] coordinates = iRead.split(", ");
        Point ball = new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
        return ball;   
    }  
    
    public void storeKey(KeyCode key)
    {
        keyPressed = key;
    }        
    
    public void sendMove()
    {
        switch (keyPressed) {
                    case DOWN:
                        outputToServer.println("D");
                        outputToServer.flush();
                        break;
                    case UP:
                        outputToServer.println("U");
                        outputToServer.flush();
                        break;
                    case LEFT:
                        outputToServer.println("L");
                        outputToServer.flush();
                        break;
                    case RIGHT:
                        outputToServer.println("R");
                        outputToServer.flush();
                        break;   
                    default:
                        outputToServer.println("N");
                        outputToServer.flush();
                        break;
                    
        }
        keyPressed = KeyCode.SPACE;
    }
    
}
