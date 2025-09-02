package server_code;

import java.sql.Connection;

import game_code.GameHandler;

public class Player1Communication {
    private String userUUID;
    private String gameID;
    private Connection connection;
    private GameHandler gameManager;

    public Player1Communication(String userID, String gameSessionID){
        this.userUUID = userID;
        this.gameID = gameSessionID;
        this.gameManager = gameHandler;
    }

    private void renderBoard(){

    }
    /*
	 * used to process user response and return the position the user wants to move
	 * @args isRow - used to indicate if its a row or col that needs to be processed 
	 */
	private void userReqPieceMove(boolean isRow){}
}
