package server_code;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

public class PlayerGameCommunication{
    private Socket socket;
    private BufferedReader incomingMSG;
    private BufferedWriter outputMSG;
    private String line;
	private String playerType = "";
	private String userUUID;
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
			int boardRowLength = 0;
			String nonJsonBoard = "";

			line = incomingMSG.readLine();
			if (line.equals("GAMESTARTED")){
				sendingMSG(userUUID, "PLAYERTYPEREC", "ENDPLAYERTYPEREC");

				line = incomingMSG.readLine();
			} else{
				System.out.println("Something went wrong with PlayerGameComms: " + line);
			}
		
            while (!line.equals("ENDGAME")){
				sendingMSG("BOARDREC");

				line = incomingMSG.readLine();
				System.out.println("Player client currently reads: "+ line);

				if(line.equals("STARTBOARD")){
					boardRowLength = Integer.parseInt(incomingMSG.readLine());
					nonJsonBoard = incomingMSG.readLine();
					incomingMSG.readLine(); //to put it at the end of the message
				}

				System.out.println(boardRowLength);
				System.out.println(nonJsonBoard);
                
				// line = incomingMSG.readLine();
                // if(line.equals("USERRESPONSEREQ")){
                    
                // }
            }
        } catch (IOException error){
            System.out.println("Error on player communication startPlaying method: " + error);
        }



    }

	public void displayBoard(JSONObject boardJsonObject){
		
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


    /*
     * 
     * String[][] arrayBoard = board.renderBoardAsStringArray();
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
     */
}