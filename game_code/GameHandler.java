package game_code;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class GameHandler{
	private CheckerBoard board;
	private String playerFolder = "";
	private Socket socket;
	private boolean gameFinished = false;
	private BufferedReader incomingMSG;
	private BufferedWriter outputMSG;

	
	public GameHandler(CheckerBoard board, String playerFolder, Socket socket){
		this.board = board;
		this.playerFolder = playerFolder;
		this.socket = socket;
		// try(
		// 	this.incomingMSG = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		// 	this.outputMSG = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		// ){

		// 	// while(!gameFinished){

		// 	// }

		// } catch (IOException error){
		// 	System.out.println("Error on game handler method: " + error);
		// }
		
	}

	/*
	* used to start the gaming session
	*/

	public void startGame(){



	}


	/*
	* used to save player data
	*/
	public void saveData(String fileName){}

	/*
	* this is used to send the current board status to a given user
	*/
	private void displayBoard(){}

	private void gameWelcomeMSG(){

	}

	private void SendingMSG(String message, String header, boolean needsMSG){
		try{
			if(needsMSG){
				
			} else{
				outputMSG.write(header);
				outputMSG.newLine();
				outputMSG.flush();
			}
		} catch(IOException error){
			System.out.println("Error on sending msg in gamehandler: " + error);
		}
	}

}