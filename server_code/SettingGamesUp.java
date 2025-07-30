package server_code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileWriter;
import java.util.UUID;
import game_code.CheckerBoard;


public class SettingGamesUp {
	private static final String saveDir = "game-save-files/";
	private static final String trackerFileName = "saveTracker";
	private String sessionFolderUUID;
	private String sessionFolderLocation;
	private static BufferedWriter output;
	private String userUUID;

	public SettingGamesUp(String userUUID, Socket socket, String userID, CheckerBoard checkerBoardOBJ, boolean botRequest){
		try{
			output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
			this.userUUID = userID;

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

			 sessionFolderLocation = gameCreation(gameFolderUUID.toString(), userUUID);
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

		} catch (IOException e){
			System.out.println("Error on UserFolderSetup: " + e);
		}
	}


	//returning current folder UUID so it can be used by other parts of the program
	public String getCurrentFolderUUID(){
		return sessionFolderUUID;
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
}
