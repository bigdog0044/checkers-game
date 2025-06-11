package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread implements  Runnable{
    private Socket socket; //socket that the client uses to connect
    private ServerMain serverMain; //reference to the main server code
    
    
    public ServerThread(Socket socket, ServerMain main){
        this.socket = socket;
        this.serverMain = main;
    }
    
    
    @Override
    public void run(){
            try {
                String connectionID = serverMain.getConnectionID();
                BufferedWriter output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Class.forName("com.mysql.cj.jdbc.Driver");
                
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e);
            } finally{
                if(socket != null){
                    try {
                        
                        socket.close();
                        
                    } catch (IOException e) {
                        //does nothing
                    }
                }
            }

        }
}
