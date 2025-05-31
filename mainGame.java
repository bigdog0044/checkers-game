public class mainGame {
        public static void main(String[] args){
        CheckerGameLogic gameLogic = new CheckerGameLogic();
        CheckerBoard board = new CheckerBoard(8,8);
        CheckerPieces piece1 = new CheckerPieces(0,1,PlayerTeam.PLAYER1, "test");
        
        //board.setValue(5, 1, 2);
        //board.setValue(5, 3, 3);
        
        
        //System.out.println(board.returnCheckerPiece(7, 1, PlayerTeam.PLAYER1).getLocation()[0][0]);
        
        
        try{
            CheckerPieces player = board.returnCheckerPiece(6, 1, PlayerTeam.PLAYER2);
            int[] reqPos = {5,1};
            boolean result;
            result = gameLogic.isValidMove(reqPos , player, board,Direction.RIGHT);   
        } catch(InvalidPiece | IllegalMove e){
            System.out.println(e.getMessage());
        }
        
        
        System.out.println();
        System.out.println("Below is the actual board rendered");
        System.out.println();
            
        board.rendarBoard();
    }
}
