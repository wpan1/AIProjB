package aiproj.hexifence;

import java.util.ArrayList;

public class QLearn {
	public static void main(String[] args) throws Exception {
		QBenchmark benchmark = new QBenchmark(10000);
		
		NeuralAI p1 = new NeuralAI(2);
		BasicAgent p2 = new BasicAgent();
		
		p1.setLearn(false);
		System.out.println("Start score: " + benchmark.benchmark(p1));
		p1.setLearn(true);
		
		// training games
		for (int i = 1; i <= 1000000; i++) {
			QGame game = new QGame();
			ArrayList<Move> m = new ArrayList<Move>();
			game.runGame(m, p1, p2);
			
			if (i % 10000 == 0) {
				p1.setLearn(false);
				System.out.println("Benchmark after " + i + " moves: " + benchmark.benchmark(p1));
				p1.setLearn(true);
			}
		}
		
		p1.setLearn(false);
		System.out.println("End score: " + benchmark.benchmark(p1));
		p1.setLearn(true);
	}
}
