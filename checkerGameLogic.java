//defining a custom error for if the player does a move which isn't possible
class IllegalMove extends Exception{
    public IllegalMove (String errMSG){
        super(errMSG);
    }
}


enum Direction{
    LEFT(1),RIGHT(2);
    
    private int directionVal;

    private Direction(int val){
        this.directionVal = val;
    }

    public int getDirectionValue(){return this.directionVal;}
}
class CheckerGameLogic {
    public boolean isValidMove(int[] reqPos, CheckerPieces piece, CheckerBoard board, Direction direction) throws IllegalMove{
        //non-queen valid moove checker
        if (!(piece.getQueenStatus())){
            return validMove(reqPos,piece, board, direction);
        }
        return false;
    }

    private boolean validMove(int[] reqPos, CheckerPieces piece, CheckerBoard board, Direction direction) throws IllegalMove{
        //checks to see if player has requested a position which falls out of range of the board
        if (reqPos[0] > board.returnRowLen() || reqPos[1] > board.returnColLen()){
            throw new IllegalMove("Invalid move: selected move is outside of board");
        }
        
        if (reqPos[0] < 0 || reqPos[1] < 0){
            throw new IllegalMove("Invalid move: selected move is outside of board");
        }

        
        int[][] moveCalcResult = moveCalculation(board, piece, direction);



        for(int[] row : moveCalcResult){
            for(int value : row){
                System.out.print(value + " ");
            }
        }
        return true;
    }


    
    /*
    * this method is used to calculate the two squares the player can move to
    * @argument board - used to get the current board layout
    * @argumnent piece - used to get the current checker piece (i.e player)
    * @argument direction - used to find which direction the player is moving in
    * @return gives the location of which the player can move in, null indicates their is an error
    */
    private int[][] moveCalculation(CheckerBoard board,CheckerPieces piece, Direction direction){
        int[][] result = new int[2][2];
        int valueAtSqr;
        int curPieceLocationRow;
        int curPieceLocationCol;
        int nextRow;
        int nextCol;
        if(piece.getTeam() == PlayerTeam.PLAYER1){
            curPieceLocationRow = piece.getLocation()[0];
            curPieceLocationCol = piece.getLocation()[1];

            //note to future self: rewrite this with a custom exception
            //used to provide the value at a specific square

            if(direction.getDirectionValue() == 1){
                nextRow = curPieceLocationRow - 1;
                nextCol = curPieceLocationCol - 1;
                
                if(nextCol < 0 ){
                    valueAtSqr = -1;
                } else{
                    valueAtSqr = board.getSquare(nextRow, nextCol);
                }
            } else{
                nextRow = curPieceLocationRow - 1;
                nextCol = curPieceLocationCol + 1;
                valueAtSqr = board.getSquare(nextRow, nextCol);
            }
            
            //assigning the values into the result array
            result[0][0] = nextRow;
            result[0][1] = nextCol;
            result[1][0] = valueAtSqr;
            result[1][1] = -2;
            
            return  result;
        } else {

            curPieceLocationRow = piece.getLocation()[0];
            curPieceLocationCol = piece.getLocation()[1];
            
            
            //note to future self: rewrite this with a custom exceptions
            if(direction.getDirectionValue() == 1){
                nextRow = curPieceLocationRow + 1;
                nextCol = curPieceLocationCol - 1;

                valueAtSqr = board.getSquare(nextRow, nextCol);

                if(nextCol > board.returnRowLen() ){
                    valueAtSqr = -1;
                } else{
                    valueAtSqr = board.getSquare(nextRow, nextCol);
                }
            } else{
                nextRow = curPieceLocationRow + 1;
                nextCol = curPieceLocationCol + 1;
                valueAtSqr = board.getSquare(nextRow, nextCol);
            }
            //assigning the values into the result array
            result[0][0] = nextRow;
            result[0][1] = nextCol;
            result[1][0] = valueAtSqr;
            result[1][1] = -1;
            
            return  result;
        }
    }

    /*
    * this method is used to calculate if a player has hit another player
    * @argument board - used to get the current board layout
    * @argumnent piece - used to get the current checker piece (i.e player)
    * @argument location - used to find what location is being checked
    * @return true if player has hit another player, false otherwise
    */
    private boolean playerHitDetection(CheckerBoard board, CheckerPieces piece, int[][] location){
        

        return true;
    }

    /*
     * Used to retrieve information about a player
     * @arguments board - takes the game board
     * @arguments value - takes the value from the diagonal values
     * @arguments row - takes the row number
     * @arguments col - takes the column number
     * @returns either player1, player 2 or null which indicates no player has been found
     */
    // private CheckerPieces playerRetrieveInfo(CheckerBoard board, int value, int row , int col){
    //     if(value == 2){
    //         return board.returnCheckerPiece(row,col,PlayerTeam.PLAYER1);
    //     }
        
    //     if(value == 3){
    //         return board.returnCheckerPiece(row,col,PlayerTeam.PLAYER2);
    //     }
    
    //     return null;
    // }
}
