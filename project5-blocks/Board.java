import java.util.*;
import java.io.*;
import java.math.BigInteger;

//instance of class stores its size and its initial and final configurations. methods are provided for manipulating board states (represented as arrays of bitboards (BigInteger[]'s)) in this context
public class Board {
    private int rows, cols;
    private int num_blocks = 0;
    private int num_final_blocks = 0;

    //reference initial and final (desired) blocks given
    private BigInteger[] initial_blocks = new BigInteger[MAX_BLOCKS];
    private BigInteger[] final_blocks = new BigInteger[MAX_BLOCKS];
    
    private BigInteger[] edges = new BigInteger[4];
    static final int DOWN = 0;
    static final int LEFT = 1;
    static final int UP = 2;
    static final int RIGHT = 3;
    static final int MAX_BLOCKS = 256;
    //represents invalid state of board, used as return value;
    static final BigInteger[] INVALID = new BigInteger[1];

    //check if (row, col) is on this board
    private boolean inRange(int row, int col) {
	return (0 <= row) && (row < rows) &&
	    (0 <= col) && (col < cols);
    }
	    
    //convert block data to a corresponding bitBoard
    private BigInteger toBits(int length, int width, int row, int col) {
	assert inRange(row, col) &&
	    inRange(row + length, col + width);

	BigInteger bits = BigInteger.ZERO;
	for (int r = row; r < row + length; r++)
	    for (int c = col; c < col + width; c++)
		bits = bits.add((BigInteger.ONE).shiftLeft(c + r * cols));

	return bits;
    }

    //check if given bitboard satisfies the final conditions
    //IN NEED OF OPTIMIZATION
    private boolean isFinal(BigInteger[] blocks) {
	int num_found = 0;
	for (int i = 0; i < num_blocks; i++) {
	    for (int f = 0; f < num_final_blocks; f++) {
		if ((blocks[i]).equals(final_blocks[f])) {
		    num_found++;
		}
	    }
	}
	return (num_found == num_final_blocks);
    }

    private boolean isZero(BigInteger bits) {
	return bits.equals(BigInteger.ZERO);
    }
    
    //returns a bitboard representing the given shift on blocks, or INVALID
    //or INVALID if no such move is possible
    private BigInteger[] shift(BigInteger[] blocks, int block, int direction) {
	
	//check if block is already at edge
	if (!isZero((blocks[block]).and(edges[direction])))
	    return INVALID;

	BigInteger shiftedBlock = BigInteger.ZERO;
	switch (direction) {
	case DOWN:
	    shiftedBlock = blocks[block].shiftRight(cols);
	    break;
	case LEFT:
	    shiftedBlock = blocks[block].shiftLeft(1);
	    break;
	case UP:
	    shiftedBlock = blocks[block].shiftLeft(cols);
	    break;
	case RIGHT:
	    shiftedBlock = blocks[block].shiftRight(1);
	    break;
	}

	//generate set of all cells occupied by other blocks
	BigInteger collisions = BigInteger.ZERO;
	for (int i = 0; i < block; i++)
	    collisions = collisions.or(blocks[i]);
	for (int i = block + 1; i < num_blocks; i++)
	    collisions  = collisions.or(blocks[i]);

	//check for collisions with other blocks
	if (isZero(collisions.and(shiftedBlock))) {
	    BigInteger[] shifted = new BigInteger[num_blocks];
	    System.arraycopy(blocks, 0, shifted, 0, num_blocks);
	    shifted[block] = shiftedBlock;
	    return shifted;
	} else {
	    return INVALID;
	}
    }

    class State {
	BigInteger[] blocks;
	State previous;
	String instruction;

	public State(BigInteger[] blocks, State previous, String instruction) {
	    this.blocks = blocks;
	    this.previous = previous;
	    this.instruction = instruction;
	}
    }
    
