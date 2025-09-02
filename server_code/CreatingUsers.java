	

	package server_code;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class CreatingUsersError extends Exception{
    public CreatingUsersError(String msg){
        super(msg);
    }
}

public class CreatingUsers{
	private static Connection connection;
	private static BufferedWriter output;

	public CreatingUsers(){
		try{
			DBUsernameAndPass dbinfo = new DBUsernameAndPass();
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	        "jdbc:mysql://localhost:3306/checkergamedb", dbinfo.getUsername(), dbinfo.getPassword());
		} catch (ClassNotFoundException | SQLException e){
			System.out.println("Error on CreatingUsers: " + e);
		}
	}

	/*
	* this method is used to create and insert users into the database
	* @returns true if user has been added to the database 
	*/
	public boolean createUser(String username, String password, String role ) throws CreatingUsersError{
		try{

            if(checkUsername(username)){
            	UUID userUUID = UUID.randomUUID();
            	String sqlStatement = "INSERT INTO userinfo(`ID`,`username`,`password`,`gameSessionID`,`lastIpAdrrConn`,`lastDateConn`,`lastTimeOfConn`,`searchingForGame`,`isInGame`,`privilegeType`) VALUES (?,?,?,?,?,?,?,?,?,?);";
            	PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
        		
        		preparedSQL.setString(1,userUUID.toString());
        		preparedSQL.setString(2,username);
        		preparedSQL.setString(3,password);
        		preparedSQL.setNull(4,java.sql.Types.VARCHAR);
        		preparedSQL.setNull(5,java.sql.Types.VARCHAR);
        		preparedSQL.setNull(6,java.sql.Types.DATE);
        		preparedSQL.setNull(7,java.sql.Types.TIME);
        		preparedSQL.setBoolean(8,false);
        		preparedSQL.setBoolean(9,false);
        		preparedSQL.setString(10,role);

        		preparedSQL.executeUpdate();

        		return true; //user created successfully
            }

            //throws an error if the same username is found in the database
            throw new CreatingUsersError("Invalid username: Username exists in database. Please create a new user without using the same usernames");
		} catch (SQLException e){
			System.out.println("Error on CreatingUsers createUser method: " + e);
		} finally{
			try{
				if(output != null){
					output.close();
				}	
			} catch (IOException e){
				/*does nothing*/
			}
		}

		return false;
	}

	/*
	* checks to see if username exists in the database
	* @returns true if no username exists in database and false if it does exist
	*/
	private boolean checkUsername(String username){
		try{
			String sqlStatement = "SELECT * FROM `userinfo` WHERE username = ?;";

	        PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
	        preparedSQL.setString(1,username);
	        ResultSet resultSet = preparedSQL.executeQuery();

	        if(!resultSet.next()){
	                return true; //username is valid
	        }
		} catch (SQLException e){
			System.out.println("Error on checkUsername method: " + e);
		}
        

        return false;
    }
}