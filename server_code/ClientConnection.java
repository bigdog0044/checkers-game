package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientConnection {
    private static final String address = "127.0.0.1";
    private static final int port = 9000;
    public static void main(String[] args){
        try {
            Socket socket = new Socket(address,port);
            
            //socket input and output streams
            BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // //outputting what the server initially sends
            //System.out.println(incomingMSG.readLine());
            // System.out.println(incomingMSG.readLine());
            

            socket.close();

        } catch (IOException e) {
            System.out.println("Error on client connection: " + e.getStackTrace());
        }


    }
}