    public void solveBreadthFirst() {
	Set<Integer> tried = new HashSet<>();
	Queue<State> frontier = new LinkedList<>();

	frontier.add(new State(initial_blocks, null, null));

	State winner = null;

	while (frontier.peek() != null) {
	    State state = frontier.remove();

	    //check if state is invalid
	    if (state.blocks == INVALID) continue;

	    //check if state has already been checked
	    int hash = Arrays.hashCode(Arrays.copyOfRange(state.blocks, 0, num_blocks));
	    if (tried.contains(hash)) continue;
	    tried.add(hash);

	    //System.out.println("Testing blocks:\n" + blocksToString(state.blocks));

	    //check if termination condition reached
	    if (isFinal(state.blocks)) {
		winner = state;
		break;
	    }

	    for (int block = 0; block < num_blocks; block++) {
		//DOWN
		frontier.add(new State(shift(state.blocks, block, DOWN), state, (bitsToRow(state.blocks[block]) + " " + bitsToCol(state.blocks[block]) + " " + (bitsToRow(state.blocks[block]) - 1) + " " + bitsToCol(state.blocks[block]))));

		//LEFT
		frontier.add(new State(shift(state.blocks, block, LEFT), state, (bitsToRow(state.blocks[block]) + " " + bitsToCol(state.blocks[block]) + " " + bitsToRow(state.blocks[block]) + " " + (bitsToCol(state.blocks[block]) + 1) )));

		//UP
		frontier.add(new State(shift(state.blocks, block, UP), state, (bitsToRow(state.blocks[block]) + " " + bitsToCol(state.blocks[block]) + " " + (bitsToRow(state.blocks[block]) + 1) + " " + bitsToCol(state.blocks[block]))));

		//RIGHT
		frontier.add(new State(shift(state.blocks, block, RIGHT), state, (bitsToRow(state.blocks[block]) + " " + bitsToCol(state.blocks[block]) + " " + bitsToRow(state.blocks[block]) + " " + (bitsToCol(state.blocks[block]) - 1))));
	    }
	}

	if (winner == null) {
	    exit("no solution found");
	} else {
	    LinkedList<String> instructions = new LinkedList<>();
	    for (; winner.instruction != null; winner = winner.previous)
		instructions.addFirst(winner.instruction);
	    for (String instruction: instructions)
		System.out.println(instruction);
	}
    }


    //attempts to solve this board, starting from the initial position and seeking satisfaction of isFinal
    public void solve() {
	Set<Integer> tried = new HashSet<>();
	LinkedList<String> instructions = new LinkedList<>();
	
	if (tryShifting(initial_blocks, tried, instructions)) {
	    for (String instruction: instructions)
		System.out.println(instruction);
	} else {
	    exit("no solution found");
	}
    }

    //return the row and column of the bottom lefthand corner of the given bitboard's represented block
    private int bitsToRow(BigInteger bits) {
	return bits.getLowestSetBit() / cols;
    }

    private int bitsToCol(BigInteger bits) {
	return bits.getLowestSetBit() % cols;
    }
		

    
    //can isFinal be reached from the given set of blocks? tries all possible shifts from this position (and checks this position itself) to find out!
    private boolean tryShifting(BigInteger[] blocks, Set<Integer> tried, LinkedList<String> instructions) {
	//check if this is not a valid state
	if (blocks == INVALID)
	    return false;

	int hash = Arrays.hashCode(Arrays.copyOfRange(blocks, 0, num_blocks));
	//check if already checked blocks
	if (tried.contains(hash))
	    return false;
	tried.add(hash);

	//check if blocks meet final conditions
	if (isFinal(blocks))
	    return true;

	for (int block = 0; block < num_blocks; block++) {
	    if (tryShifting(shift(blocks, block, DOWN), tried, instructions)) {
		instructions.addFirst(bitsToRow(blocks[block]) + " " + bitsToCol(blocks[block]) + " " + (bitsToRow(blocks[block]) - 1) + " " + bitsToCol(blocks[block]));
		return true;
	    }
	    if (tryShifting(shift(blocks, block, RIGHT), tried, instructions)) {
		instructions.addFirst(bitsToRow(blocks[block]) + " " + bitsToCol(blocks[block]) + " " + bitsToRow(blocks[block]) + " " + (bitsToCol(blocks[block]) - 1));
		return true;
	    }
	    if (tryShifting(shift(blocks, block, UP), tried, instructions)) {
		instructions.addFirst(bitsToRow(blocks[block]) + " " + bitsToCol(blocks[block]) + " " + (bitsToRow(blocks[block]) + 1) + " " + bitsToCol(blocks[block]));
		return true;
	    }
	    if (tryShifting(shift(blocks, block, LEFT), tried, instructions)) {
		instructions.addFirst(bitsToRow(blocks[block]) + " " + bitsToCol(blocks[block]) + " " + bitsToRow(blocks[block]) + " " + (bitsToCol(blocks[block]) + 1));
		return true;
	    }
	}
	return false;
    }

