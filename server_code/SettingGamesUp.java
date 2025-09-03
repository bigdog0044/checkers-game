package server_code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import game_code.CheckerBoard;


public class SettingGamesUp {
	private static final String saveDir = "game-save-files/";
	private static final String trackerFileName = "saveTracker";
	private String sessionFolderUUID;
	private String sessionFolderLocation;
	private Socket socket;
	private static BufferedWriter output;
	private String userUUID;

	public SettingGamesUp(String userUUID, Socket socket, String userID, CheckerBoard checkerBoardOBJ, boolean botRequest){
		try{
			output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
			this.userUUID = userID;
			this.socket = socket;

			output.write("SESSIONCREATING");
			output.newLine();
			output.flush();

			 File folderOBJ = new File(saveDir + userUUID);

			 //creating user folder
			 if(!folderOBJ.exists()){
				 folderOBJ.mkdirs();

				 SendingCreationMSG("folder", "creating");
			 }


			 //generates a new UUID for a session folder
			 UUID gameFolderUUID = UUID.randomUUID();

			 sessionFolderUUID = gameFolderUUID.toString();

			 sessionFolderLocation = gameCreation(sessionFolderUUID, userUUID);
			 //letting user know that user session has been created
			 if(sessionFolderLocation != null){
			 	output.write("New game session has been created with the following ID: " + gameFolderUUID.toString());
			 	output.newLine();
			 	output.flush();

			 	if (gameFileCreation(sessionFolderLocation, userUUID, checkerBoardOBJ, botRequest)){
			 		//add stuff in  
			 	}
			} else {
				System.out.println("An error has occured when creating folder");
			}
			output.write("ENDSESSCREATE");
			output.newLine();
			output.flush();

			//used to populate gameinfo table
			updateGameInfoRecord(sessionFolderUUID, userUUID);

			//used to start the player1 communication method
		} catch (IOException e){
			System.out.println("Error on UserFolderSetup: " + e);
		}
	}


	//returning current folder UUID so it can be used by other parts of the program
	public String getCurrentFolderUUID(){
		return sessionFolderUUID;
	}


	//returning current session folder location
	public String getSessionFolderLocation(){
		return sessionFolderLocation;
	}

	//used to send custom creation messages
	private void SendingCreationMSG(String startingMSG,String message){
		try{
			output.write(startingMSG + " new " + message + "!");
			output.newLine();
			output.flush();
		} catch (IOException e){
			System.out.println("Error on sendingcreationmsg: " + e);
		}

	}


	/*
	* used to create the folder in which game files have been saved in
	* @return directory location if successful folder creation
	*/
	private String gameCreation(String folderName, String userID){
		File gameFolderOBJ = new File(saveDir + userID + "/"+ folderName);
		gameFolderOBJ.mkdir();

		return gameFolderOBJ.getPath() + "\\";
	}



	/*
	* used to create and write into files all data required to get the game running
	*@return true if successful
	*/
	private boolean gameFileCreation(String targetFolder,String userID, CheckerBoard board, boolean botRequest){
		try{
			//outputs to server for logs reason
			System.out.println("Creating folder on following path: " + targetFolder);
			System.out.println("Creating file on following path: " + targetFolder);

			//creating file objects
			File metaDataOBJ = new File(targetFolder,"metadata.txt");
			File gameSaveOBJ = new File(targetFolder,"gameSave.txt");

			
			boolean successCreaton = false;

			if (metaDataOBJ.createNewFile() && gameSaveOBJ.createNewFile()){
				//outputs to server for logs reason
				System.out.println("Creating meta file on following path: " + metaDataOBJ.getPath());
				System.out.println("Creating meta file on following path: " + gameSaveOBJ.getPath());
				try(
					FileWriter metaDataWriterOBJ = new FileWriter(metaDataOBJ.getPath());
					FileWriter gameSaveWriterOBJ = new FileWriter(gameSaveOBJ.getPath());
				) {

					//this does the writing for the meta data file
					metaDataWriterOBJ.write("Owner: " + userID);
					metaDataWriterOBJ.write("\n");
					metaDataWriterOBJ.write("Game-save-dir-location: " + gameSaveOBJ.getPath());
					metaDataWriterOBJ.write("\n");
					metaDataWriterOBJ.write("Number-rows: " + board.returnRowLen());
					metaDataWriterOBJ.write("\n");
					metaDataWriterOBJ.write("Number-columns: " + board.returnColLen());
					metaDataWriterOBJ.write("\n");
					metaDataWriterOBJ.write("Max-players: 2");
					metaDataWriterOBJ.write("\n");
					metaDataWriterOBJ.write("Requires bot: " + botRequest);


					//this handles writing the board peices to the file itself

					for (int[] row : board.boardStatus()){
						for (int colPos = 0; colPos < row.length; colPos++){

							if(colPos == row.length - 1){
								gameSaveWriterOBJ.write(row[colPos] + "\n");
							} else {
								gameSaveWriterOBJ.write(row[colPos] + " ");
							}
						}
					}

				} catch(IOException error){
					System.out.println("gameFileCreation method error:" + error);

				}

			


			}
			return successCreaton;
		} catch (IOException error){
			System.out.println("Error on file creation: " + error);
			return false;
		}
		
		
	}

	/*
	 * this method is used to update the gameinfo table with a new game session
	 * this will be used later by the server to check for any missing files
	 */

	private void updateGameInfoRecord(String gameSessionUUID, String userUUID){
		String DBName = "checkergameDB";
		DBUsernameAndPass dbinfo = new DBUsernameAndPass();
		Connection connection;
		try{
             Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + DBName, dbinfo.getUsername(), dbinfo.getPassword());

			String sqlStatement = " INSERT INTO `gameinfo`(`owner_game_ID`, `player1_ID`, `player2_ID`, `player_tern`, `game_ID`, `gameOver`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedSQL = connection.prepareStatement(sqlStatement);
            preparedSQL.setString(1,userUUID);
            preparedSQL.setString(2,userUUID);
			preparedSQL.setNull(3, java.sql.Types.VARCHAR);
			preparedSQL.setString(4, "player1");
			preparedSQL.setString(5, gameSessionUUID);
			preparedSQL.setBoolean(6, false);
            preparedSQL.executeUpdate();
        } catch (ClassNotFoundException | SQLException e){
            System.out.println("ServerThread class initialisation method error: " + e);
        }
	}
}
