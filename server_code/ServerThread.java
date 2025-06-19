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
    
    
    public ServerThread(Socket socket, ServerMain main){
        this.socket = socket;
        this.serverMain = main;
    }
    
    
    @Override
    public void run(){
            try {
                String connectionID = serverMain.getConnectionID();
                BufferedWriter output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader incomingMSG = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                output.write("AUTHREQ");
                output.newLine();
                output.flush();
                
                
                switch (incomingMSG.readLine()) {
                    case "AUTHCHECK":
                    String username = incomingMSG.readLine();
                    String password = incomingMSG.readLine();
                    
                    String userUUID = validAuth(username, password);

                    if (userUUID != null){
                        output.write("AUTHSUCCESS");
                        output.newLine();
                        output.write(userUUID);
                        output.flush();
                    } else{
                        output.write("AUTHNOTSUCCESS");
                        output.flush();
                    }
                    break;
                    
                    default:
                    break;
                }
            } catch (IOException e) {
                System.out.println("Error on server thread: " + e);
            } finally{
                if(socket != null){
                    try {
                        
                        socket.close();
                        
                    } catch (IOException e) {
                        //does nothing
                    }
                }
            }
            
        }
        
        public String validAuth(String username, String password){
            try{
                DBUsernameAndPass dbinfo = new DBUsernameAndPass();
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/checkersgamedb", dbinfo.getUsername(), dbinfo.getPassword());
                
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

            } catch (ClassNotFoundException | SQLException e){
            System.out.println("Valid auth error: " + e);
            }
        return null;
    }
}
