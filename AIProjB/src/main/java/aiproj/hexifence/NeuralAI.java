package aiproj.hexifence;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class NeuralAI implements Player{
	/**
	 * Neural network to approximate q-value for each move
	 */
	private Map<Move, BasicNetwork> networks = new HashMap<Move, BasicNetwork>(9);
	
	/**
	 * Random generator
	 */
	private Random random = new Random();
	
	/**
	 * Parameters for exploration vs exploitation dilemma
	 */
	private double t, a = 1, b = 0.99, c = 0.002;
	//private double t, a = 1, b = 0.99, c = 0.002;
	
	/**
	 * Number of moves done in current game
	 */
	private int movesDone = 0;
	
	/**
	 * Number of training games played
	 */
	private int gamesPlayed = 0;
	
	/**
	 * Learning rate
	 */
	private double learningRate = 0.2;
	//private double learningRate = 0.1;
	private boolean explore = true;
	private boolean learn = true;
	
	private double qOutput;
	private Move selectedMove;
	
	private double[] state;
	private GameBoard gameBoard;
	private int pieceColor;
	private int oppPieceColor;
	
	public NeuralAI(int n) throws Exception {
		// empty game to get all possible actions
		GameBoard gameBoard = new GameBoard(n);
		List<Move> actions = gameBoard.getMoves(n);
		
		// for each action a neural network
		for (Move action : actions) {
			BasicNetwork network = new BasicNetwork();
			network.addLayer(new BasicLayer(null, true, 4*n-1));
			network.addLayer(new BasicLayer(new ActivationTANH(), true, 60));
			network.addLayer(new BasicLayer(new ActivationTANH(), true, 1));
			network.getStructure().finalizeStructure();
			network.reset();
			
			networks.put(action, network);
		}
		
		// Temperature value
		t = a * Math.pow(b, gamesPlayed);
	}
	
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
		if (movesDone > 0)
			observeResultingState();
		
		boolean exploreInMove = explore && learn;
		
		state = gameBoard.getStateBoard(pieceColor);
		ArrayList<Move> possible = gameBoard.getMoves(pieceColor);
		BasicNeuralData state = new BasicNeuralData(this.state);
		selectedMove = null;
		
		double divisor = 0.0;
		Map<Move, Double> qValues = new HashMap<Move, Double>();
		Double bestQValue = null;
		
		for (Move action : possible) {
			BasicNetwork network = networks.get(action);
			final MLData output = network.compute(state);
			double qValue = output.getData(0);
			qValues.put(action, qValue);
			
			if (!exploreInMove) {
				if (bestQValue == null || qValue > bestQValue) {
					bestQValue = qValue;
					selectedMove = action;
				}
			} else {
				divisor += Math.exp(qValue / t);
			}
		}
		
		if (exploreInMove) {
			double choice = random.nextDouble();
			double sum = 0;
			
			for (Move action : possible) {
				double p = Math.exp(qValues.get(action) / t) / divisor;
				
				sum += p;
				if (choice <= sum) {
					selectedMove = action;
					break;
				}
			}
		}
		
		qOutput = qValues.get(selectedMove);
		movesDone++;
		gameBoard.update(selectedMove);
		return selectedMove;
	}
	
	private void observeResultingState() {
		if (!learn)
			return;
		
		// reward received
		double reward = 0.0;
		if (this.gameBoard.totalMovesLeft == 0 || this.gameBoard.gameState == Piece.INVALID) {
			if (gameBoard.getWinner() == this.pieceColor)
				reward += 1;
			else
				reward -= 1;
		}

		// observe resulting state
		BasicNeuralData newState = new BasicNeuralData(gameBoard.getStateBoard(pieceColor));
		Double best = 0.0;
		ArrayList<Move> possible = gameBoard.getMoves(pieceColor);
		for (Move action : possible) {
			final MLData output = networks.get(action).compute(newState);
			
			if (best == null || output.getData(0) > best) {
				best = output.getData(0);
			}
		}
		
		// adjust the neural network
		double qTarget = (1 - learningRate) * qOutput + learningRate * (reward + best);
		
		NeuralDataSet trainingSet = new BasicNeuralDataSet(
			new double[][]{ state }, 
			new double[][] { {qTarget} }
		);
		final Train train = new Backpropagation(networks.get(selectedMove), trainingSet, 0.2, 0.9);
		//final Train train = new Backpropagation(networks.get(selectedMove), trainingSet);
		train.iteration();
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
			this.state = gameBoard.getStateBoard(pieceColor);
			return 0;
		}
		//return 1 if move is valid and a hexagon is captured by move m
		this.gameBoard.update(m);
		this.state = gameBoard.getStateBoard(pieceColor);
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

	public void setLearn(boolean learn) {
		this.learn = learn;
	}

}
