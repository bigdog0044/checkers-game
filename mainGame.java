public class mainGame {
        public static void main(String[] args){
        CheckerBoard board = new CheckerBoard(8,8);
        CheckerPieces piece1 = new CheckerPieces(0,1,PlayerTeam.PLAYER1, "test");
        
        board.rendarBoard();
    }
}
