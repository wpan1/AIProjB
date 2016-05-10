package aiproj.hexifence;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		GameBoard gameBoard = new GameBoard(3);
		gameBoard.update(new Move(1,0,Piece.BLUE));
		System.out.println(gameBoard.checkValid(new Move(1,0,Piece.BLUE)));
		gameBoard.printBoard(System.out);
		System.out.println(gameBoard.checkCapture(new Move(1,0,Piece.BLUE)));
	}

}
