package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

	char[][] gameBoard;
	int N;
	int[][] hexagons;
	HashMap<int[], Integer> capturedMap;
	int totalMovesLeft;
	int gameState;
	
	
	//Constructor
	public GameBoard(int n) throws Exception{
		this.N = n;
		generateHexagons(n);
		
		if (n == 3){
			gameBoard = new char[][]{
									 {'+', '+', '+', '+', '+', '+', '-', '-', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '-', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '+', '+', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '+', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '+', '+', '+', '+', '-'},
									 {'+', '-', '+', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '+', '+', '+', '+', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '+', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '+', '+', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '-', '-', '+', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '-', '-', '+', '+', '+', '+', '+', '+'},
									};
									
			this.totalMovesLeft = 72;
			
			int count = 0;
			for (char[] c : gameBoard){
				for (char d : c){
					if (d == '+'){
						count++;
					}
				}
			}
			System.out.println(count);
		}
		else if (n == 2){
			gameBoard = new char[][]{
									 {'+', '+', '+', '+', '-', '-', '-'},
									 {'+', '-', '+', '-', '+', '-', '-'},
									 {'+', '+', '+', '+', '+', '+', '-'},
									 {'+', '-', '+', '-', '+', '-', '+'},
									 {'-', '+', '+', '+', '+', '+', '+'},
									 {'-', '-', '+', '-', '+', '-', '+'},
									 {'-', '-', '-', '+', '+', '+', '+'},
									};
			this.totalMovesLeft = 30;
			
			int count = 0;
			for (char[] c : gameBoard){
				for (char d : c){
					if (d == '+'){
						count++;
					}
				}
			}
			System.out.println(count);
		}
		else{
			throw new Exception();
		}
	}
	
	public void printBoard(PrintStream output){
		for (char[] row : gameBoard){
			for (char column : row){
				output.print(column + " ");
			}
			output.print('\n');
		}
	}
	
	private void generateHexagons(int n){
		this.capturedMap = new HashMap<int[], Integer>();
		
		if (n == 2){
			hexagons = new int[][] {{0,0},{0,1},
									{1,0},{1,1},{1,2},
									{2,1},{2,2}};
			
			//create a map to track which hexagons have been captured
			for (int[] hexagon : hexagons){
				this.capturedMap.put(hexagon, null);
			}
		}
		else if (n == 3){
			hexagons = new int[][] {{0,0},{0,1},{0,2},
									{1,0},{1,1},{1,2},{1,3},
									{2,0},{2,1},{2,2},{2,3},{2,4},
									{3,1},{3,2},{3,3},{3,4},
									{4,2},{4,3},{4,4}};
									
			//create a map to track which hexagons have been captured						
			for (int[] hexagon : hexagons){
				this.capturedMap.put(hexagon, null);
			}
		}
	}
	
	/**
	 * Update the gameboard
	 * @param m Move
	 * @param p Piece color
	 */
	public void update(Move m){
		if (this.totalMovesLeft <= 0){
			return;
		}
		
		if (m.P == Piece.BLUE){
			gameBoard[m.Row][m.Col] = 'B';
		}
		else{
			gameBoard[m.Row][m.Col] = 'R';
		}
		
		this.totalMovesLeft--;
		
	}
	
	/**
	 * Checks if move is valid in current GameBoard state
	 * @param m Move
	 * @return boolean value if valid
	 */
	public boolean checkValid(Move m){
		// Check if space is open
		if (gameBoard[m.Row][m.Col] == '+'){
			// If open, move is valid
			return true;
		}
		// If space is not open or taken, move is invalid
		return false;
	}
	
	/**
	 * Check if move captures a hexagon
	 * @param m Move
	 * @return boolean true if capturable
	 */
	public boolean checkCapture(Move m){
		// Get all possible hexagons caputed by this move
		ArrayList<int[]> hexagons = getHexagons(m);
		boolean flag = false;
		for (int[] hexagon : hexagons){
			// If hexagon is capturable, return true
			if (checkCapturableHexagon(hexagon)){
				flag = true;
				if (hexagon[0] == 0 && hexagon[1] == 0) gameBoard[1][1] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 0 && hexagon[1] == 1) gameBoard[1][3] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 1 && hexagon[1] == 0) gameBoard[3][1] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 1 && hexagon[1] == 1) gameBoard[3][3] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 1 && hexagon[1] == 2) gameBoard[3][5] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 2 && hexagon[1] == 1) gameBoard[5][3] = (m.P == Piece.BLUE) ? 'b' : 'r';
				if (hexagon[0] == 2 && hexagon[1] == 2) gameBoard[5][5] = (m.P == Piece.BLUE) ? 'b' : 'r';
				
				this.capturedMap.put(hexagon, m.P);
			}
		}
		//a hexagon was captured
		if (flag) return true;
		// Otherwise, hexagon/s at the Move m are not captured
		return false;
	}
	
