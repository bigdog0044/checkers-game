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
        this.board = SetUp(this.board);
    }


    public int[][] boardStatus(){ return this.board;}
    public void rendarBoard(){
        int rowNum = 1;
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
                    default:
                        break;
                }
            }
            System.out.println();
            rowNum += 1;
        }

        //used for outputting column numbers
        for(int i = 1; i < board[0].length + 1; i++){
            System.out.print(i + " ");
        }

    }

    private int[][] SetUp(int[][] board){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if((y + x) % 2 == 0){
                    board[x][y] = 1;
                } else {
                    board[x][y] = 0;
                }
            }
        }

        return board;
    }
}