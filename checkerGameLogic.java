class CheckerGameLogic {
    public boolean isValidMove(int[][] requestedPos, CheckerPieces piece){
        //non-queen valid moove checker
        if (!(piece.getQueenStatus())){
            return validMove(requestedPos,piece);
        }
        return false;
    }

    private boolean validMove(int[][] requestedPos, CheckerPieces piece){
        if(requestedPos[0] < )
        return false;
    }
    /*
     * things to write
     * chance piece to queen once it has gotten to the end of the board
     * valid move calculator
     * implement player hit detection
     */
}
