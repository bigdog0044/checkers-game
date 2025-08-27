package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.net.Socket;
import server_code.CreatingUsers;

public class ClientConnection {
    private static final String address = "127.0.0.1";
    private static final int port = 9000;
    private static final Scanner keyboardOBJ = new Scanner(System.in);
    private static String line;
    private static String clientUUID;
    private static Socket socket;
    private static BufferedReader incomingMSG;
    private static BufferedWriter outputMSG;
    public static void main(String[] args){
        try {


            socket = new Socket(address,port);

            //socket input and outputMSG streams
            incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputMSG = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));



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
                    outputMSG.write("AUTHCHECK");
                    outputMSG.newLine();
                    outputMSG.write(username);
                    outputMSG.newLine();
                    outputMSG.write(password);
                    outputMSG.newLine();
                    outputMSG.flush();

                    //reads in the response back from the server
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
            welcomeMSG();

            int userResponse = 0;
            while(userResponse != 4){
                try{
                    userResponse = keyboardOBJ.nextInt();
                    System.out.println(userResponse);
                    switch(userResponse){
                        case 1:
                            System.out.println("Start of case 1");
                            //sending off request to create a new user profile with client UUID
                            outputMSG.write("CREATEUSERREQ");
                            outputMSG.newLine();
                            outputMSG.write(clientUUID);
                            outputMSG.newLine();
                            outputMSG.flush();

                            //reading response back from server
                            line = incomingMSG.readLine();
                            if(line.equals("VALIDROLE")){
                                String username = "";
                                String password = "";
                                String role = "";

                                System.out.println("Username current value: " + username);


                                while(username.equals("")){
                                    System.out.print("Eneter username: ");
                                    username = keyboardOBJ.nextLine();

                                    if(username.equals("")){
                                        System.out.println("Please enter a valid username");
                                    }
                                }

                                while(password.equals("")){
                                    System.out.print("Eneter a password for " + username + ": ");
                                    password = keyboardOBJ.nextLine();

                                    if(password.equals("")){
                                        System.out.println("Please enter a valid password");
                                    }
                                }

                                while (role.equals("")){
                                    System.out.println("Select which role you would like " + username + " to have");
                                    System.out.println("[1] User");
                                    System.out.println("[2] Admin");
                                    System.out.println("[3] AI Bot");
                                    try{

                                        int choice = keyboardOBJ.nextInt();

                                        switch(choice){
                                            case 1:
                                                role = "user";
                                                break;
                                            case 2:
                                                role = "admin";
                                                break;
                                            case 3:
                                                role = "aibot";
                                                break;
                                            default:
                                                System.out.println("Please enter a valid number from the selection above");
                                        }

                                    } catch (InputMismatchException e){
                                        System.out.println("Enter a number");
                                    }
                                }

                                //sending off username,password,and role to the server to be verified and created
                                outputMSG.write(username);
                                outputMSG.newLine();
                                outputMSG.write(password);
                                outputMSG.newLine();
                                outputMSG.write(role);
                                outputMSG.newLine();
                                outputMSG.write("ENDOFMSG");
                                outputMSG.newLine();
                                outputMSG.flush();

                                //reading response back from the server
                                line = incomingMSG.readLine();

                                if(line.equals("VALIDPROFILE")){
                                    System.out.println("User " + username + " has been created with password: " + password + " and role: " + role);
                                    break; //fixes an issue with looping and connection hanging
                                } else if (line.equals("ERROR")){
                                    line = incomingMSG.readLine();
                                    if("INVALIDPROFILE".equals(line)){
                                        while(!"ENDOFMSG".equals((line = incomingMSG.readLine()))){
                                            //line = incomingMSG.readLine();
                                            System.out.println(line);
                                        }

                                        break; //fixes an issue with looping and connection hanging

                                    } else{
                                        System.out.println("Another error has occured");
                                        while(!"ENDOFMSG".equals((line = incomingMSG.readLine()))){
                                            System.out.println(line);
                                        }

                                        break; //fixes an issue with looping and connection hanging
                                    }
                                }


                            }

                            if (line.equals("INVALIDROLE")){
                                line = incomingMSG.readLine();
                                System.out.println(line);
                            }

                            keyboardOBJ.next();
                            break;

                        case 3:
                            //sending off request to server to initiate session creation
                            outputMSG.write("CREATINGSESSION");
                            outputMSG.newLine();
                            outputMSG.flush();

                            //reading response back from server
                            line = incomingMSG.readLine();

                            if(line.equals("RESPONSEREC")){
                                while(!line.equals("ENDOFMSG")){
                                    System.out.println(line);
                                    line = incomingMSG.readLine();
                                }
                            } else {
                                System.out.println("Another header came through instead: " + line);
                            }



                            //this handles bot request for users game
                            line = incomingMSG.readLine();
                            if(line.equals("BOTREC")){
                                Scanner keyboardInputOBJ = new Scanner(System.in);
                                
                                String response = "";
                                line = incomingMSG.readLine();

                                while(!line.equals("VALIDRESPONSE")){
                                    //reads outputMSG from the server
                                    if(line.equals("USERRESPONSEREQ")){
                                        while(!line.equals("ENDOFMSG")){
                                            System.out.println(line);
                                            line = incomingMSG.readLine();
                                        }
                                        //allowing user to respond
                                        response = keyboardInputOBJ.nextLine();
                                        outputMSG.write("USERRESPONSE");
                                        outputMSG.newLine();
                                        outputMSG.write(response);
                                        outputMSG.newLine();
                                        outputMSG.flush();
                                    }

                                    //reading response back from server
                                    line = incomingMSG.readLine();
                                    if(line.equals("INVALIDRESPONSE")){
                                        while(!line.equals("ENDOFMSG")){
                                            line = incomingMSG.readLine();
                                            System.out.println(line);
                                            
                                        }
                                    }


                                    
                                }


                                //reading the valid header response back from the server
                                if(line.equals("VALIDRESPONSE")){
                                    while(!line.equals("ENDOFMSG")){
                                        System.out.println(line);
                                        line = incomingMSG.readLine();
                                    }
                                }
                            }


                            //this handles session creation
                            line = incomingMSG.readLine();
                            if(line.equals("SESSIONCREATING")){
                              while(!line.equals("ENDSESSCREATE")){

                                if(!line.equals("ENDOFMSG") || !line.equals("ENDSESSCREATE")){
                                    System.out.println(line);
                                }
                                line = incomingMSG.readLine();
                                
                              }
                            }

                            //this part of the code reads the response from the server letting the client know that the game has started

                            line = incomingMSG.readLine();
                            if(line.equals("STARTGAME")){
                                while(!line.equals("ENDGAME")){
                                    line = incomingMSG.readLine();
                                    
                                    //reading board from server
                                    if(line.equals("STARTBOARD")){
                                        while(!line.equals("ENDBOARD")){
                                            System.out.println(line);
                                            line = incomingMSG.readLine();
                                        }
                                    }

                                    //allowing user to send location for their board
                                    line = incomingMSG.readLine();
                                    System.out.println("Current header: " + line);
                                    Scanner playerMoveOBJ = new Scanner(System.in);
                                    while(!line.equals("VALIDRESPONSE")){
                                        if(line.equals("USERRESPONSEREQ")){

                                            line = incomingMSG.readLine();

                                            while(!line.equals("ENDOFMSG")){
                                                System.out.println(line);
                                                line = incomingMSG.readLine();
                                            }
                                            String response = playerMoveOBJ.nextLine();
                                            
                                            /*do not change this as this works...dunno how yet */
                                            outputMSG.write("USERRESPONSE");
                                            outputMSG.newLine();
                                            outputMSG.write(response);
                                            outputMSG.newLine();
                                            outputMSG.flush();

                                            line = incomingMSG.readLine();
                                            System.out.println("next header: " + line);
                                        }

                                        //reads out the error message
                                        if(line.equals("ERROR")){
                                            while(!line.equals("ENDOFMSG")){
                                                System.out.println(line);
                                                line = incomingMSG.readLine();
                                            }

                                            line = "USERRESPONSEREQ";
                                        }
                                    }

                                }
                            }

                            //reading response on rendering board from server
                            line = incomingMSG.readLine();

                            break;
                        case 4:
                            System.out.println("Logging out. Please re-run program to re-authenticate");
                            break;
                        default:
                            System.out.println(userResponse);
                    }


                    welcomeMSG();
                    keyboardOBJ.nextLine();
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

                //closing all the sockets and input/outputMSG stream
                System.out.println("closing everything on client side");
                outputMSG.write("CLOSE");
                outputMSG.newLine();
                outputMSG.flush();

                try { if (incomingMSG != null) incomingMSG.close(); } catch (IOException ignored) {}
                try{ outputMSG.close();} catch(IOException ignored){}
                try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
            } catch (IOException e){
                System.out.println("Error on finally statement in client connection: " + e);
            }


        }

    }

    private static void welcomeMSG(){
        try{
            outputMSG.write("USERWELCOMEMSG");
            outputMSG.newLine();
            outputMSG.flush();

            line = incomingMSG.readLine();
            if(line.equals("WELCOMEMSG")){
                while(!"ENDOFMSG".equals((line = incomingMSG.readLine()))){
                    System.out.println(line);
                }
            } else {
                System.out.println("Something went horribly wrong on welcomeMSG: " + line);
            }
        } catch (IOException e){
            System.out.println("Welcome message method error: " + e);
        }

    }

    private void sendingMSG(String header){
		try{
			outputMSG.write(header);
			outputMSG.newLine();
			outputMSG.flush();
		} catch(IOException error){
			System.out.println("Error on sending msg in gamehandler: " + error);
		}
	}
	
	private void sendingMSG(String message, String startHeader, String endHeader){
		try{
			outputMSG.write(startHeader);
			outputMSG.newLine();
			outputMSG.write(message);
			outputMSG.newLine();
			outputMSG.write(endHeader);
			outputMSG.newLine();
			outputMSG.flush();
		} catch(IOException error){
			System.out.println("Error on sending msg in gamehandler: " + error);
		}
	}

}
