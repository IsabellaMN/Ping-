
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
        // Create a socket to connect to the server
        Socket socket = new Socket("localhost", 8000);

        // Create an input stream to receive data from the server
        outputToServer = new PrintWriter(socket.getOutputStream());

        // Create an input stream to read data from the server
        inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        }
        catch (Exception ex) {
        ex.printStackTrace();
        }
        
    }
    public int getPlayerNumber() throws IOException
    {
        return Integer.parseInt(inputFromServer.readLine());
    }        
    
}
