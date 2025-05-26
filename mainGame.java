public class mainGame {
        public static void main(String[] args){
        CheckerBoard board = new CheckerBoard(8,8);
        

        System.out.println(board.boardStatus()[0][2]);
        board.rendarBoard();
    }
}
