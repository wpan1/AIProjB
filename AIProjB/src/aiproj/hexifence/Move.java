package aiproj.hexifence;
/*
 *   Move:
 *      Define possible hexifence move
 *      
 *   @author lrashidi
 *   
 */
 
public class Move{
	
	public int P;
	public int Row;
	public int Col;	
	
	public Move(int row, int col, int p){
		this.Row = row;
		this.Col = col;
		this.P = p;
	}

	public Move() {
		// TODO Auto-generated constructor stub
	}
		
}