    public Board(String initialFile, String finalFile) {

	//set row, col, and initial blocks from initialFile
	try {

	    Scanner scanner = new Scanner(new File(initialFile));
	    //read first two ints
	    rows = scanner.nextInt();
	    cols = scanner.nextInt();

	    while (scanner.hasNextInt()) {
		if (num_blocks == MAX_BLOCKS)
		    exit("Maximum " + MAX_BLOCKS + " blocks exceeded");
		//read four ints at a time, exceptions caught below
		initial_blocks[num_blocks++] = toBits(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
	    }

	    if (num_blocks == 0)
		exit("Input board contains no blocks");

	    
	    scanner.close();
	
	} catch (NoSuchElementException e) {
	    exit("Input file not in correct format");
	} catch (FileNotFoundException e) {
	    exit("Input file does not exist");
	}

	//set final blocks from finalFile

	try {

	    Scanner scanner = new Scanner(new File(finalFile));

	    while(scanner.hasNextInt()) {
		//read four ints at a time, exceptions caught below, as above
		final_blocks[num_final_blocks++] = toBits(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
	    }
	} catch (NoSuchElementException e) {
	    exit("Final file not in correct format");
	} catch (FileNotFoundException e) {
	    exit("Final file does not exist");
	}
	    

	//set edges
	edges[DOWN] = toBits(1, cols, 0, 0);
	edges[LEFT] = toBits(rows, 1, 0, cols-1);
	edges[UP] = toBits(1, cols, rows-1, 0);
	edges[RIGHT] = toBits(rows, 1, 0, 0);
    }

    private void exit(String why) {
	System.out.println(why);
	System.exit(1);
    }

    private void check(int num) {
	System.out.println("Check reached: " + num);
    }

    public String blocksToString(BigInteger[] blocks) {
	if (blocks == INVALID)
	    return "INVALID";
	
	String out = "";
	String s;
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {
		s = "_";
		for (int i = 0; i < num_blocks; i++)
		    if (blocks[i] != null && !isZero( (blocks[i]).and(toBits(1, 1, r, c)) ))
			s = "" + i;
		out += s;
	    }
	    out += "\n";
	}
	return out;
    }

    public String bitsToString(BigInteger bits) {
	String out = "";
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {
		out += isZero( bits.and(toBits(1, 1, r, c)) )? 0: 1;
	    }
	    out += "\n";
	}
	return out;
    }

    public static void main(String[] args) {
	if (args[0].equals("-ooptions")) {
	    System.out.println("Currently available debugging options:\n[-ooptions: lists all debugging options");
	    return;
	}

	
	Board b = new Board(args[0], args[1]);

	/*
	System.out.println("Initial:\n" + b.blocksToString(b.initial_blocks));
	System.out.println("Final:\n" + b.blocksToString(b.final_blocks));
	*/

	b.solveBreadthFirst();

	System.exit(0);
    }
		
}
