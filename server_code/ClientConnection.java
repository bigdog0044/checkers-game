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
    public static void main(String[] args){
        try {
            Socket socket = new Socket(address,port);
            
            //socket input and output streams
            BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


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

                    if(line.equals("AUTHSUCCESS")){
                        validAuth = true;
                        while ((line = incomingMSG.readLine())!= null){
                            clientUUID = line;
                            System.out.println("Successful login");
                        }
                    } else if(line.equals("AUTHNOTSUCCESS")){
                        System.out.println("No valid username and password found");
                    }
                    
                }
            }
            System.out.println(clientUUID);
            // while (line != null && !line.equals("CLOSE")){
            //     System.out.println("test");
            //     String username = "";
            //     String password = "";
            //     while (username.equals("") || password.equals("")){
            //         System.out.print("Enter username: ");
            //         username = keyboardOBJ.nextLine();
            //         System.out.print("Enter password: ");
            //         password = keyboardOBJ.nextLine();
            //     }
                
            //     //sending authentication request to the server with username and password
            //     output.write("AUTHREQ");
            //     output.newLine();
            //     output.write(username);
            //     output.newLine();
            //     output.write(password);
            //     output.newLine();
            //     output.flush();
            // }

            //closing connections
            output.close();
            incomingMSG.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Error on client connection: " + e.getStackTrace());
        }


    }
}
