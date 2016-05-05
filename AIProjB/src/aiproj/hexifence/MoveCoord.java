package aiproj.hexifence;

public class MoveCoord {
	public int Row;
	public int Col;	
	
	MoveCoord(){
	}
	
	MoveCoord(int row, int col){
		this.Row = row;
		this.Col = col;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoveCoord other = (MoveCoord) obj;
		if (Col != other.Col)
			return false;
		if (Row != other.Row)
			return false;
		return true;
	}
	
}
