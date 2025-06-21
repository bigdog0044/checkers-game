package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientConnection {
    private static final String address = "127.0.0.1";
    private static final int port = 9000;
    private static final Scanner keyboardOBJ = new Scanner(System.in);
    private static String line;
    private static String clientUUID;
    private static Socket socket;
    private static BufferedReader incomingMSG;
    private static BufferedWriter output;
    public static void main(String[] args){
        try {

            Socket socket = new Socket(address,port);
            
            //socket input and output streams
            incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            


            line = incomingMSG.readLine();
            boolean validAuth = false;

            //loging portion
            if (line.equals("AUTHREQ")){
                
                while(!(validAuth)){
                    String username = "";
                    String password = "";

                    //does a infinite loop to constantly ask user for username and password
                    while (username.equals("") || password.equals("")){
                        System.out.print("Enter username: ");
                        username = keyboardOBJ.nextLine();
                        System.out.print("Enter password: ");
                        password = keyboardOBJ.nextLine();
                    }

                    //sends out a request to the server to validate the username and password
                    output.write("AUTHCHECK");
                    output.newLine();
                    output.write(username);
                    output.newLine();
                    output.write(password);
                    output.newLine();
                    output.flush();

                    line = incomingMSG.readLine();

                    if( "AUTHSUCCESS".equals(line)){
                        clientUUID = incomingMSG.readLine();
                        validAuth = true;
                    } else{
                        System.out.println("No valid username and password found");
                    }
                    
                }
            }

            output.write("USERWELCOMEMSG");
            output.newLine();
            output.flush();
            System.out.println("Finished sending off request for USERWELCOMEMSG");
            while((line = incomingMSG.readLine()) != null){               
                System.out.println(line);
            }

            System.out.println("while loop has finished");

        } catch (IOException e) {
            System.out.println("Error on client connection: " + e);
        } finally{
            try{
                System.out.println(output);
                System.out.println("closing everything on client side");
                output.write("CLOSE");
                output.newLine();
                output.flush();

                try { if (incomingMSG != null) incomingMSG.close(); } catch (IOException ignored) {}
                try{ output.close();} catch(IOException ignored){}
                try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
            } catch (IOException e){
                System.out.println("Error on finally statement in client connection: " + e);
            }

            
        }

    }
}
