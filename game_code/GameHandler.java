package game_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GameHandler{
	private CheckerBoard board;
	private String playerFolder = "";
	private Socket socket;
	private boolean gameFinished = false;
	private BufferedReader incomingMSG;
	private BufferedWriter outputMSG;
	private boolean player1_turn = false;
	private boolean player2_turn = false;
	private CheckerGameLogic CheckerGameLogic = new CheckerGameLogic();


	
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

				//allowing user input and processing the users selection
				movingPiece();

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
			userSelection[0] = userReqPieceMove(true);
			userSelection[1] = userReqPieceMove(false);

			validMove = true;
			try{
				CheckerGameLogic.validMove(userSelection, null, board, null);
			} catch (IllegalMove message){

			}
		}


		//sending a response back to the user that the response was valid
		sendingMSG("VALIDRESPONSE");

	}

	/*
	 * used to process user response and return the position the user wants to move
	 * @args isRow - used to indicate if its a row or col that needs to be processed 
	 */
	private int userReqPieceMove(boolean isRow){
		int value = 0;
		if(isRow){
			while(true){
				String message = "Please choose a row on the board to move. Note the board starts from 0 up to 7";
				sendingMSG(message, "USERRESPONSEREQ", "ENDOFMSG");
		
				try{
		
					String line = incomingMSG.readLine();
		
					if(line.equals("USERRESPONSE")){
						try{

							line = incomingMSG.readLine();
							System.out.println("Game handler user response: " + line);
							value = Integer.parseInt(line);
							break;
						} catch (NumberFormatException error){
							sendingMSG("Invalid response please enter a number", "ERROR", "ENDOFMSG");
						}
					}
				} catch (IOException error){
					System.out.println("Error on movingPiece method: " + error);
				}
			}
		} else {
			while(true){
				String message = "Please choose a column on the board to move. Note the board starts from 0 up to 7";
				sendingMSG(message, "USERRESPONSEREQ", "ENDOFMSG");
		
				try{
		
					String line = incomingMSG.readLine();
		
					if(line.equals("USERRESPONSE")){
						try{

							line = incomingMSG.readLine();
							System.out.println("Game handler user response: " + line);
							value = Integer.parseInt(line);
							break;
						} catch (NumberFormatException error){
							sendingMSG("Invalid response please enter a number", "ERROR", "ENDOFMSG");
						}
					}
				} catch (IOException error){
					System.out.println("Error on movingPiece method: " + error);
				}
			}
		}

		return value;
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