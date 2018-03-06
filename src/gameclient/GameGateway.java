
package gameclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class GameGateway implements game.GameConstants {
    
    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    
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
    
    
}
