/*
 * Will Ernst & Ghassan Gedeon-Achi
 * All Group members were present and contributing during all work on this project
 * I have neither given nor received any unauthorized aid on this assignment
 */


import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;

/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightsPlayer {

	static Scanner scan = new Scanner(System.in);
	static int size=3; //size=3 for 8-Puzzle
	static int numiterations = 10000;
	static int curnumnodes; //number of nodes for the specific run
	static int curnummoves; //number of moves for specific run
	static int numnodes; //number of nodes generated
	static int nummoves; //number of moves required to reach goal
	
	
	public static void main(String[] args)
	{	
		int boardchoice = getBoardChoice();
		int algchoice = getAlgChoice();
			
		int numsolutions = 0;
		
		Node initNode;

		if(boardchoice==0)
			numiterations = 1;

		for(int i=0; i<numiterations; i++){
		
			if(boardchoice==0)
				initNode = getUserBoard();
			else
				initNode = generateInitialState();//create the random board for a new puzzle
			
			boolean result=false; //whether the algorithm returns a solution
			
			switch (algchoice){
				case 0: 
					result = runBFS(initNode); //BFS
					break;
				case 1: 
					result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
					break;
				case 2: 
					result = runAStar(initNode, 1); //A* with your new heuristic
					break;
			}
			
			
			//if the search returns a solution
			if(result){
				
				numsolutions++;
				
				
				System.out.println("Number of nodes generated to solve: " + curnumnodes);
				System.out.println("Number of moves to solve: " + curnummoves);			
				System.out.println("Number of solutions so far: " + numsolutions);
				System.out.println("_______");		
				
				numnodes += curnumnodes;
				nummoves += curnummoves;
			}
			else
				System.out.print(".");
			
		}//for

		
		
		System.out.println();
		System.out.println("Number of iterations: " +numiterations);
		
		if(numsolutions > 0){
			System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
			System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
		}
		else
			System.out.println("No solutions in "+numiterations+" iterations.");
		
	}
	
	
	public static int getBoardChoice()
	{
		
		System.out.println("single(0) or multiple boards(1)");
		int choice = Integer.parseInt(scan.nextLine());
		
		return choice;
	}
	
	public static int getAlgChoice()
	{
		
		System.out.println("BFS(0) or A* Manhattan Distance(1) or A* Manhattan Linear Conflict (2)");
		int choice = Integer.parseInt(scan.nextLine());
		
		return choice;
	}

	
	public static Node getUserBoard()
	{
		
		System.out.println("Enter board: ex. 012345678");
		String stbd = scan.nextLine();
		
		int[][] board = new int[size][size];
		
		int k=0;
		
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(stbd.charAt(k));
				board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
				k++;
			}
		}
		
		
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(board[i][j]);
			}
			System.out.println();
		}
		
		
		Node newNode = new Node(null,0, board);

		return newNode;
		
		
	}

    
	
	
	/**
	 * Generates a new Node with the initial board
	 */
	public static Node generateInitialState()
	{
		int[][] board = getNewBoard();
		
		Node newNode = new Node(null,0, board);

		return newNode;
	}
	
	
	/**
	 * Creates a randomly filled board with numbers from 0 to 8. 
	 * The '0' represents the empty tile.
	 */
	public static int[][] getNewBoard()
	{
		
		int[][] brd = new int[size][size];
		Random gen = new Random();
		int[] generated = new int[size*size];
		for(int i=0; i<generated.length; i++)
			generated[i] = -1;
		
		int count = 0;
		
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				int num = gen.nextInt(size*size);
				
				while(contains(generated, num)){
					num = gen.nextInt(size*size);
				}
				
				generated[count] = num;
				count++;
				brd[i][j] = num;
			}
		}
		
		/*
		//Case 1: 12 moves
		brd[0][0] = 1;
		brd[0][1] = 3;
		brd[0][2] = 8;
		
		brd[1][0] = 7;
		brd[1][1] = 4;
		brd[1][2] = 2;
		
		brd[2][0] = 0;
		brd[2][1] = 6;
		brd[2][2] = 5;
		*/
		
		return brd;
		
	}
	
	/**
	 * Helper method for getNewBoard()
	 */
	public static boolean contains(int[] array, int x)
	{ 
		int i=0;
		while(i < array.length){
			if(array[i]==x)
				return true;
			i++;
		}
		return false;
	}
	
	
	/**
	 * TO DO:
     * Prints out all the steps of the puzzle solution and sets the number of moves used to solve this board.
     */
    public static void printSolution(Node node) {
    	
    	curnummoves = 0;
    	ArrayList<Node> Solution = new ArrayList<Node>();
    	Solution.add(node);
    	// Base case where their is only one node in solution.
    	if (node.getparent() == null){
    		node.print(node);
    	}else{
    		//Add node to queue, then add parent of node, and parent of parent of node .... (until you reach initNode)
    		do {
    			node = node.getparent();
    			Solution.add(node);
    			curnummoves++;
    		}while(node.getparent() != null);
    		
    		//Reverse collection and print the solution from Initial to Goal. 
    		Collections.reverse(Solution);
    		System.out.println("\n");
    		for (Node n : Solution) {
    			n.print(n);
    		}
    	}

    }
	
	
	
	
	/**
	 * TO DO:
	 * Runs Breadth First Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 */
	public static boolean runBFS(Node initNode)
	{
		curnumnodes = 0; //using these to keep track of num nodes for current solution (helps with output formating
		
		Queue<Node> Frontier = new LinkedList<Node>();
		ArrayList<Node> Explored = new ArrayList<Node>();
		
		//Add initial node and set max depth
		Frontier.add(initNode); 
		int maxDepth = 13;
		
		//while frontier is not empty, remove the oldest node in it 
		while(!Frontier.isEmpty()) {
			Node current = Frontier.remove();
			Explored.add(current); //add node you are about to expand to explored.
			curnumnodes++; 
			
			// Check to see if you have exceed max depth, return false if you have.
			if(current.getdepth() >= maxDepth) {
				return false;
			}
			// Check to see if you have reached goal, print solution if that is the case
			if(current.isGoal()) {
				printSolution(current);
				return true;
			}else{
				ArrayList<int[][]> children = current.expand(); //Expand all possible children
				for(int[][] child : children) { //check to see if they are already in Frontier, 
					boolean match = true;
					for(Node n : Frontier) {
						if (n.isSameBoard(child))
							match = false;
					}
					for(Node n : Explored){ //Check to see if they are already in Explored
						if(n.isSameBoard(child)){
							match = false;
						}
					}
					if(match == true) { //if node is neither explored or in frontier, add it to frontier
						Node newNode = new Node(current, current.getdepth()+1, child);
						Frontier.add(newNode);
					}
				}
			}
		}
		//If frontier is empty, than all possible combination have been exhausted and depth is < 13. NO solution so return false. 
		return true;
		
	}//BFS
	
	
	
	/***************************A* Code Starts Here ***************************/
	
	/**
	 * TO DO:
	 * Runs A* Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
	 */
	public static boolean runAStar(Node initNode, int heuristic)
	{
		curnumnodes = 0; //Same as BFS, this is used to format output better.
		//Using Priority Queue, with a comparator which will return the lowest f(n) heuristic node in Frontier. 
		PriorityQueue<Node> Frontier = new PriorityQueue<Node>(10000, new Comparator<Node>() {
			public int compare(Node n1, Node n2){
				if (n1.getfvalue() < n2.getfvalue()) return -1;
				if (n1.getfvalue() > n2.getfvalue()) return 1;
				return 0;
			}
		});
		// Using a queue for explored
		Queue<Node> Explored = new LinkedList<Node>();
		int maxDepth = 13; //Setting Max Depth
		
		initNode.setgvalue(0); //set g(n) value for initNode
		
		// Depending on user input either evaluate with the Manhattan Heuristic (0) or the Linear Conflict And Manhattan Heuristic (1)
		if(heuristic == 0) {
			initNode.sethvalue(initNode.evaluateHeuristic());
		}else if(heuristic == 1) {
			initNode.sethvalue(initNode.LinearConflictAndManhattan());
		}
		Frontier.add(initNode);
		
		//While Frontier is not empty, keep removing/expanding the node with lowest heuristic value.
		while(!Frontier.isEmpty()) {
			Node current = Frontier.remove();
			Explored.add(current); //Add current to explored.
			curnumnodes++;
			
			//Check for exceeding max depth, return false if that is the case
			if(current.getdepth() >= maxDepth) {
				return false;
			}
			
			//check for Goal Being Reached. If so, print solution and return true. 
			if(current.isGoal()) {
				printSolution(current);
				System.out.println("\n"); // for output readability 
				return true;
			}else{
				//expand currents children, check if they are in explored don't do anything.
				ArrayList<int[][]> children = current.expand(); 
				for(int[][] child : children) {
					boolean match = true;
					for(Node n : Explored){
						if(n.isSameBoard(child)){
							match = false;
						}
					}
					if(match == true) {
						//If they are not in explored, create a new node with that board state and evaluate its h(n) heuristic with proper alg.
						Node newNode = new Node(current, current.getdepth()+1, child);
						if(heuristic == 0) {
							newNode.sethvalue(newNode.evaluateHeuristic());
						}else {
							newNode.sethvalue(newNode.LinearConflictAndManhattan());
						}
						newNode.setgvalue(current.getgvalue()+1); //g(n) is equal to that of the parent + 1. 
						boolean inFrontier = false;
						for(Node n : Frontier) { //check to see BoardState is in Frontier, if so check f(n) and update g(n) and h(n) if newNode has lower f(n)
							if(newNode.isSameBoard(n.board)) {
								inFrontier = true;
								if(newNode.getfvalue() < n.getfvalue()) {
									n.setgvalue(newNode.getgvalue());
									n.sethvalue(newNode.gethvalue());
								}
							}
						} //if node is not in frontier than add it to frontier. 
						if (inFrontier == false) {
							Frontier.add(newNode);
						}
					}
				}
			}
		}
		//If frontier is empty, than all possible combination have been exhausted and depth is < 13. NO solution so return false. 
		return false;
	}//A* Star
	
}