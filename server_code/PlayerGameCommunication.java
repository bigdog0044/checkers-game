package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;

import java.net.Socket;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

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

				if(line.equals("STARTBOARD")){
					int totalRows = Integer.parseInt(incomingMSG.readLine());
					int totalCols = Integer.parseInt(incomingMSG.readLine());
					this.checkerBoard = new String[totalRows][totalCols];
					while(!line.equals("ENDBOARD")){
						for(String[] row : this.checkerBoard){
							if((line = incomingMSG.readLine()).equals("STARTROW")){
								int rowPos = 0;
								System.out.println("Current playercom line: " + line);
								while(!((line = incomingMSG.readLine()).equals("ENDROW")) && rowPos < totalCols){
									row[rowPos] = line;
									rowPos++;
								}
							} else {
								System.out.println("Something went wrong when trying to translate board on client side: " + line);
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
            }
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