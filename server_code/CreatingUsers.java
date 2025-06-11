package server_code;


import java.util.UUID;
import java.util.ArrayList;

public class CreatingUsers{
    private String username;
    private UUID userID;
    private String password;
    private boolean isInGame;
    private boolean searchForGame;
    private ArrayList ipConnectHistory;
    private ArrayList gameSavesID;

    public CreatingUsers(String username, String password){
        this.username = username;
        this.password = password; //change this since i want it encrypted
        this.userID = UUID.randomUUID();
        this.isInGame = false;
        this.searchForGame = true;
        this.ipConnectHistory = new ArrayList<String>();
        this.gameSavesID = new ArrayList<String>();
    }

    public void saveuser(){

    }
}

