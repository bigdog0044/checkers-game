import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientConnection{
    private static final int portNumber = 9000;
    private static final String address = "localhost";
    public static void main(String[] args) {
        try {            
            Socket socket = new Socket(address, portNumber);
            BufferedWriter output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader incommingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            
            
            System.out.println("Server responded with: " + incommingMSG.readLine());
            output.write("Sending this message because I'm so cool");
            output.newLine();
            output.flush();
            
            socket.close();

            
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}