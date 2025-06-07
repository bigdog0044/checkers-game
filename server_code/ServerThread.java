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
                SQLServerLogin sqlServer = new SQLServerLogin();
                System.out.println(sqlServer.dbLogin());

                // output.newLine();
                // output.write("Your connection number is " +  connectionID);
                // output.newLine();
                // //used to let the client know its time to authenticate this connection
                // output.write("AUTHREQUIRED");
                // output.newLine();
                // output.flush();
                // System.out.println("Client has sent: " + incomingMSG.readLine());
                
            } catch (IOException e) {
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
