package aiproj.hexifence;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameBoard gameBoard = new GameBoard(3);
		System.out.println(gameBoard.checkCapture(new Move(2,3,Piece.BLUE)));
	}

}
