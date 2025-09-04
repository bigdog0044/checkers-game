package game_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import server_code.DBUsernameAndPass;

public class GameHandler{
	private CheckerBoard board;
	private Socket socket;
	private BufferedReader incomingMSG;
	private BufferedWriter outputMSG;
	private CheckerGameLogic CheckerGameLogic = new CheckerGameLogic();
	private Connection connection;
	private String gameID;
	private String gameOwnerID; 
	private String line; //used to process response from user

	
	public GameHandler(CheckerBoard board,Socket socket, String gameID){
		//conencting to the database
		try{
			DBUsernameAndPass dbinfo = new DBUsernameAndPass();
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(
	        "jdbc:mysql://localhost:3306/checkergamedb", dbinfo.getUsername(), dbinfo.getPassword());
		} catch (ClassNotFoundException | SQLException e){
			System.out.println("Error on GameHandler: " + e);
		}
		
		this.board = board;
		this.socket = socket;
		this.gameID = gameID;
		this.gameOwnerID = ownerID();

		try{
			this.incomingMSG = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.outputMSG = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException error){
			System.out.println("Error on socket within GameHandler");
		}

		//used to inform any clients connected that the game has started
		sendingMSG("GAMESTARTED");


		try{

			line = incomingMSG.readLine();
	
			if (line.equals("PLAYERTYPEREC")){
				String ID = "";
	
				while(!line.equals("ENDPLAYERTYPEREC")){
					ID = incomingMSG.readLine();
					line = incomingMSG.readLine();
				}
	
				String playerType = checkPlayerType(ID);

				sendingMSG(playerType, "PLAYERTYPE", "ENDPLAYERTYPE");
			}
		} catch (IOException error){
			System.out.println("Error on trying to read playertype header stuff: " + error);
		}

		//used to start the game session
		// while(!isGameOver()){
		// 	System.out.println("this is running");
			

		// 	//initialises who is going
		// 	if(playerTurn().equals("player1")){
		// 		sendingMSG("STARTPLAYER1");
		// 	} else if (playerTurn().equals("player2")){
		// 		sendingMSG("STARTPLAYER2");
		// 	}

		// 	try{
		// 		line = incomingMSG.readLine();
		// 		System.out.println("GameHandler currently reads: " + line);
		// 		if (line.equals("BOARDREC")) {
		// 			sendBoardToUser();
		// 		}
		// 	} catch (IOException error){
		// 		System.out.println(error);
		// 	}

		// 	// sendingMSGWithoutFlush("USERRESPONSEREQ");
		// 	// sendingMSGWithoutFlush("Please choose from the following options");
		// 	// sendingMSGWithoutFlush("[1] move piece");
		// 	// sendingMSGWithoutFlush("[2] quit game");
		// 	// sendingMSG("ENDMSG");

		// 	//int userResponse;
		// 	//userResponse = incomingMSG.read();


			
			

		// }


		//informs the players the game is now over
		sendingMSG("ENDGAME");
		
	}

	/*
	 * this is used to find out if player is either player 1 or 2
	 * @return player type
	 */

	 private String checkPlayerType(String playerUUID){
		String result = "";
		try{

			String sqlStatement = "SELECT `player1_ID` FROM `gameinfo` WHERE `player1_ID` = ? AND `game_ID`=?";
			PreparedStatement preparedSqlStatement = connection.prepareStatement(sqlStatement);
			preparedSqlStatement.setString(1,playerUUID);
			preparedSqlStatement.setString(2, this.gameID);
			ResultSet resultSet = preparedSqlStatement.executeQuery();

			/*
			 * if resultSet = false -> true -> means its player2
			 * if resultSet = true -> false -> means its player1 
			 */
			if(!resultSet.next()){
				result = "player1";
			} else {
				result = "player2";
			}
		} catch (SQLException error){
			System.out.println("Error on sql for checkPlayerType: " + error);
		}

		return result;
	 }

	/*used to get the owner of the game session ID */

	private String ownerID(){
		String output = "";
		try{
			String sqlStatement = "SELECT `owner_game_ID` FROM `gameinfo` WHERE game_ID = ?; ";
			PreparedStatement preparedSqlStatement = connection.prepareStatement(sqlStatement);
			preparedSqlStatement.setString(1, this.gameID);
			ResultSet result = preparedSqlStatement.executeQuery();
			result.next();
			output = result.getString(1);
		} catch (SQLException error){
			System.out.println("Error on ownerID method: " + error);
		}

		return output;
	}
	
	/*used to work out which players turn it is */
	
	private String playerTurn(){
		String output = "";
		try{
			String sqlStatement = "SELECT `player_tern` FROM `gameinfo` WHERE `game_ID` = ?;";
			PreparedStatement preparedSqlStatement = connection.prepareStatement(sqlStatement);
			preparedSqlStatement.setString(1, this.gameID);
			ResultSet result = preparedSqlStatement.executeQuery();
			result.next();
			output = result.getString(1);
		} catch (SQLException error){
			System.out.println("Error on playerTurn method: " + error);
		}

		return output;
	}
	/*
	 * used to check if the game is over
	 */

	 private boolean isGameOver(){
		 try{
	
			 String sqlQuery = "SELECT * FROM `gameinfo` WHERE owner_game_ID=? AND gameOver=?;";
			 PreparedStatement preparedSql = connection.prepareStatement(sqlQuery);
			 preparedSql.setString(1, this.gameOwnerID);
			 preparedSql.setBoolean(2, true);
			 ResultSet result = preparedSql.executeQuery();

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
		String boardStrRowLength = Integer.toString(board.returnRowLen());
		JSONObject boardJsonObject = board.convertingBoardToJSON();
		
		sendingMSGWithoutFlush("STARTBOARD");
		sendingMSGWithoutFlush(boardStrRowLength);
		sendingMSGWithoutFlush(boardJsonObject.toJSONString());
		sendingMSG("ENDBOARD");
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
	
	private void sendingMSGWithoutFlush(String message){
		try{
			outputMSG.write(message);
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