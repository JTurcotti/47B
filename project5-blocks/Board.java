import java.util.*;
import java.io.*;

public class Board {
    private int rows, cols;
    private int num_blocks = 0;
    private long[] blocks = new long[MAX_BLOCKS];
    
    private long[] edges = new long[4];
    static final int DOWN = 0;
    static final int LEFT = 1;
    static final int UP = 2;
    static final int RIGHT = 3;
    static final int MAX_BLOCKS = 256;

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
    
    public Board(String filename) {

	//set row, col, and blocks
	try {
	    Scanner scanner = new Scanner(new File(filename));
	    //read first two ints
	    rows = scanner.nextInt();
	    cols = scanner.nextInt();
	    blocks = new long[MAX_BLOCKS];

	    while (scanner.hasNextInt()) {
		//read four ints at a time, exceptions caught below
		blocks[num_blocks++] = toBits(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
	    }
	    
	    scanner.close();
	
	} catch (NoSuchElementException e) {
	    System.out.println("Input file not in correct format");
	    System.exit(1);
	} catch (FileNotFoundException e) {
	    System.out.println("Input file does not exist");
	    System.exit(1);
	}

	//set edges
	edges[DOWN] = toBits(1, cols, 0, 0);
	edges[LEFT] = toBits(rows, 1, 0, cols-1);
	edges[UP] = toBits(1, cols, rows-1, 0);
	edges[RIGHT] = toBits(rows, 1, 0, 0);
    }

    @Override
    public String toString() {
	String out = "";
	String s;
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {
		s = "_";
		for (int i = 0; i < num_blocks; i++)
		    if ((blocks[i] & toBits(1, 1, r, c)) != 0)
			s = "" + (i + 1);
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
	Board b = new Board(args[0]);
	

	System.out.println(b);
	for (int i = 0; i < 4; i ++)
	    System.out.println(b.bitsToString(b.edges[i]));
    }
			
}
