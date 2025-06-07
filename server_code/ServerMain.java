import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

class InvalidPortNumber extends Exception{
    public InvalidPortNumber(String value){
        super(value);
    }
}

public class ServerMain{
    private String connectionID;

    public ServerMain(int portNumber)throws IOException{
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server running on port: " + portNumber);

        //this is used to accept incoming connections 
        while(true){
            UUID uuid = UUID.randomUUID();

            connectionID = uuid.toString();


            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            System.out.println("Client has connected via " + clientSocket.getLocalAddress());
            ServerThread tcpServerThread = new ServerThread(clientSocket, this);
            
            Thread thread = new Thread(tcpServerThread);

            thread.start();
        }
    }

    public String getConnectionID(){return  this.connectionID;}
    public static void main(String[] args) throws InvalidPortNumber{
        try{
            if(args.length > 0){
                new ServerMain(Integer.parseInt(args[0]));
            } else{
                throw new InvalidPortNumber("Invalid port number detected. Please enter a port number");
            }
        } catch(IOException | InvalidPortNumber e){
            System.out.println(e);
        }
    }
}