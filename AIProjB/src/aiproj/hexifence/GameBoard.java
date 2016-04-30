package aiproj.hexifence;

import java.io.PrintStream;
import java.util.ArrayList;

public class GameBoard {
	char[][] gameBoard;
	int N;
	int[][] hexagons;
	
	
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
		if (n == 2){
			hexagons = new int[][] {{0,0},{0,1},
									{1,0},{1,1},{1,2},
									{2,1},{2,2}};
		}
		else if (n == 3){
			hexagons = new int[][] {{0,0},{0,1},{0,2},
									{1,0},{1,1},{1,2},{1,3},
									{2,0},{2,1},{2,2},{2,3},{2,4},
									{3,1},{3,2},{3,3},{3,4},
									{4,2},{4,3},{4,4}};		
		}
	}
	
	/**
	 * Update the gameboard
	 * @param m Move
	 * @param p Piece color
	 */
	public void update(Move m, int p){
		if (p == Piece.BLUE){
			gameBoard[m.Col][m.Row] = 'B';
		}
		else{
			gameBoard[m.Col][m.Row] = 'R';
		}
	}
	
	/**
	 * Checks if move is valid in current GameBoard state
	 * @param m Move
	 * @return boolean value if valid
	 */
	public boolean checkValid(Move m){
		// Check if space is open
		if (gameBoard[m.Col][m.Row] == '+'){
			// If open, move is valid
			return true;
		}
		// If space is not open or taken, move is invalid
		return false;
	}
	
	/**
	 * Check if move captures a hexagon
	 * @param m Move
	 * @return boolean value if capturable
	 */
	public boolean checkCapture(Move m){
		// Get all possible hexagons caputed by this move
		ArrayList<int[]> hexagons = getHexagons(m);
		for (int[] hexagon : hexagons){
			// If hexagon is capturable, return true
			if (checkCapturableHexgon(hexagon)){
				return true;
			}
		}
		// Otherwise, hexagon/s at the Move m are not captured
		return false;
	}
	
	/**
	 * Check if hexagon is capturable
	 * @param int[] hexagon parameter for hexagon
	 * @return true if capturable
	 */
	private boolean checkCapturableHexgon(int[] hexagon){
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
		if (count == 6){
			return true;
		}
		System.out.println(count);
		// Otherwise, hexagon is not captured
		return false;
	}
	
	/**
	 * Get all possible hexagons capturable with Move m
	 * @param m Move
	 * @return array of all hexagons at Move m
	 */
	private ArrayList<int[]> getHexagons(Move m){
		ArrayList<int[]> retHexagons = new ArrayList();
		int col = m.Col, row = m.Row;
		// Right Top
		if (checkValidHexagon(row/2, (col-1)/2)){
			retHexagons.add(new int[]{row/2, (col-1)/2});
		}
		// Left Top
		if (checkValidHexagon(row/2, col/2)){
			retHexagons.add(new int[]{row/2, col/2});
		}
		// Right
		if (checkValidHexagon((row-1)/2, (col-2)/2)){
			retHexagons.add(new int[]{(row-1)/2, (col-2)/2});
		}
		// Left
		if (checkValidHexagon((row-1)/2, col/2)){
			retHexagons.add(new int[]{(row-1)/2, col/2});
		}
		// Bot Left
		if (checkValidHexagon((row-2)/2, (col-1)/2)){
			retHexagons.add(new int[]{(row-2)/2, (col-1)/2});
		}
		// Bot Right
		if (checkValidHexagon((row-2)/2, (col-2)/2)){
			retHexagons.add(new int[]{(row-2)/2, (col-2)/2});
		}
		// Convert to correct format
		return retHexagons;
	}

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
