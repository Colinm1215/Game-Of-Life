import java.awt.*;
import java.util.ArrayList;

public class Fungus extends Cell {

    Fungus(int r, int c) {
        super(r, c);
        setColor(Color.blue);
    }

    @Override
    public void spawn() {
        super.spawn();
        setColor(Color.blue);
    }

    /*
    returns true if this Cell should be alive next generation.
    returns false if this Cell should not be alive next generation.
     */
    public boolean nextFate(Cell[][] cells) {
//          Each Fungus with no neighbors dies, as if by loneliness.
//          Else they live
        int numNeighbors = numNeighbors(cells);

        return (numNeighbors >= 1);
    }

    /*
    returns the number of living cells around this cell.
     */
    int numNeighbors(Cell[][] cells) {
        int numNeighbor = 0;

        for (int[] pair : NEIGHBORLOC) {
            try {
                Cell newCell = cells[getRow() + pair[0]][getCol() + pair[1]]; // get the cell object
                if (!newCell.equals(this) && newCell.getIsAlive()) // make sure it isn't this cell, and that it's alive
                    numNeighbor++; // if so add 1 to number of neighbors
            } catch (IndexOutOfBoundsException ignored) {
                // if the cell is on the corner or sides we'll get an index out of bounds, just ignore it
            }
        }
        return numNeighbor;
    }

    ArrayList<int[]> getNormalNeighbors(Cell[][] cells) { // return Normal Neighbor indexes, for use in turning them into Fungus

        ArrayList<int[]> neighbors = new ArrayList<int[]>();

        for (int[] pair : NEIGHBORLOC) {
            try {
                Cell newCell = cells[getRow() + pair[0]][getCol() + pair[1]];
                if (!newCell.equals(this) && newCell.getIsAlive() && !(newCell instanceof Fungus))
                    // make sure newCell is not this cell, is alive, and isn't a Fungus
                    neighbors.add(new int[]{newCell.getRow(), newCell.getCol()}); // add to ArrayList
            } catch (IndexOutOfBoundsException ignored) {
                // if the cell is on the corner or sides we'll get an index out of bounds, just ignore it
            }
        }
        return neighbors;
    }

    @Override
    public void draw(Graphics2D g2, boolean paused) {
        g2.setColor(getColor());
        g2.fillRect(getCol() * SIZE, getRow() * SIZE, SIZE, SIZE);
        g2.setColor(Color.black);
        g2.drawRect(getCol() * SIZE, getRow() * SIZE, SIZE, SIZE);
    }
}
