package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TestAgent implements Player{
	public GameBoard gameBoard;
	int pieceColor;
	int oppPieceColor;
	
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
	public Move makeMove() {
		ArrayList<Move> possMoves = new ArrayList<Move>();
		for (ArrayList<Integer> move : gameBoard.getMoves()){
			for (Hexagon hex : gameBoard.hexagonMap.get(move)){
				if (hex.remainingEdges > 2){
					Move newmove = new Move(move.get(1), move.get(0), pieceColor);
					possMoves.add(newmove);
				}
			}
		}
		Random rand = new Random();
		
		if (possMoves.size() == 0){
			ArrayList<ArrayList<Integer>> moves = gameBoard.getMoves();
			HashMap<ArrayList<Hexagon>,ArrayList<ArrayList<Integer>>> links = new HashMap<ArrayList<Hexagon>,ArrayList<ArrayList<Integer>>>();
			ArrayList<Hexagon> tempLink = null;
			// Iterate through moves
			for (ArrayList<Integer> move : moves){
				// Iterate through hexagons mapping to move
				for (Hexagon hex : gameBoard.hexagonMap.get(move)){
					// Iterate through links
					linkloop : for (ArrayList<Hexagon> link : links.keySet()){
						// Iterate though hexagon in links
						for (Hexagon hex2 : link){
							if (hex.equals(hex2)){
								tempLink = link;
								break linkloop;
							}
						}
					}
				}
				if (tempLink != null){
					tempLink.removeAll(gameBoard.hexagonMap.get(move));
					tempLink.addAll(gameBoard.hexagonMap.get(move));
					links.get(tempLink).add(move);
					tempLink = null;
				}else{
					links.put(gameBoard.hexagonMap.get(move), moves);
				}
			}
			int minsize = Integer.MIN_VALUE;
			ArrayList<Hexagon> minhexList = null;
			for (ArrayList<Hexagon> hex : links.keySet()){
				if (hex.size() > minsize){
					minhexList = hex;
					minsize = hex.size();
				}
			}
			for (Hexagon hex : minhexList){
				Move mv = new Move (links.get(hex).get(0).get(1),
						links.get(hex).get(0).get(0),
						pieceColor);
			}
		}
		
		int randint = rand.nextInt(possMoves.size());
		gameBoard.update(possMoves.get(randint));
		return possMoves.get(randint);
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
				System.out.println(oppCount);
				System.out.println(ourCount);
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
