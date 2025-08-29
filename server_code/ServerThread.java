package server_code;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import game_code.CheckerBoard;
import game_code.GameHandler;

public class ServerThread implements  Runnable{
    private Socket socket; //socket that the client uses to connect
    private ServerMain serverMain; //reference to the main server code
    private Connection connection;
    private String line;
    private BufferedReader incomingMSG;
    private BufferedWriter output;
    private boolean authenticated = false;
    private String userUUID;
    private String DBName;

    public ServerThread(Socket socket, ServerMain main){
        this.socket = socket;
        this.serverMain = main;
        this.DBName = "checkergamedb";
        DBUsernameAndPass dbinfo = new DBUsernameAndPass();

        try{
             Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + this.DBName, dbinfo.getUsername(), dbinfo.getPassword());
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("ServerThread class initialisation method error: " + e);
        }
    }


    @Override
    public void run(){
            try {
                output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String connectionID = serverMain.getConnectionID();

                output.write("AUTHREQ");
                output.newLine();
                output.flush();

                while(!"CLOSE".equals(line)){
                    //reads in another header request
                    line = incomingMSG.readLine();
                    System.out.println("Incoming headers and request for server: " + line);
                    switch (line) {

                        case "AUTHCHECK":
                            String username = incomingMSG.readLine();
                            String password = incomingMSG.readLine();

                            userUUID = validAuth(username, password);
                            if (userUUID != null){
                                output.write("AUTHSUCCESS");
                                output.newLine();
                                output.write(userUUID);
                                output.newLine();
                                output.flush();

                                //used to update last ip address, last time, and and date of connection columns in table
                                updateUserRecord(username);

                            } else{
                                output.write("AUTHNOTSUCCESS");
                                output.newLine();
                                output.flush();
                            }
                            break;
                        case "USERWELCOMEMSG":
                            output.write("WELCOMEMSG");
                            output.newLine();
                            output.write("Welcome to the checkers game!");
                            output.newLine();
                            output.write("Please choose from the following options");
                            output.newLine();
                            output.write("[1] Create new users - Note: only admin accounts can do this");
                            output.newLine();
                            output.write("[2] find game sessions");
                            output.newLine();
                            output.write("[3] create a new session");
                            output.newLine();
                            output.write("[4] logout");
                            output.newLine();
                            output.write("[5] view your current game saves");
                            output.newLine();
                            output.write("ENDOFMSG");
                            output.newLine();
                            output.flush();
                            break;
                        case "CLOSE":
                        //this handles closing all connections and resetting everything on client side
                            try{
                                System.out.println("Closing everything on server thread side");

                                String sqlStatement = "UPDATE userinfo SET `searchingForGame` = ?, `isInGame` = ?, `waitingForPlayer` = ? WHERE `id` = ?;";
                                PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);

                                preparedSQL.setBoolean(1,false);
                                preparedSQL.setBoolean(2,false);
                                preparedSQL.setBoolean(3,false);
                                preparedSQL.setString(4, userUUID);

                                preparedSQL.executeUpdate();
                            } catch (SQLException e){
                                System.out.println("Error on resetting search for game and is in game columns: " + e);
                            }

                            try { if (incomingMSG != null) incomingMSG.close(); } catch (IOException ignored) {}
                            try { if (output != null) output.close(); } catch (IOException ignored) {}
                            try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
                            try { if (connection != null && !connection.isClosed()) connection.close(); } catch (SQLException ignored) {}
                            break;
                        case "CREATEUSERREQ":
                            line = incomingMSG.readLine();



                            boolean result = validDateUserRole(line);
                            CreatingUsers createUserOBJ = new CreatingUsers();
                            if(result){
                                output.write("VALIDROLE");
                                output.newLine();
                                output.flush();

                                String userName = incomingMSG.readLine();
                                String passWord = incomingMSG.readLine();
                                String role = incomingMSG.readLine();

                                try{
                                    if(createUserOBJ.createUser(userName,passWord,role)){

                                        output.write("VALIDPROFILE");
                                        output.newLine();
                                        output.flush();
                                    }
                                } catch (CreatingUsersError e){
                                    //outputs the error message
                                    output.write("ERROR");
                                    output.newLine();
                                    output.write("INVALIDPROFILE");
                                    output.newLine();
                                    output.write(e.getMessage());
                                    output.newLine();
                                    output.write("ENDOFMSG");
                                    output.newLine();
                                    output.flush();
                                }

                            } else{
                                output.write("INVALIDROLE");
                                output.newLine();
                                output.write("Invalid permissions for this user");
                                output.newLine();
                                output.flush();
                            }


                            break;
                        case "CREATINGSESSION":
                            output.write("RESPONSEREC");
                            output.newLine();
                            output.write("Creating session");
                            output.newLine();
                            output.write("ENDOFMSG");
                            output.newLine();
                            output.flush();

                            boolean successfulResponse = false;
                            boolean requiresBot = false;

                            output.write("BOTREC");
                            output.newLine();
                            output.flush();
                            while (!successfulResponse){

                                //used to ask user about if they want a bot in their game
                                output.write("USERRESPONSEREQ");
                                output.newLine();
                                output.write("Do you want a bot in your game? (Yes/No)");
                                output.newLine();
                                output.write("ENDOFMSG");
                                output.newLine();
                                output.flush();


                                line = incomingMSG.readLine();

                                if (line.equals("USERRESPONSE")){
                                    line = incomingMSG.readLine();
                                    if(line.equals("Yes") || line.equals("yes")){
                                        output.write("VALIDRESPONSE");
                                        output.newLine();
                                        output.write("Adding bot to your session");
                                        output.newLine();
                                        output.write("ENDOFMSG");
                                        output.newLine();
                                        output.flush();

                                        requiresBot = true;
                                        successfulResponse = true;

                                    } else if(line.equals("No") || line.equals("no")){
                                        output.write("VALIDRESPONSE");
                                        output.newLine();
                                        output.write("Not adding bot to your session");
                                        output.newLine();
                                        output.write("Looking for other players to join");
                                        output.newLine();
                                        output.write("ENDOFMSG");
                                        output.newLine();
                                        output.flush();

                                        successfulResponse = true;

                                    } else{
                                        output.write("INVALIDRESPONSE");
                                        output.newLine();
                                        output.write("Invalid response");
                                        output.newLine();
                                        output.write("ENDOFMSG");
                                        output.newLine();
                                        output.flush();
                                    }
                                }
                                
                            }

                            /*this part of the code is used to start the actual game part of this project */
                            //creating new checker board for player 
                            CheckerBoard board = new CheckerBoard(8,8);

                            //used to setup a new session for the user
                            SettingGamesUp setupGameOBJ = new SettingGamesUp(userUUID, socket, userUUID, board, requiresBot);

                            //used to update user record for gameSessionID column
                            updateGameSessionID(setupGameOBJ.getCurrentFolderUUID());
                            //used to update user record for waiting for a player to join
                            if(!requiresBot){
                                updateWaitingForPlayer();
                            }
                            //updates player is in game status
                            playerInGame();

                            GameHandler gameHandlerOBJ = new GameHandler(board,setupGameOBJ.getSessionFolderLocation(),socket);
                            gameHandlerOBJ.startGame(); //starts game
                            
                            break;


                        default:
                            System.out.println("Other incoming header requests: " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error on server thread: " + e);
            }

        }

        //add java docs for these methods


        private void updateUserRecord(String username){
            //getting date and time
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();

            InetAddress clientIP = socket.getLocalAddress();
            String ip = clientIP.toString();
            String clientDate = date.toString();
            String clientTime = time.toString();

            try{
                String sqlStatement = "UPDATE userinfo SET `lastIpAdrrConn` = ?, `lastDateConn` = ?, `lastTimeOfConn` = ? WHERE username = ?;";
                PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);

                preparedSQL.setString(1,clientIP.toString());
                preparedSQL.setString(2,clientDate);
                preparedSQL.setString(3,clientTime);
                preparedSQL.setString(4,username);


                preparedSQL.executeUpdate();

            } catch (SQLException e){
                System.out.println("Error on updating user record: " + e);
            }
        }

        /*
         * this method is used to update the user record with the current game session ID
         */

        private void updateGameSessionID(String folderUUID){
            try{
                String sqlStatement = "UPDATE userinfo SET `gameSessionID` = ? WHERE `ID`= ?; ";
                PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
                preparedSQL.setString(1, folderUUID);
                preparedSQL.setString(2, userUUID);
                preparedSQL.executeUpdate();
            } catch (SQLException error){
                System.out.println("Error on updateGameSessionID method: " + error);
            }
        }

        /*
         * this method is used to update the user record for if the player is waiting for another player to join
         */

        private void updateWaitingForPlayer(){
            try{
                String sqlStatement = "UPDATE userinfo SET `waitingForPlayer` = ? WHERE `ID`= ?; ";
                PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
                preparedSQL.setBoolean(1, true);
                preparedSQL.setString(2, userUUID);
                preparedSQL.executeUpdate();
            } catch (SQLException error){
                System.out.println("Error on updateWaitingForPlayer: " + error);
            }
        }


        /*
         * used to update isInGame column
         */

         private void playerInGame(){
             try{
                 String sqlStatement = "UPDATE userinfo SET `isInGame` = ? WHERE `ID` = ?; ";
                 PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
                 preparedSQL.setBoolean(1, true);
                 preparedSQL.setString(2, userUUID);
                 preparedSQL.executeUpdate();
            } catch (SQLException error){
                System.out.println("Error on updating playerInGame method: " + error);
            }
                

         }
        

        private String validAuth(String username, String password){
        try{

            String sqlStatement = "select * from userinfo where username = ? and password = ?;";
            //this is used below as not only is it the better as it has massive security improvement and also enables portability
            PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
            preparedSQL.setString(1,username);
            preparedSQL.setString(2,password);


            ResultSet resultSet = preparedSQL.executeQuery();

            while(resultSet.next()){
                if(resultSet.getString("username").equals(username) && resultSet.getString("password").equals(password)){
                    return resultSet.getString("ID");
                }
            }

        } catch (SQLException e){
            System.out.println("Valid auth error: " + e);
        }

        return null;
    }


    private boolean validDateUserRole(String userUUID){
        String sqlStatement = "SELECT * FROM `userinfo` WHERE ID = ? AND privilegeType='admin'; ";
        try{
            PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
            preparedSQL.setString(1,userUUID);
            ResultSet resultSet = preparedSQL.executeQuery();

            if(resultSet.next()){
                return true; //user has valid role
            }

        } catch (SQLException e){
            System.out.println("Error on creating users validation: " + e);
        }

        return false; //user does not have valid role

    }


}
