public class mainGame {
        public static void main(String[] args){
        CheckerGameLogic gameLogic = new CheckerGameLogic();
        CheckerBoard board = new CheckerBoard(8,8);
        CheckerPieces piece1 = new CheckerPieces(0,1,PlayerTeam.PLAYER1, "test");
        
        board.rendarBoard();


        //System.out.println(board.returnCheckerPiece(7, 1, PlayerTeam.PLAYER1).getLocation()[0][0]);
        
        CheckerPieces player = board.returnCheckerPiece(7, 1, PlayerTeam.PLAYER1);
        int[][] reqPos = {{0},{0}};
        gameLogic.isValidMove(reqPos, player);
    }
}
