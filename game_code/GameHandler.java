package game_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server_code.DBUsernameAndPass;

public class GameHandler{
	private CheckerBoard board;
	private String playerFolder = "";
	private Socket socket;
	private BufferedReader incomingMSG;
	private BufferedWriter outputMSG;
	private CheckerGameLogic CheckerGameLogic = new CheckerGameLogic();
	private Connection connection;
	private String userUUID;

	
	public GameHandler(CheckerBoard board,Socket socket, String playerID){
		this.board = board;
		this.playerFolder = playerFolder;
		this.socket = socket;
		this.userUUID = playerID;

		//conencting to the database
		try{
			DBUsernameAndPass dbinfo = new DBUsernameAndPass();
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	        "jdbc:mysql://localhost:3306/checkergamedb", dbinfo.getUsername(), dbinfo.getPassword());
		} catch (ClassNotFoundException | SQLException e){
			System.out.println("Error on GameHandler: " + e);
		}
		
	}
	
	/*
	* used to start the gaming session
	*/
	
	/*
	 * used to check if the game is over
	 */

	 private boolean isGameOver(){
		 try{
	
			 String sqlQuery = "SELECT * FROM `gameinfo` WHERE owner_game_ID=? AND gameOver=?;";
			 PreparedStatement preparedSql = connection.prepareStatement(sqlQuery);
			 preparedSql.setString(1, userUUID);
			 preparedSql.setBoolean(2, true);
			 ResultSet result = preparedSql.executeQuery();

			 System.out.println(result.isBeforeFirst());

			 /*
			  * this is the following of what this outputs
			  * false: no data has been found
			  * true: data has been found
			  */
			 return result.isBeforeFirst();
		 } catch (SQLException error){
			 System.out.println("Error on isGameOver: " + error);
		 }

		 return false;
	 }
	/*
	* used to save player data
	*/
	public void saveData(String fileName){}

	/*
	 * used to send the board status to the user
	 */
	private void sendBoardToUser(){
		String[][] arrayBoard = board.renderBoardAsStringArray();
		try{
			outputMSG.write("STARTBOARD");
			outputMSG.newLine();
			for(String[] row : arrayBoard){
				for(int colPos = 0; colPos < row.length; colPos++){

					if(colPos == row.length - 1){
						outputMSG.write(row[colPos]);
						outputMSG.newLine();
					} else{
						outputMSG.write(row[colPos]);
						outputMSG.write("\t");
					}
				}
				outputMSG.newLine();
			}

			outputMSG.write("ENDBOARD");
			outputMSG.newLine();
			outputMSG.flush();
		} catch (IOException error){
			System.out.println("Error on sending status of board to user: " + error);
		}

	}

	/*
	 * this method is used to process the user moving a piece on the board
	 */
	private void movingPiece(){
		int[] userSelection= new int[2];
		boolean validMove = false;
		//this is used to handle user responses and execute the players move on the checker board
		while(!validMove){
			//userSelection[0] = userReqPieceMove(true);
			//userSelection[1] = userReqPieceMove(false);

			validMove = true;
			try{
				CheckerGameLogic.validMove(userSelection, null, board, null);
			} catch (IllegalMove message){
				System.out.println(message);
			}
		}


		//sending a response back to the user that the response was valid
		sendingMSG("VALIDRESPONSE");

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