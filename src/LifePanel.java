import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Colin Mettler
 * Conway's Game of Life
 **/

class LifePanel extends JPanel implements ActionListener, KeyListener, ChangeListener, MouseListener,
        MouseMotionListener, ListSelectionListener {
    private final Timer timer;
    private final World theWorld;
    private boolean paused;
    static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    static final int TIME_INIT = 500;
    static final int TIME_SLOW = 1000;
    static final int TIME_FAST = 1;
    private static JFrame frame;
    private JPanel topPanel;
    private final OptionPanel optionPanel;
    private int[] prevCell = new int[2];
    private String cellType = "Normal";
    private String curPattern = "One Cell";
    static final PatternReader PATTERN_READER = new PatternReader();

    private LifePanel() {
        setSize(LifePanel.WIDTH, LifePanel.HEIGHT);

        // start paused
        paused = true;

        // init optionPanel
        optionPanel = new OptionPanel(this);

        // add option menu to frame
        topPanel = optionPanel.getCurPanel();
        frame.add(topPanel, BorderLayout.NORTH);

        // add listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        //init theWorld.
        theWorld = new World(LifePanel.HEIGHT / Cell.SIZE, LifePanel.WIDTH / Cell.SIZE);

        // init and start timer
        timer = new Timer(TIME_INIT, this);
        setTimer(true);
    }

    void setTimer(boolean state) {
        if (state) timer.start();
        else timer.stop();
    }

    // called if a new pattern is selected
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // set current pattern to new selected pattern
        curPattern = (String) ((JList) e.getSource()).getSelectedValue();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        theWorld.drawWorld(g2, paused);
    }


    // called if the JSlider value changes
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int time = source.getValue();
            timer.setDelay(time);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("start".equals(command)) paused = false; // if start button pressed, unpause sim
        else if ("pause".equals(command)) paused = true; // if pause button pressed, pause sim
        else if ("clear".equals(command)) theWorld.clearCells(); // if clear button pressed, clear sim
        else if ("random".equals(command)) theWorld.randomizeWorld(); // if randomized pressed, randomize sim
        else if ("normal".equals(command)) cellType = "Normal"; // set cellType to Normal
        else if ("fungus".equals(command)) cellType = "Fungus"; // set cellType to Fungus
        else if ("help".equals(command)) { // show a series of messages explaining controls
            setTimer(false);
            JOptionPane.showMessageDialog(this,
                    "Welcome to the Tutorial!",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "You can click on the board to spawn or kill Cells",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "The Cells on the board will behave according to rules assigned to their Type",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            int option = JOptionPane.showOptionDialog(frame,
                    "Do you wish to learn about the Cell Types?",
                    "Tutorial",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "For each Normal Cell that is alive:\n" +
                                "   Each Cell with one or no neighbors dies, as if by loneliness.\n" +
                                "   Each Cell with four or more neighbors dies, as if by overpopulation.\n" +
                                "   Each Cell with two or three neighbors survives.\n" +
                                "For each Normal Cell that is 'unpopulated' or 'dead':\n" +
                                "   Each Cell with three neighbors becomes populated.",
                        "Tutorial", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(this,
                        "For each Fungus Cell that is alive:\n" +
                                "   Each Cell with no neighbors dies, as if by loneliness.\n" +
                                "   Each neighboring Normal Cell will become a Fungus Cell\n",
                        "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.validate();
            topPanel = optionPanel.getPanel(1);
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
            JOptionPane.showMessageDialog(this,
                    "Press the Start button to start the Sim\n"
                            + "Press the Pause button to pause the Sim\n"
                            + "In addition, you can press the Space key to toggle between the two states",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "Press the Clear button to clear the board of all Cells\n"
                            + "In addition, you can press the C key to clear the board",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "Use the slider to change the speed of the Sim\n"
                            + "You can also use the left and right arrow keys to change the speed",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "You can use the up and down arrows to switch between menu panels",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            frame.remove(topPanel);
            frame.repaint();
            frame.validate();
            topPanel = optionPanel.getPanel(2);
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
            JOptionPane.showMessageDialog(this,
                    "Press the Randomize Cells button to clear the board\n" +
                            "and then fill the board with Cells randomly\n" +
                            "This can also be accomplished by pressing the R key",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "Use the selection box labeled Patterns to select, and use, a loaded pattern",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this,
                    "Use the Load A New Pattern button to load a pattern from a .lif file\n" +
                            "Look up the proper formatting if you're not sure how to use a .lif file",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            frame.remove(topPanel);
            frame.repaint();
            frame.validate();
            topPanel = optionPanel.getPanel(3);
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
            JOptionPane.showMessageDialog(this,
                    "Use the buttons on this panel to select the type of Cell you wish to place down\n" +
                            "Remember, the only Cells that can be placed in a pattern are Normal Cells",
                    "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            frame.remove(topPanel);
            frame.repaint();
            frame.validate();
            topPanel = optionPanel.getCurPanel();
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
            setTimer(true);
        }

        if (!paused) { // if not paused, continue to next generation
            theWorld.nextGeneration();
        }
        repaint();
        this.grabFocus();
    }

    @Override
    // use mouse coordinates to edit the cells while dragging
    public void mouseDragged(MouseEvent mouseEvent) {
        if (curPattern.equals("One Cell")) {
            int xVal = mouseEvent.getX() / Cell.SIZE;
            int yVal = (mouseEvent.getY()) / Cell.SIZE;
            if (!(prevCell[0] == xVal && prevCell[1] == yVal)) {
                theWorld.editCell(yVal, xVal, cellType);
                prevCell = new int[]{xVal, yVal};
            }
        }
        repaint();
    }

    @Override
    // use mouse coordinates to edit single clicks, or place a pattern
    public void mouseReleased(MouseEvent mouseEvent) {
        if (curPattern.equals("One Cell")) {
            int xVal = mouseEvent.getX() / Cell.SIZE;
            int yVal = (mouseEvent.getY()) / Cell.SIZE;
            if (!(prevCell[0] == xVal && prevCell[1] == yVal)) {
                theWorld.editCell(yVal, xVal, cellType);
                prevCell = new int[]{xVal, yVal};
            }
        } else {
            ArrayList<String> lines = PATTERN_READER.readFile(curPattern);
            ArrayList<int[]> coords = PATTERN_READER.parseFileArray(lines);
            int xVal = mouseEvent.getX() / Cell.SIZE;
            int yVal = (mouseEvent.getY()) / Cell.SIZE;
            try {
                for (int[] coord : coords) {
                    theWorld.editCell(yVal + coord[1], xVal + coord[0], "Patterned");
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // use keys to edit the sim speed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (topPanel.equals(optionPanel.getPanel(1))) {
                JSlider slider = (JSlider) topPanel.getComponent(4);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) slider.setValue(slider.getValue() + 50);
                else slider.setValue(slider.getValue() - 50);
            } else {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (timer.getDelay() + 50 <= TIME_SLOW) timer.setDelay(timer.getDelay() + 50);
                    else timer.setDelay(TIME_SLOW);
                } else if (timer.getDelay() - 50 >= TIME_FAST) {
                    timer.setDelay(timer.getDelay() - 50);
                } else {
                    timer.setDelay(TIME_FAST);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) { // use space to pause/unpause
            paused = !paused;
        } else if (e.getKeyCode() == KeyEvent.VK_C) { // use c to clear the board
            theWorld.clearCells();
        } else if (e.getKeyCode() == KeyEvent.VK_R) { // use r to randomize the board
            theWorld.randomizeWorld();
        // use the up and down arrow keys to switch option menus
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            optionPanel.upPanel();
            frame.remove(topPanel);
            frame.repaint();
            frame.validate();
            topPanel = optionPanel.getCurPanel();
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            optionPanel.downPanel();
            frame.remove(topPanel);
            frame.repaint();
            frame.validate();
            topPanel = optionPanel.getCurPanel();
            if (topPanel.equals(optionPanel.getPanel(1))) {
                JSlider slider = (JSlider) topPanel.getComponent(4);
                slider.setValue(timer.getDelay());
            }
            frame.add(topPanel, BorderLayout.NORTH);
            frame.repaint();
            frame.validate();
        }
        this.grabFocus();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public static void main(String[] args) {
        frame = new JFrame("Life of Mettler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT + 24));

        JPanel panel = new LifePanel();
        panel.setFocusable(true);
        panel.grabFocus();
        frame.setResizable(false);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}