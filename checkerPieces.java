class CheckerPieces{
    private int[][] location;
    private String team;

    public CheckerPieces(int rowNum, int colNum, String team){
        this.location = new int[2][1];
        this.location[0][0] = rowNum;
        this.location[1][0] = colNum;
        this.team = team;
    }

    public int[][] getLocation(){
        return this.location;
    }

    public String getTeam(){ return this.team;}
    // need one function to update the checker piece on relation to the board
}