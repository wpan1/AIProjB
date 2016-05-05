package aiproj.hexifence;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveMapper {
	HashMap<String, Integer> mapper;
	HashMap<Integer, Integer> rotator;
	
	MoveMapper(int n){
		mapper = new HashMap<String, Integer>();
		int count = 0;
		if (n==2){
			try{
				GameBoard g = new GameBoard(2);
				for (int i = 0; i < g.gameBoard.length; i++){
					for (int j = 0; j < g.gameBoard[i].length; j++){
						if (g.gameBoard[i][j] == '+'){
							String str = new String(i + " " + j);
							mapper.put(str, ++count);
						}
					}
				}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.exit(-1);
			}

			//Initialise rotate map
			rotator = new HashMap<Integer, Integer>();
			rotator.put(4, 1);
			rotator.put(7, 2);
			rotator.put(13, 3);
			rotator.put(17, 4);
			rotator.put(3, 5);
			rotator.put(12, 6);
			rotator.put(23, 7);
			rotator.put(2, 8);
			rotator.put(6, 9);
			rotator.put(11, 10);
			rotator.put(16, 11);
			rotator.put(22, 12);
			rotator.put(26, 13);
			rotator.put(1, 14);
			rotator.put(10, 15);
			rotator.put(21, 16);
			rotator.put(30, 17);
			rotator.put(5, 18);
			rotator.put(9, 19);
			rotator.put(15, 20);
			rotator.put(20, 21);
			rotator.put(25, 22);
			rotator.put(29, 23);
			rotator.put(8, 24);
			rotator.put(19, 25);
			rotator.put(28, 26);
			rotator.put(14, 27);
			rotator.put(18, 28);
			rotator.put(24, 29);
			rotator.put(27, 30);
			
		}
		else if (n==3){
			//implement later
		}
	}
	
	private int getLineNumber(Move m){
		String str = new String(m.Row + " " + m.Col);
		return mapper.get(str);
	}
	
	public int[] convertToLines(ArrayList<Move> moveSet){
		int[] lines = new int[30];
		int count = 0;
		for(Move m : moveSet){
			lines[count++] = getLineNumber(m);
		}
		
		return lines;
	}
	
//	public int[] rotateVector(int[] lines){
//		int[] newLine = new int[30];
//		for (int i = 0; i < lines.length; i++){
//			newLine[i] = rotator.get(lines[i]);
//		}
//		
//		return newLine;
//	}
	
}
