package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;
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

            //sending request for user message
            output.write("USERWELCOMEMSG");
            output.newLine();
            output.flush();

            //reading user message

            if((line = incomingMSG.readLine()).equals("WELCOMEMSG")){
                while(!"ENDOFMSG".equals((line = incomingMSG.readLine()))){               
                    System.out.println(line);
                }
            }
            
            Scanner keyboardOBJ = new Scanner(System.in);
            int userResponse = 0;

            while(userResponse != 4){
                try{
                    userResponse = keyboardOBJ.nextInt();

                    switch(userResponse){
                        case 4:
                            System.out.println("Logging out. Please re-run program to re-authenticate");
                            break;
                    }
                } catch (InputMismatchException e){
                    System.out.println("Invalid number, please enter another number!");
                    keyboardOBJ.nextLine();
                }
            }

            System.out.println("selection loop end");
            

            //closing scanner
            keyboardOBJ.close();

        } catch (IOException e) {
            System.out.println("Error on client connection: " + e);
        } finally{
            try{

                //closing all the sockets and input/output stream
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
