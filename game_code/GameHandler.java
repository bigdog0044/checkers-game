package game_code;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class GameHandler{
	private CheckerBoard board;
	private String playerFolder = "";
	private Socket socket;
	private boolean gameFinished = false;
	private BufferedReader incomingMSG;
	private BufferedWriter outputMSG;
	private boolean player1_turn = false;
	private boolean player2_turn = false;


	
	public GameHandler(CheckerBoard board, String playerFolder, Socket socket){
		this.board = board;
		this.playerFolder = playerFolder;
		this.socket = socket;
		
	}
	
	/*
	* used to start the gaming session
	*/
	
	public void startGame(){
		try{
			this.incomingMSG = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.outputMSG = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

			//this sends out a message saying game has started
			sendingMSG("STARTGAME");
			
			//outputting client 
			while(!gameFinished){
				//sending the board to the user
				sendBoardToUser();

				//allowing user input

				gameFinished = true;
	
			}

			sendingMSG("ENDGAME");
		} catch (IOException error){
			System.out.println("Error on game handler method: " + error);
		}

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
				System.out.println(row.length);
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
		String message = "Please choose a piece on the board to move";
		sendingMSG(message, "USERRESPONSEREQ", "ENDOFMSG");

		try{

			String line = incomingMSG.readLine();
		} catch (IOException error){
			System.out.println("Error on movingPiece method: " + )
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