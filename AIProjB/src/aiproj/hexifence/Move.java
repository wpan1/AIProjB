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
	
	public Move(int col, int row, int p){
		this.Row = row;
		this.Col = col;
		this.P = p;
	}

	public Move() {
	}
		
	public void printMove(){
		System.out.println("Player " + P + ": (" + Row + "," + Col + ")");
	}
}
