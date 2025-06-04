import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

class InvalidPortNumber extends Exception{
    public InvalidPortNumber(String value){
        super(value);
    }
}

public class ServerMain{
    public static void server(int portNumber)throws IOException{
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server running on port: " + portNumber);

            //this is used to accept incoming connections 
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");
        System.out.println("Client has connected via " + clientSocket.getLocalAddress());

        BufferedWriter output = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        output.write("Server listening.....awaiting communication");
        output.newLine();
        output.flush();

        System.out.println("Client has sent: " + incomingMSG.readLine());

        clientSocket.close();
        serverSocket.close(); 

    }
    public static void main(String[] args) throws InvalidPortNumber{
        try{
            if(args.length > 0){
                server(Integer.parseInt(args[0]));
            } else{
                throw new InvalidPortNumber("Invalid port number detected. Please enter a port number");
            }
        } catch(IOException | InvalidPortNumber e){
            System.out.println(e);
        }
    }
}