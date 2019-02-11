import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

@SuppressWarnings("unchecked")
class OptionPanel extends JPanel {
    private final JPanel panelOne = new JPanel();
    private final JPanel panelTwo = new JPanel();
    private final JPanel panelThree = new JPanel();
    private int curPanel = 1;
    private final DefaultListModel listModel = new DefaultListModel();
    private final JFileChooser fc = new JFileChooser();

    OptionPanel(LifePanel panel) {
        createPanelOne(panel);
        createPanelTwo(panel);
        createPanelThree(panel);
    }

    private void createPanelThree(LifePanel panel) {
        // init panel three buttons
        JButton helpButtonP3 = new JButton("Help");
        helpButtonP3.setActionCommand("help");

        JButton normalButton = new JButton("Normal Cells");
        normalButton.setActionCommand("normal");

        JButton fungusButton = new JButton("Fungus Cells");
        fungusButton.setActionCommand("fungus");

        // add action listeners
        helpButtonP3.addActionListener(panel);
        fungusButton.addActionListener(panel);
        normalButton.addActionListener(panel);

        // Create panelThree
        panelThree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelThree.setPreferredSize(new Dimension(LifePanel.WIDTH, 50));
        panelThree.setLayout(new BoxLayout(panelThree, BoxLayout.X_AXIS));

        // add panelThree Buttons
        panelThree.add(helpButtonP3);
        panelThree.add(normalButton);
        panelThree.add(fungusButton);
    }

    private void createPanelTwo(final LifePanel panel) {
        // create panel two buttons
        JButton helpButtonP2 = new JButton("Help");
        helpButtonP2.setActionCommand("help");

        JButton randomButton = new JButton("Randomize Cells");
        randomButton.setActionCommand("random");

        JButton patternLoadButton = new JButton("Load A New Pattern");

        JLabel patternLabel = new JLabel("Patterns :");

        // add action listenrs
        helpButtonP2.addActionListener(panel);
        randomButton.addActionListener(panel);
        patternLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.setTimer(false);
                int returnVal = fc.showOpenDialog(panelTwo); // open a file chooser
                if (returnVal == JFileChooser.APPROVE_OPTION) { // if file chosen
                    File file = fc.getSelectedFile(); // get file
                    File newFile = new File("Patterns/" + file.getName());
                    ArrayList<String> lines;
                    try {
                        lines = LifePanel.PATTERN_READER.readLoadedFile(file); // read file
                        System.out.println(lines.get(0));
                        System.out.println(file.getName());
                        // check if correct file, if so add name to menu
                        if (lines.get(0).equals("#Life 1.06") && file.getName().contains(".lif")) {
                            String filename = file.getName().replace(".lif", "");
                            if (newFile.createNewFile()) {
                                BufferedWriter bf = new BufferedWriter(new FileWriter(newFile));
                                for (String str : lines) {
                                    bf.write(str + "\n");
                                }
                                bf.close();
                                if (notAlreadyInList(filename)) listModel.addElement(filename);
                            } else {
                                JOptionPane.showMessageDialog(panel,
                                        "Failed to create file" +
                                                " Perhaps their is already a file with that name in Patterns?" +
                                                " Remember, it has to be a .lif file," +
                                                " and the file header has to be #Life 1.06",
                                        "File Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel,
                                    "File not formatted correctly!" +
                                            " Remember, it has to be a .lif file," +
                                            " and the file header has to be #Life 1.06",
                                    "File Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ignored) {
                    }
                    panel.setTimer(true);
                } else if (returnVal == JFileChooser.CANCEL_OPTION || returnVal == JFileChooser.ERROR_OPTION) {
                    panel.setTimer(true);
                }
                panelTwo.repaint();
            }
        });

        // Create panelTwo
        panelTwo.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelTwo.setPreferredSize(new Dimension(LifePanel.WIDTH, 50));
        panelTwo.setLayout(new BoxLayout(panelTwo, BoxLayout.X_AXIS));

        // add patterns to panel two menu
        File folder = new File("Patterns");
        File[] listOfFiles = folder.listFiles();
        listModel.addElement("One Cell");
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                String filename = file.getName().replace(".lif", "");
                if (file.getName().contains(".lif")) {
                    ArrayList<String> lines = LifePanel.PATTERN_READER.readFile(filename);
                    if (lines.get(0).equals("#Life 1.06")) listModel.addElement(filename);
                }
            }
        }

        // create panel two pattern menu
        JList list = new JList(listModel); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(1);
        list.setSelectedIndex(0);
        list.addListSelectionListener(panel);
        JScrollPane listScrollPane = new JScrollPane(list);

        // add panelTwo Buttons
        panelTwo.add(helpButtonP2);
        panelTwo.add(randomButton);

        // add panelTwo Menu
        panelTwo.add(patternLabel);
        panelTwo.add(listScrollPane);
        panelTwo.add(patternLoadButton);
    }

    private void createPanelOne(LifePanel panel) {
        // Create panelOne
        panelOne.setBorder(new BevelBorder(BevelBorder.LOWERED));
        panelOne.setPreferredSize(new Dimension(LifePanel.WIDTH, 50));
        panelOne.setLayout(new BoxLayout(panelOne, BoxLayout.X_AXIS));

        // Create Buttons
        JButton helpButtonP1 = new JButton("Help");
        helpButtonP1.setActionCommand("help");

        JButton startButton = new JButton("Start");
        startButton.setActionCommand("start");

        JButton pauseButton = new JButton("Pause");
        pauseButton.setActionCommand("pause");

        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand("clear");

        // Add ActionListeners to Buttons
        helpButtonP1.addActionListener(panel);
        startButton.addActionListener(panel);
        pauseButton.addActionListener(panel);
        clearButton.addActionListener(panel);

        // Create Slider
        JSlider programTime = new JSlider(JSlider.HORIZONTAL,
                LifePanel.TIME_FAST, LifePanel.TIME_SLOW, LifePanel.TIME_INIT);
        programTime.setInverted(true);
        Hashtable labelTable = new Hashtable();
        labelTable.put(LifePanel.TIME_SLOW, new JLabel("Slow"));
        labelTable.put(LifePanel.TIME_INIT, new JLabel("Speed of Simulation"));
        labelTable.put(LifePanel.TIME_FAST, new JLabel("Fast"));
        programTime.setLabelTable(labelTable);
        programTime.setPaintLabels(true);
        programTime.addChangeListener(panel);

        // Add panelOne Buttons
        panelOne.add(helpButtonP1);
        panelOne.add(startButton);
        panelOne.add(pauseButton);
        panelOne.add(clearButton);

        // Add panelOne Slider
        panelOne.add(programTime);
    }

    private boolean notAlreadyInList(String name) {
        return !listModel.contains(name);
    }

    JPanel getCurPanel() {
        if (curPanel == 1) return panelOne;
        else if (curPanel == 2) return panelTwo;
        else return panelThree;
    }

    JPanel getPanel(int index) {
        if (index == 1) return panelOne;
        else if (index == 2) return panelTwo;
        else return panelThree;
    }

    void upPanel() {
        if (curPanel + 1 <= 3) curPanel++;
    }

    void downPanel() {
        if (curPanel - 1 >= 1) curPanel--;
    }
}
