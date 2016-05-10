package aiproj.hexifence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QBenchmark {
	
	int gamesToPlay;
	
	public QBenchmark(int gamesToPlay){
		this.gamesToPlay = gamesToPlay;
	}

	public float benchmark(NeuralAI p1){
		Samuely2 randagent = new Samuely2();
		QGame newGen = new QGame();
		int gamesLeft = gamesToPlay;
		float gamesWon = 0;
		
		while (gamesLeft > 0){
			
			ArrayList<Move> m = new ArrayList<Move>();
			//run game
			char winner = newGen.runGame(m, p1, randagent);
			if (winner == 'T'){
				gamesWon += 1;
			}
			//get equiv rotations of game
			gamesLeft--;
		}
		return gamesWon/gamesToPlay;
	}
	
}
