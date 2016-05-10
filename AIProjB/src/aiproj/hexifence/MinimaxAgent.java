package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MinimaxAgent implements Player{
	public GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	private static final int MINIMAX_DEPTH = 6;
	
	@Override
	public int init(int n, int p) {
		try{
			gameBoard = new GameBoard(n);
			this.pieceColor = p;
			if (p == Piece.BLUE){
				oppPieceColor = Piece.RED;
			}else{
				oppPieceColor = Piece.BLUE;
			}
			gameBoard.gameState = Piece.EMPTY;
			
			return 0;
		}
		catch(Exception e){
			return 1;
		}
	}

	@Override
	public Move makeMove(){
		// When to start using minimax
		if (gameBoard.totalMovesLeft > 12){
			// If not below totalMovesLeft, play a random move
			ArrayList<ArrayList<Integer>> moves = gameBoard.getMoves();
			int rand = (int) (Math.random()*moves.size());
			
			Move move = new Move(moves.get(rand).get(1),moves.get(rand).get(0),pieceColor);
			gameBoard.update(move);
			return move;
		}
		// Use minimax
		System.out.println("MINIMAX");
		int[] moveDet = minimax(MINIMAX_DEPTH, pieceColor, Integer.MIN_VALUE, Integer.MAX_VALUE);
		Move move = new Move(moveDet[2], moveDet[1], pieceColor);
		gameBoard.update(move);
		return move;
	}
	
	private int[] minimax(int depth, int currPieceColor, int alpha, int beta) {
		// We are maximising, while opponent is minimising
		int currScore;
	    int bestRow = -1;
	    int bestCol = -1;
	    
	    // Gameover or depth reached, evaluate score
		if (gameBoard.totalMovesLeft == 0 || depth == 0){
			currScore = evaluateBoard();
			return new int[]{currScore, bestRow, bestCol};
		}
		else{
			// Iterate through all moves
			for (ArrayList<Integer> move : gameBoard.getMoves()){
				// Update the board for the current move
				gameBoard.update(new Move(move.get(1),move.get(0), currPieceColor));
				if (currPieceColor == pieceColor){
					// Maximising score
					currScore = minimax(depth - 1, oppPieceColor, alpha, beta)[0];
					// Update score values
					if (currScore > alpha){
						alpha = currScore;
						bestRow = move.get(0);
						bestCol = move.get(1);
					}
				}else{
					// Minimising score
					currScore = minimax(depth - 1, pieceColor, alpha, beta)[0];
					// Update score values
					if (currScore < beta){
						beta = currScore;
						bestRow = move.get(0);
						bestCol = move.get(1);
					}
				}
				// Revert board back to original state
				gameBoard.remove(move);
	            // Stop
	            if (alpha >= beta) break;
			}
		}
		currScore = (currPieceColor == pieceColor) ? alpha : beta;
		// Return score values
		return new int[]{currScore, bestRow, bestCol};
	}
	
	private int evaluateBoard(){
		if (gameBoard.blueCap > gameBoard.redCap)
			return 1;
		return 0;
	}

	@Override
	public int opponentMove(Move m) {
		//Check if move is valid
		if (!gameBoard.checkValid(m)){
			this.gameBoard.gameState = Piece.INVALID;
			return -1;
		}
	
		//Check if move m captures any hexagons
		//return 0 if none captured
		if (gameBoard.checkCapture(m) == false){
			this.gameBoard.update(m);
			return 0;
		}
		//return 1 if move is valid and a hexagon is captured by move m
		this.gameBoard.update(m);
		return 1;
	}

	@Override
	public int getWinner() {
		if (this.gameBoard.totalMovesLeft == 0 || this.gameBoard.gameState == Piece.INVALID){
			//Perform end game checks only if there are no more moves to be played or
			//the game ended due to an invalid move
			int oppCount = 0;
			int ourCount = 0;
			if (pieceColor == Piece.BLUE){
				ourCount = gameBoard.blueCap;
				oppCount = gameBoard.redCap;
			}else{
				ourCount = gameBoard.redCap;
				oppCount = gameBoard.blueCap;
			}
			if (oppCount > ourCount){
				return this.oppPieceColor;
			}
			else if (oppCount == ourCount){
				return Piece.DEAD;
			}
			else{
				return this.pieceColor;
			}
		}
		//Game still in progress
		return Piece.EMPTY;
	}

	@Override
	public void printBoard(PrintStream output) {
		gameBoard.printBoard(output);
	}

}
