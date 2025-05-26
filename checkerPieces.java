class CheckerPieces{
    private int[][] location;
    private PlayerTeam team;
    private String CheckerName;

    public CheckerPieces(int rowNum, int colNum, PlayerTeam team, String name){
        this.location = new int[2][1];
        this.location[0][0] = rowNum;
        this.location[1][0] = colNum;
        this.team = team;
        this.CheckerName = name;
    }

    public int[][] getLocation(){
        return this.location;
    }

    public PlayerTeam getTeam(){ return this.team;}
    // need one function to update the checker piece on relation to the board
}