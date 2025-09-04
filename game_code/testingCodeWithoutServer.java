package game_code;

public class testingCodeWithoutServer {

    public static void main(String args[]){
        CheckerBoard board = new CheckerBoard(8, 8);
        board.rendarBoard();

        board.convertingBoardToJSON();
    }
}
