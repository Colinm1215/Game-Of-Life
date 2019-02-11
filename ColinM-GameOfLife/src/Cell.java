import java.awt.*;

class Cell {
    private boolean isAlive;
    private final int row;
    private final int col;
    private Color color = Color.white;

    static final int SIZE = 10;
    // these are the relative locations of all neighbors, with (0,0) being this cell
    static final int[][] NEIGHBORLOC = new int[][]{{-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 0}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}};

    Cell(int r, int c) {
        isAlive = false;
        row = r;
        col = c;
    }

    void kill() {
        color = Color.white; // set color to white, kill
        isAlive = false;
    }

    public void spawn() {
        color = Color.green; // set color to green, spawn
        isAlive = true;
    }

    /*
    returns the number of living cells around this cell.
     */
    int numNeighbors(Cell[][] cells) {
        int numNeighbor = 0;

        // go through each relative location, find absolute location, and evaluate that cell
        for (int[] pair : NEIGHBORLOC) {
            try {
                Cell newCell = cells[getRow() + pair[0]][getCol() + pair[1]]; // find the actual cell object
                if (!newCell.equals(this) && newCell.getIsAlive() && newCell.getClass().equals(Cell.class))
                    // if the cell we find isn't this cell, is alive, and isn't a member of Fungus, or any other subclass
                    numNeighbor++; // add 1 to number of neighbors
            } catch (IndexOutOfBoundsException ignored) {
                // if the cell is on the corner or sides we'll get an index out of bounds, just ignore it
            }
        }
        return numNeighbor;
    }

    /*
    returns true if this Cell should be alive next generation.
    returns false if this Cell should not be alive next generation.
     */
    public boolean nextFate(Cell[][] cells) {
//        For a space that is alive:
//          Each cell with one or no neighbors dies, as if by loneliness.
//          Each cell with four or more neighbors dies, as if by overpopulation.
//          Each cell with two or three neighbors survives.
//        For a space that is 'unpopulated' or 'dead':
//          Each cell with three neighbors becomes populated.
        int numNeighbors = numNeighbors(cells);

        if (isAlive) return !(numNeighbors < 2 || numNeighbors > 3);
        else return (numNeighbors == 3);
    }

    public void draw(Graphics2D g2, boolean paused) {
        if (isAlive && !paused) color = color.darker();
        g2.setColor(color);
        g2.fillRect(col * SIZE, row * SIZE, SIZE, SIZE);
        g2.setColor(Color.black);
        g2.drawRect(col * SIZE, row * SIZE, SIZE, SIZE);
    }

    boolean getIsAlive() {
        return isAlive;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object otherCell) {
        //override so that if this and otherCell have the same row and col, true.
        if (!(otherCell instanceof Cell)) return false;
        else return (((Cell) otherCell).getRow() == row && ((Cell) otherCell).getCol() == col);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "isAlive=" + isAlive +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

    Color getColor() {
        return color;
    }

    @SuppressWarnings("SameParameterValue")
    void setColor(Color color) {
        this.color = color;
    }
}
