import java.util.*;
import java.io.*;


//instance of class stores its size and its initial and final configurations. methods are provided for manipulating board states (represented as arrays of bitboards (long[] s)) in this context
public class Board {
    private int rows, cols;
    private int num_blocks = 0;
    private int num_final_blocks = 0;

    //reference initial and final (desired) blocks given
    private long[] initial_blocks = new long[MAX_BLOCKS];
    private long[] final_blocks = new long[MAX_BLOCKS];
    
    private long[] edges = new long[4];
    static final int DOWN = 0;
    static final int LEFT = 1;
    static final int UP = 2;
    static final int RIGHT = 3;
    static final int MAX_BLOCKS = 64;
    //represents invalid state of board, used as return value;
    static final long[] INVALID = new long[1];

    //check if (row, col) is on this board
    private boolean inRange(int row, int col) {
	return (0 <= row) && (row < rows) &&
	    (0 <= col) && (col < cols);
    }
	    
    //convert block data to a corresponding bitBoard
    private long toBits(int length, int width, int row, int col) {
	assert inRange(row, col) &&
	    inRange(row + length, col + width);

	long bits = 0;
	for (int r = row; r < row + length; r++)
	    for (int c = col; c < col + width; c++)
		bits += 1 << (c + r * cols);

	return bits;
    }

    //check if given bitboard satisfies the final conditions
    //IN NEED OF OPTIMIZATION
    private boolean isFinal(long[] blocks) {
	int num_found = 0;
	for (int i = 0; i < num_blocks; i++) {
	    for (int f = 0; f < num_final_blocks; f++) {
		if (blocks[i] == final_blocks[f]) {
		    num_found++;
		}
	    }
	}
	return (num_found == num_final_blocks);
    }

    //returns a bitboard representing the given shift on blocks, or INVALID
    //or INVALID if no such move is possible
    private long[] shift(long[] blocks, int block, int direction) {
	
	//check if block is already at edge
	if ((blocks[block] & edges[direction]) != 0)
	    return INVALID;

	long shiftedBlock = 0;
	switch (direction) {
	case DOWN:
	    shiftedBlock = blocks[block] >>> cols;
	    break;
	case LEFT:
	    shiftedBlock = blocks[block] << 1;
	    break;
	case UP:
	    shiftedBlock = blocks[block] << cols;
	    break;
	case RIGHT:
	    shiftedBlock = blocks[block] >>> 1;
	    break;
	}

	//generate set of all cells occupied by other blocks
	long collisions = 0;
	for (int i = 0; i < block; i++)
	    collisions |= blocks[i];
	for (int i = block + 1; i < num_blocks; i++)
	    collisions |= blocks[i];

	//check for collisions with other blocks
	if ((collisions & shiftedBlock) == 0) {
	    long[] shifted = new long[num_blocks];
	    System.arraycopy(blocks, 0, shifted, 0, num_blocks);
	    shifted[block] = shiftedBlock;
	    return shifted;
	} else {
	    return INVALID;
	}
    }

    //attempts to solve this board, starting from the initial position and seeking satisfaction of isFinal
    public void solve() {
	Set<Integer> tried = new HashSet<>();
	LinkedList<String> instructions = new LinkedList<>();
	
	if (tryShifting(initial_blocks, tried, instructions)) {
	    for (String s: instructions)
		System.out.println(s);
	} else {
	    exit("no solution found");
	}
    }

    //return the row and column of the bottom lefthand corner of the given bitboard's represented block
    private int bitsToRow(long bits) {
	return ((int) (Math.log(bits & -bits)/Math.log(2))) / cols;
    }

    private int bitsToCol(long bits) {
    	return ((int) (Math.log(bits & -bits)/Math.log(2))) % cols;
    }
		

    
    //can isFinal be reached from the given set of blocks? tries all possible shifts from this position (and checks this position itself) to find out!
    private boolean tryShifting(long[] blocks, Set<Integer> tried, LinkedList<String> instructions) {
	//check if this is not a valid state
	if (blocks == INVALID)
	    return false;
	
	//check if already checked blocks
	if (tried.contains(Arrays.hashCode(blocks)))
	    return false;
	tried.add(Arrays.hashCode(blocks));

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

	    if (rows * cols > 64)
		exit("Input board too large: maximum supported size is 64 cells");

	    while (scanner.hasNextInt()) {
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

    public String blocksToString(long[] blocks) {
	if (blocks == INVALID)
	    return "INVALID";
	
	String out = "";
	String s;
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {
		s = "_";
		for (int i = 0; i < num_blocks; i++)
		    if ((blocks[i] & toBits(1, 1, r, c)) != 0)
			s = "" + i;
		out += s;
	    }
	    out += "\n";
	}
	return out;
    }

    public String bitsToString(long bits) {
	String out = "";
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {
		out += ((bits & toBits(1, 1, r, c)) == 0)? 0: 1;
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
	System.out.println(b.blocksToString(b.initial_blocks));
	System.out.println(b.blocksToString(b.final_blocks));
	*/

	b.solve();
    }
		
}