//	//Method below assumes the move to be made is valid
//	private boolean checkCapturableHexagon(int[] hexagon){
//		int x = hexagon[0], y = hexagon[1];
//		int count = 0;
//		
//		//Right top
//		if (gameBoard[2*x][2*y+1] != '+'){
//			count++;
//		}
//		// Left top
//		if (gameBoard[2*x][2*y] != '+'){
//			count ++;
//		}
//		// Right 
//		if (gameBoard[2*x+1][2*y+2] != '+'){
//			count ++;
//		}
//		// Left
//		if (gameBoard[2*x+1][2*y] != '+'){
//			count ++;
//		}
//		// Left Bottom
//		if (gameBoard[2*x+2][2*y+1] != '+'){
//			count ++;
//		}
//		// Right Bottom
//		if (gameBoard[2*x+2][2*y+2] != '+'){
//			count ++;
//		}
//		
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Check if hexagon is capturable. Called after latest move added
	 * @param int[] hexagon parameter for hexagon
	 * @return true if capturable
	 */

	private boolean checkCapturableHexagon(int[] hexagon){
		int x = hexagon[0], y = hexagon[1];
		// Count of surrounding colored edges
		int count = 0;
		// Right top
		if (gameBoard[2*x][2*y+1] != '+'){
			count += 1;
		}
		// Left top
		if (gameBoard[2*x][2*y] != '+'){
			count += 1;
		}
		// Right 
		if (gameBoard[2*x+1][2*y+2] != '+'){
			count += 1;
		}
		// Left
		if (gameBoard[2*x+1][2*y] != '+'){
			count += 1;
		}
		// Left Bottom
		if (gameBoard[2*x+2][2*y+1] != '+'){
			count += 1;
		}
		// Right Bottom
		if (gameBoard[2*x+2][2*y+2] != '+'){
			count += 1;
		}
		// If 6 edges are surrounding, hexagon is captured
		if (count == 5){
			return true;
		}
		//System.out.println("Count" + count);
		// Otherwise, hexagon is not captured
		return false;
	}
	
	
	private ArrayList<int[]> getHexagons(Move m){
		ArrayList<int[]> capturableHexagons = new ArrayList<int[]>();
		int x = m.Row;
		int y = m.Col;
		for (int[] hexagon : hexagons){
			//Check top left
			if (hexagon[0]*2 == x && hexagon[1]*2 == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
			//Check top right
			if (hexagon[0]*2 == x && (hexagon[1]*2 +1) == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
			//Check left
			if ((hexagon[0]*2 + 1) == x && hexagon[1]*2 == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
			//Check right
			if ((hexagon[0]*2 + 1) == x && (hexagon[1]*2 + 2) == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
			//Check bottom left
			if ((hexagon[0]*2 + 2) == x && (hexagon[1]*2 + 1) == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
			//Check bottom right
			if ((hexagon[0]*2 + 2) == x && (hexagon[1]*2 + 2) == y){
				if (!capturableHexagons.contains(hexagon)) capturableHexagons.add(hexagon);
			}
		}
		
		if (capturableHexagons.size() > 2){
			System.out.println("getHexagons fked up.");
		}
//		System.out.printf("(%d, %d): num hexagons: %d\n", x, y, capturableHexagons.size());
		return capturableHexagons;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	Doesn't work, some of the math fks up if you do it this way
//	/**
//	 * Get all possible hexagons capturable with Move m
//	 * @param m Move
//	 * @return array of all hexagons at Move m
//	 */
//	private ArrayList<int[]> getHexagons(Move m){
//		ArrayList<int[]> retHexagons = new ArrayList<int[]>();
//		int col = m.Col, row = m.Row;
//		// Right Top
//		if (checkValidHexagon(row/2, (col-1)/2)){
//			int[] o = new int[]{row/2, (col-1)/2};
//			if (!(retHexagons.contains(o))){
//				retHexagons.add(o);
//			}
//		}
//		// Left Top
//		if (checkValidHexagon(row/2, col/2)){
//			int[] o = new int[]{row/2, col/2};
//			if (!retHexagons.contains(o)) retHexagons.add(o);
//		}
//		// Right
//		if (checkValidHexagon((row-1)/2, (col-2)/2)){
//			int[] o = new int[]{(row-1)/2, (col-2)/2};
//			if (!retHexagons.contains(o)) retHexagons.add(o);
//		}
//		// Left
//		if (checkValidHexagon((row-1)/2, col/2)){
//			int[] o = new int[]{(row-1)/2, col/2};
//			if (!retHexagons.contains(o)) retHexagons.add(o);
//		}
//		// Bot Left
//		if (checkValidHexagon((row-2)/2, (col-1)/2)){
//			int[] o = new int[]{(row-2)/2, (col-1)/2};
//			if (!retHexagons.contains(o)) retHexagons.add(o);
//		}
//		// Bot Right
//		if (checkValidHexagon((row-2)/2, (col-2)/2)){
//			retHexagons.add(new int[]{(row-2)/2, (col-2)/2});
//		}
//		// Convert to correct format
//		for (int[] h: retHexagons){
//			System.out.println(h[0] + ", "  + h[1]);
//		}
//		return retHexagons;
//	}

	/**
	 * Check if hexagon is valid
	 * @param row Row value
	 * @param col Column value
	 * @return
	 */
	private boolean checkValidHexagon(int row, int col){
		for (int[] hexagon : hexagons){
			if (hexagon[0] == row && hexagon[1] == col){
				return true;
			}
		}
		return false;
	}
	
	
	
}
