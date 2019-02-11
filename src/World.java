import java.awt.*;
import java.util.ArrayList;

class World {

    private final Cell[][] cells;

    /*
    Creates a world of cells, devoid of life.
     */
    World(int rows, int cols) {
        cells = new Cell[rows][cols];
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
        randomizeWorld();
    }

    /*
    Sets every cell to be alive or not alive with a 50% chance.
     */
    void randomizeWorld() {
        clearCells();
        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                if (Math.random() >= 0.5) {
                    cell.spawn();
                }
            }
        }
    }

    void drawWorld(Graphics2D g2, boolean paused) {
        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                cell.draw(g2, paused);
            }
        }
    }

    void nextGeneration() {
        //nextGenFate holds if a cell will be alive in the next generation.
        //this is needed so we don't modify the cells while we are
        //calculating the next gen.
        boolean[][] nextGenFate = new boolean[cells.length][cells[0].length]; // will it live next gen?
        boolean[][] fungFate = new boolean[cells.length][cells[0].length]; // will it become a Fungus cell?

        for (Cell[] cellArray : cells) {
            for (Cell cell : cellArray) {
                nextGenFate[cell.getRow()][cell.getCol()] = cell.nextFate(cells); // check if alive or dead
                if (cell instanceof Fungus) { // if instance of Fungus, check surrounding cells.
                    fungFate[cell.getRow()][cell.getCol()] = true; // make sure this Cell is still a Fungus next gen
                    ArrayList<int[]> newFungus = ((Fungus) cell).getNormalNeighbors(cells); // get living neighbors
                    for (int[] pair : newFungus) {
                        if (nextGenFate[cell.getRow()][cell.getCol()])
                            fungFate[pair[0]][pair[1]] = true; // make neighbor a Fungus
                    }
                }
            }
        }
        //last thing in this method, use nextGenFate to spawn or kill the cells,
        //thus advancing the generation.
        for (int r = 0; r < nextGenFate.length; r++) {
            for (int c = 0; c < nextGenFate[0].length; c++) {
                Cell cell = cells[r][c];
                if (fungFate[r][c] && !(cell instanceof Fungus)) {
                    cell = new Fungus(r, c);
                    cell.spawn();
                } else if (!(fungFate[r][c]) && cell instanceof Fungus) {
                    cell = new Cell(r, c);
                    cell.spawn();
                }
                if (nextGenFate[r][c] && !cell.getIsAlive()) cell.spawn();
                else if (!(nextGenFate[r][c]) && cell.getIsAlive()) cell.kill();
                cells[r][c] = cell;
            }
        }
    }

    void editCell(int row, int col, String type) { // used to edit the cells with mouse interaction
        try {
            Cell cell = cells[row][col];
            if (type.equals("Fungus")) {
                if (!(cell instanceof Fungus)) {
                    cell = new Fungus(row, col);
                }
                if (cell.getIsAlive()) cell.kill();
                else cell.spawn();
            } else {
                if (type.equals("Patterned")) {
                    if (!(cell.getClass().equals(Cell.class))) {
                        cell = new Cell(row, col);
                    }
                    cell.spawn();
                } else {
                    if (!(cell.getClass().equals(Cell.class))) {
                        cell = new Cell(row, col);
                    }
                    if (cell.getIsAlive()) cell.kill();
                    else cell.spawn();
                }
            }

            cells[row][col] = cell;
        } catch (IndexOutOfBoundsException ignored) {
            // if the cell is on the corner or sides we'll get an index out of bounds, just ignore it
        }
    }

    void clearCells() {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[0].length; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
    }

}