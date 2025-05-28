enum Direction{
    LEFT,RIGHT 
}
class CheckerGameLogic {
    public boolean isValidMove(int[] reqPos, CheckerPieces piece, CheckerBoard board, Direction direction){
        //non-queen valid moove checker
        if (!(piece.getQueenStatus())){
            return validMove(reqPos,piece, board, direction);
        }
        return false;
    }

    private boolean validMove(int[] reqPos, CheckerPieces piece, CheckerBoard board, Direction direction){
        //checks to see if player has requested a position which falls out of range of the board
        if (reqPos[0] > board.returnRowLen() || reqPos[1] > board.returnColLen()){
            return false;
        }
        
        if (reqPos[0] < 0 || reqPos[1] < 0){
            return false;
        }


        moveCalculation(board, piece, direction);
        

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
            
            nextRow = curPieceLocationRow - 1;
            nextCol = curPieceLocationCol - 1;
            
            //note to future self: rewrite this with a custom exception
            //used to provide the value at a specific square
            if (!(nextRow < 0 && nextCol - 1 > board.returnRowLen())){
                if(direction == Direction.LEFT){
                    valueAtSqr = board.getSquare(curPieceLocationRow - 1, curPieceLocationCol - 1);
                } else{
                    valueAtSqr = board.getSquare(curPieceLocationRow - 1, curPieceLocationCol + 1);
                }
            } else{
                return null; //error has occured
            }
            
            //assigning the values into the result array
            result[0][0] = nextRow;
            result[0][1] = nextCol;
            result[1][0] = valueAtSqr;
            result[1][1] = 0;
            
            return  result;
        } else {

            curPieceLocationRow = piece.getLocation()[0];
            curPieceLocationCol = piece.getLocation()[1];
            
            nextRow = curPieceLocationRow - 1;
            nextCol = curPieceLocationCol - 1;
            
            //note to future self: rewrite this with a custom exception
            //used to provide the value at a specific square
            if (!(nextRow < 0 && nextCol - 1 > board.returnRowLen())){
                if(direction == Direction.LEFT){
                    valueAtSqr = board.getSquare(curPieceLocationRow + 1, curPieceLocationCol - 1);
                } else{
                    valueAtSqr = board.getSquare(curPieceLocationRow + 1, curPieceLocationCol + 1);
                }
            } else{
                return null; //error has occured
            }
            
            //assigning the values into the result array
            result[0][0] = nextRow;
            result[0][1] = nextCol;
            result[1][0] = valueAtSqr;
            result[1][1] = 0;
            
            return  result;
        }
    }
    /*
    * things to write
    * chance piece to queen once it has gotten to the end of the board
    * valid move calculator
    * implement player hit detection
    * actual implement the player movement method
    */
    /*
     * Used to retrieve information about a player
     * @arguments board - takes the game board
     * @arguments value - takes the value from the diagonal values
     * @arguments row - takes the row number
     * @arguments col - takes the column number
     * @returns either player1, player 2 or null which indicates no player has been found
     */
    private CheckerPieces playerRetrieveInfo(CheckerBoard board, int value, int row , int col){
        if(value == 2){
            return board.returnCheckerPiece(row,col,PlayerTeam.PLAYER1);
        }
        
        if(value == 3){
            return board.returnCheckerPiece(row,col,PlayerTeam.PLAYER2);
        }
    
        return null;
    }
}
