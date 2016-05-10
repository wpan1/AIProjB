package aiproj.hexifence;

public class Hexagon{
	
	int remainingEdges;
	// Not needed, will add if necessary
	int x;
	int y;
	int capturedBy = -1;
	
	public Hexagon(int x, int y){
		this.x = x;
		this.y = y;
		this.remainingEdges = 6;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!Hexagon.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final Hexagon other = (Hexagon) obj;
	    if (this.x != other.x) {
	        return false;
	    }
	    if (this.y != other.y){
	    	return false;
	    }
	    return true;
	}
}
