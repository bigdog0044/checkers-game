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
    private Colour[][] board;
    private int total;
    /* 
     * 0 = black
     * 1 = white
    */
    public CheckerBoard(int size1, int size2){
        this.board = new Colour[size1][size2];
        this.total = 0;

        //used to actually setup the board ready for players to be added onto the board
        this.board = SetUpBoard(this.board);
    }

    public Colour getSquare(int location1, int location2){
        return this.board[location1][location2];
    }
    
    public Colour[][] boardStatus(){ return this.board;}

    public void rendarBoard(){
        int rowNum = this.board[0].length;
        for(Colour[] row : this.board){
            System.out.print(rowNum);
            for(Colour value : row){
                switch (value) {
                    case BLACK:
                        System.out.print(" " + Colour.BLACK + " ");
                        break;
                    case WHITE:
                        System.out.print(" " + Colour.WHITE + " ");
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

    private Colour[][] SetUpBoard(Colour[][] board){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if((y + x) % 2 == 0){
                    board[x][y] = Colour.WHITE;
                } else {
                    board[x][y] = Colour.BLACK;
                }
            }
        }

        return board;
    }
}