enum Colour{
    WHITE,BLACK
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
        for(int[] row : this.board){
            for(int value : row){
                switch (value) {
                    case 0:
                        System.out.print(" Black ");
                        break;
                    case 1:
                        System.out.print(" White ");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }

    }

    private int[][] SetUp(int[][] board){
        boolean isBlackTile = true;
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