public class mainGame {
        public static void main(String[] args){
        CheckerBoard board = new CheckerBoard(1,1);
        CheckerPieces piece1 = new CheckerPieces(0,1,"player1");
        System.out.println(piece1.getLocation()[0][0]);
        System.out.println(piece1.getLocation()[1][0]);

        board.rendarBoard();
    }
}
