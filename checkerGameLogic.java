class CheckerGameLogic {
    public boolean isValidMove(int reqRowPos, int reqColPos, CheckerPieces piece, CheckerBoard board){
        //non-queen valid moove checker
        if (!(piece.getQueenStatus())){
            return validMove(reqRowPos, reqColPos,piece, board);
        }
        return false;
    }

    private boolean validMove(int reqRowPos, int reqColPos, CheckerPieces piece, CheckerBoard board){
        //checks to see if player has requested a position which falls out of range of the board
        if (reqRowPos > board.returnRowLen() || reqColPos > board.returnColLen()){
            return false;
        }
        
        if (reqRowPos < board.returnRowLen() || reqColPos < board.returnColLen()){
            return false;
        }


        

        return true;
    }
    /*
     * things to write
     * chance piece to queen once it has gotten to the end of the board
     * valid move calculator
     * implement player hit detection
     */
}
