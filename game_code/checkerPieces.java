package game_code;

class CheckerPieces{
    private int[] location;
    private PlayerTeam team;
    private String CheckerName;
    private boolean isQueen;

    public CheckerPieces(int rowNum, int colNum, PlayerTeam team, String name){
        this.location = new int[2];
        this.location[0] = rowNum;
        this.location[1] = colNum;
        this.team = team;
        this.CheckerName = name;
        this.isQueen = false;
    }

    public int[] getLocation(){
        return this.location;
    }

    public PlayerTeam getTeam(){ return this.team;}

    public void SetNewLocation(int[]location) {this.location = location;}

    public boolean getQueenStatus(){return this.isQueen;}
    // need one function to update the checker piece on relation to the board
}