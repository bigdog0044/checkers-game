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
    private int total;
    /* 
     * 0 = black
     * 1 = white
    */
    public CheckerBoard(int size1, int size2){
        this.board = new int[size1][size2];
        this.total = 0;

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

    private void SetUpBoardPlayers(int[][] board){
        //Player 2
        for(int x = 0; x < 2; x++){
            for(int y = 0; y < board[x].length; y++){
                if(board[x][y] == 0){
                    board[x][y] = 3;
                }
            }
        }
        
        for(int x = board[0].length - 2; x < board[0].length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(board[x][y] == 0){
                    board[x][y] = 2;
                }
            }
        }
    }
}