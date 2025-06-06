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
            BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            while(incomingMSG.readLine() != null){
                System.out.println(incomingMSG.readLine());
            }

            socket.close();

        } catch (IOException e) {
        }


    }
}
