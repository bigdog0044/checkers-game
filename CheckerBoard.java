enum Colour{
    WHITE(1),BLACK(0);

    private int value;
    
    public int getColourValue(){
        return this.value;
    }

    private Colour(int value){
        this.value = value;
    }
}

//used to assign a enum value to a team the checker piece is associated with
enum PlayerTeam{
    PLAYER1(2), PLAYER2(3);

    private int team;
    private PlayerTeam(int value){
        this.team = value;
    }

    public int getValue(){
        return this.team;
    }
}
class CheckerBoard {
    private int[][] board;
    private CheckerPieces[] player1Pieces; 
    private CheckerPieces[] player2Pieces; 
    /* 
     * 0 = black
     * 1 = white
    */
    public CheckerBoard(int rowSize, int colSize){
        this.board = new int[rowSize][colSize];
        this.player1Pieces = new CheckerPieces[(int)rowSize*2];
        this.player2Pieces = new CheckerPieces[(int)rowSize*2];
        //used to actually setup the board ready for players to be added onto the board
        this.board = SetUpBoard(this.board);
        SetUpBoardPlayers(this.board);
    }

    public int getSquare(int location1, int location2){
        return this.board[location1][location2];
    }
    
    public int[][] boardStatus(){ return this.board;}

    //remove once done testing
    public void setValue(int row, int col, int value){
        this.board[row][col] = value;
    }

    public void rendarBoard(){
        int rowNum = this.board[0].length;
        for(int[] row : this.board){
            System.out.print(rowNum);
            for(int value : row){
                switch (value) {
                    case 0:
                        System.out.print(" " + Colour.BLACK + " ");
                        break;
                    case 1:
                        System.out.print(" " + Colour.WHITE + " ");
                        break;
                    case 2:
                        System.out.print( " " + PlayerTeam.PLAYER1 + " ");
                        break;

                    case 3:
                        System.out.print( " " + PlayerTeam.PLAYER2 + " ");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();

            //will improve later since this isn't the best implementation in my view
            if(rowNum == this.board[0].length - (this.board[0].length - 1)){
                for(int i = 1; i < this.board[0].length + 1; i++){
                    System.out.print("   " + i + "   ");
                }

                System.out.println();
            }
            rowNum -= 1;
        }
    }

    private int[][] SetUpBoard(int[][] board){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if((y + x) % 2 == 0){
                    board[x][y] = 0; //white
                } else {
                    board[x][y] = 1; //black tile
                }
            }
        }

        return board;
    }

    //their seems to be a better way of sorting this out, will come back later and redo
    private void SetUpBoardPlayers(int[][] board){
        /*
         * This is used to setup the starting position the player will be at 
         */
        //Player 2
        int pl2ArrCount = 0;
        int pl1ArrCount = 0;
        for(int x = 0; x < 2; x++){
            for(int y = 0; y < board[x].length; y++){
                if(board[x][y] == 0){
                    board[x][y] = 3;

                    //adding the checker pieces to a seperate array
                    player2Pieces[pl2ArrCount] = new CheckerPieces(x,y, PlayerTeam.PLAYER2, PlayerTeam.PLAYER2.name());
                    pl2ArrCount++;
                }
            }
        }
        
        //player 1
        for(int x = board[0].length - 2; x < board[0].length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(board[x][y] == 0){
                    board[x][y] = 2;
                    
                    //adding the checker pieces to a seperate array
                    player1Pieces[pl1ArrCount] = new CheckerPieces(x,y, PlayerTeam.PLAYER1, PlayerTeam.PLAYER1.name());
                    pl1ArrCount++;
                }
            }
        }
    }
}