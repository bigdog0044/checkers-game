package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ServerThread implements  Runnable{
    private Socket socket; //socket that the client uses to connect
    private ServerMain serverMain; //reference to the main server code
    private Connection connection;
    private String line;
    private BufferedReader incomingMSG;
    private BufferedWriter output;
    private boolean authenticated = false;

    public ServerThread(Socket socket, ServerMain main){
        this.socket = socket;
        this.serverMain = main;
        DBUsernameAndPass dbinfo = new DBUsernameAndPass();

        try{
             Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/checkersgamedb", dbinfo.getUsername(), dbinfo.getPassword());
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
                    System.out.println(line);
                    switch (line) {
    
                        case "AUTHCHECK":
                            String username = incomingMSG.readLine();
                            String password = incomingMSG.readLine();
                                
                            String userUUID = validAuth(username, password);
                            if (userUUID != null){
                                output.write("AUTHSUCCESS");
                                output.newLine();
                                output.write(userUUID);
                                output.newLine();
                                output.flush();
                                String sqlStatement = "UPDATE userinfo SET `lastIpAdrrConn` = ?, `lastDateConn` = ?, `lastTimeOfConn` = ? WHERE username = ?;";
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
                            output.flush();
                            break;
                        case "CLOSE":
                            System.out.println("Closing everything on server thread side");
                            try { if (incomingMSG != null) incomingMSG.close(); } catch (IOException ignored) {}
                            try { if (output != null) output.close(); } catch (IOException ignored) {}
                            try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
                            try { if (connection != null && !connection.isClosed()) connection.close(); } catch (SQLException ignored) {}
                            break;
                        default:
                            System.out.println("Incoming header requests: " + line);                        
                    }
                }
            } catch (IOException e) {
                System.out.println("Error on server thread: " + e);                
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
}