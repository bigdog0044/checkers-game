package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
public class PlayerGameCommunication{
    private Socket socket;
    private BufferedReader incomingMSG;
    private BufferedWriter outputMSG;
    private String line;
	private String playerType = "";
	private String userUUID;
	private String[][] checkerBoard;
    public PlayerGameCommunication(Socket socket, String userUUID){
        this.socket = socket;
		this.userUUID = userUUID;
        try{
			this.incomingMSG = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.outputMSG = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException error){
			System.out.println("Error on socket within GameHandler");
		}
    }

    public void startPlaying(){
        try{
			line = incomingMSG.readLine();
			if (line.equals("GAMESTARTED")){
				sendingMSG(userUUID, "PLAYERTYPEREC", "ENDPLAYERTYPEREC");

				line = incomingMSG.readLine();

				if(line.equals("PLAYERTYPE")){
					while(!line.equals("ENDPLAYERTYPE")){
						playerType = incomingMSG.readLine();
						line = incomingMSG.readLine();
					}
				}
			} else{
				System.out.println("Something went wrong with PlayerGameComms: " + line);
			}
		
            while (!line.equals("ENDGAME")){
				sendingMSG("BOARDREC");

				line = incomingMSG.readLine();
				System.out.println("PlayerGameCommunication reads: " + line);
				if(line.equals("STARTBOARD")){
					int totalRows = Integer.parseInt(incomingMSG.readLine());
					int totalCols = Integer.parseInt(incomingMSG.readLine());
					this.checkerBoard = new String[totalRows][totalCols];
					while(!line.equals("ENDBOARD")){
						for(String[] row : this.checkerBoard){
							if((line = incomingMSG.readLine()).equals("STARTROW")){
								int rowPos = 0;
								while(!((line = incomingMSG.readLine()).equals("ENDROW")) && rowPos < totalCols){
									row[rowPos] = line;
									rowPos++;
								}
							} else {
								if(!line.equals("ENDBOARD")){
									System.out.println("Something went wrong when trying to translate board on client side: " + line);
								}
							}
						}
						if((line = incomingMSG.readLine()).equals("STARTBOARD")){
							
						} else {
							System.out.println("Error invalid header on reading board system: " + line);
						}
					}
				} else {
					System.out.println("Error on reading json obj on client side: " + line);
					break;
				}
                

				displayBoard(this.checkerBoard);

				//used to enable the player to select their next move
				sendingMSG("USERMOVEDIS");

				try{
					line = incomingMSG.readLine();
					if(line.equals("USERRESPONSEREQ")){
						while(!(line = incomingMSG.readLine()).equals("ENDMSG")){
							System.out.println(line);
						}
					} else {
						System.out.println("Error on user move on clientside request: " + line);
					}


					Scanner userMoveKeyboardOBJ = new Scanner(System.in);
					boolean userResponseValid = false;
					while(!userResponseValid){
						try{
							int userMoveSelectionResp = userMoveKeyboardOBJ.nextInt();

							if(userMoveSelectionResp == 1){
								//do stuff
								userResponseValid = true;
							} else if (userMoveSelectionResp == 2){
								sendingMSG("REQENDGAME");
								userResponseValid = true;
							}
						} catch (InputMismatchException error){
							System.out.println("Invalid input. Please choose either [1] or [2]");
						}
					}

					userMoveKeyboardOBJ.close();
				} catch (IOException error){
					System.out.println("Error on user move on clientside request: " + error);
				}
			}

			System.out.println("PlayerCommunication has ended there loop");
			
        } catch (IOException error){
            System.out.println("Error on player communication startPlaying method: " + error);
        }


		System.out.println("Player communication end");

    }

	private void displayBoard(String[][] board){
		for(String[] row : board){
			for(String cell : row){
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}

	private void userMoveHandler(){

	}

	// private void processBoard(int totalRows, int totalCols){
	// 	String[][] processedCheckBoard = new String[totalRows][totalCols];
	// }

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