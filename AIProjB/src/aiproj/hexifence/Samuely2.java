package aiproj.hexifence;

import java.io.PrintStream;

public class Samuely2 implements Player, Piece {
	char[][] gameBoard;
	int pieceColor;
	
	@Override
	public int init(int n, int p) {
		try{
			gameBoard = new char[4*n-1][4*n-1];
			this.pieceColor = p;
			return 0;
		}
		catch(Exception e){
			return 1;
		}
	}

	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int opponentMove(Move m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		// TODO Auto-generated method stub

	}

}