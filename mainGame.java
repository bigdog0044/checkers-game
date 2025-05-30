public class mainGame {
        public static void main(String[] args){
        CheckerGameLogic gameLogic = new CheckerGameLogic();
        CheckerBoard board = new CheckerBoard(8,8);
        CheckerPieces piece1 = new CheckerPieces(0,1,PlayerTeam.PLAYER1, "test");
        
        board.setValue(5, 1, 2);
        board.setValue(5, 3, 3);
        
        
        //System.out.println(board.returnCheckerPiece(7, 1, PlayerTeam.PLAYER1).getLocation()[0][0]);
        
        CheckerPieces player = board.returnCheckerPiece(6, 0, PlayerTeam.PLAYER1);
        int[] reqPos = {10,1};
        boolean result;
        
        try{
            result = gameLogic.isValidMove(reqPos , player, board,Direction.LEFT);   
        } catch(IllegalMove e){
            System.out.println(e.getMessage());
        }
        
        
        System.out.println();
        System.out.println("Below is the actual board rendered");
        System.out.println();
            
        board.rendarBoard();
    }
}
