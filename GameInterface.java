import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameInterface extends JFrame {
    private JLabel wordLabel;
    private Game gm;
    private DrawHangingMan manDrawing;

    /*initialize game window*/
    public GameInterface() {
        gm = new Game();                                /*start a new game*/
        wordLabel = new JLabel(gm.printBlankedWord());  /*set blanked out word*/
        manDrawing = new DrawHangingMan();
        /*window configuration*/
        wordLabel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        manDrawing.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        setTitle("Hanging-man");
        getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS)); /*set layout manager*/
        add(new GameUI());
        add(wordLabel);
        add(manDrawing);
        setResizable(false);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /*update blanked word label*/
    public void updateBlanked() {
        wordLabel.setText(gm.printBlankedWord());   /*update blanked word*/
    }

    /*the following class creates, displays and updates the game's UI*/
    private class GameUI extends JPanel implements ActionListener {
        Character[] chArr = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };/*for use as selection init*/
        private ArrayList<Character> abc = new ArrayList<>(Arrays.asList(chArr));
        private JComboBox cbLetters;             /*letter selection combobox*/
        private final JLabel usedLabel;         /*displays used letters*/
        private final JLabel summery;           /*displays game statistics*/
        private ArrayList<Character> usedList;  /*dynamic array contains used letters*/

        /*initialize game UI*/
        public GameUI() {
            cbLetters = new JComboBox<>(abc.toArray());                           /*letter selection combo box*/
            cbLetters.addActionListener(this);                               /*add listener*/
            cbLetters.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
            usedLabel = new JLabel();
            usedList = new ArrayList<>();
            JLabel prompt = new JLabel("Please choose your guess: ");
            prompt.setFont(new Font("Ariel", Font.BOLD, 14));
            summery = new JLabel();
            setLayout(new GridLayout(8, 0)); /*set grid layout for panel, 4 rows for the needed items, another 4 for spacing. 20 pxl per row.*/
            setPreferredSize(new Dimension(250, 160));
            add(prompt);
            add(cbLetters);
            add(summery);
            add(usedLabel);
        }

        /*display end of game menu*/
        private void displayEndGameMenu(Status gameStatus) {
            Object input;/*input dialog answer container*/
            Object[] selection = new String[]{"New Game", "Exit"}; /*drop down menu options*/
            input = JOptionPane.showInputDialog(null, "Choose what to do next:"
                    , (gameStatus == Status.GameOver ? "Game-Over" : "Success!"), JOptionPane.PLAIN_MESSAGE, null, selection, selection[1]); /*show input dialog*/
            if (input == null || input.toString().equals("Exit"))
                System.exit(0);     /*terminate*/
            else {
                GameInterface.this.dispose();
                new GameInterface();      /*start new game*/
            }

        }

        /*update UI upon selection*/
        private char updateGUI() {
            char ch = (char) cbLetters.getSelectedItem();
            int index = cbLetters.getSelectedIndex();
            usedList.add(ch);                           /*add selection to the used letter list*/
            abc.remove(index);                          /*remove from available letters list*/
            cbLetters.removeItemAt(index);              /*remove selected item from combobox*/
            cbLetters.setSelectedIndex(0);              /*reset combobox*/
            Collections.sort(usedList);                 /*sort used letter list alphabetically*/
            summery.setText("Missed: " + gm.misses() + ", Revealed: " + gm.revealed() + "/" + gm.getNumLettersToGuess());
            usedLabel.setText("Used: " + usedList.toString());
            return ch;
        }

        /*act upon selection from combobox, overrides action preformed.*/
        public void actionPerformed(ActionEvent e) {
            Status currentStatus;
            if (e.getSource() == cbLetters && cbLetters.getSelectedIndex() > 0) {
                {   /*update game according to the selected letter*/
                    if (gm.revealChar((char) cbLetters.getSelectedItem()))
                        updateBlanked(); /*currect guess, update accordingly*/
                    else
                        manDrawing.repaint(); /*wrong guess*/
                    updateGUI();/*update statistics and combobox*/
                    if ((currentStatus = gm.gameStatus()) != Status.Continue) /*display end of game menu if needed*/
                        this.displayEndGameMenu(currentStatus);
                }
            }
        }
    }

    /*class to draw the hanging man on screen*/
    private class DrawHangingMan extends JPanel {
        private final int headIndex = 4;
        private final dualPoint[] dpArr = new dualPoint[10];

        /*struct to hold graphics initialization data*/
        private class dualPoint {
            public int x1;
            public int y1;
            public int x2;
            public int y2;

            /*constructor*/
            public dualPoint(int x1, int y1, int x2, int y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }
        }

        /*initialize dualPoint array*/
        public DrawHangingMan() {
            int x = 100, y = 50, w = 30, h = 30; /*relative coordinates to align hanging man by*/
            this.setPreferredSize(new Dimension(2 * x, 7 * h));
            dpArr[0] = new dualPoint(x / 2, y + 5 * h, x + w * 2, y + 5 * h);         /*base*/
            dpArr[1] = new dualPoint(x / 2 + w, y + 5 * h, x / 2 + w, y / 4);         /*pole*/
            dpArr[2] = new dualPoint(x / 2 + w, y / 4, x + w / 2, y / 4);             /*top bar*/
            dpArr[3] = new dualPoint(x + w / 2, y / 4, x + w / 2, y);                     /*rope*/
            dpArr[4] = new dualPoint(x, y, w, h);                                                    /*head*/
            dpArr[5] = new dualPoint(x + w / 2, y + h, x + w / 2, y + 3 * h);         /*body*/
            dpArr[6] = new dualPoint(x + w / 2, y + h, x + w / 16, y + 2 * h);        /*left arm*/
            dpArr[7] = new dualPoint(x + w / 2, y + h, x + w, y + 2 * h);             /*right arm*/
            dpArr[8] = new dualPoint(x + w / 2, y + 3 * h, x, y + 4 * h);                 /*left leg*/
            dpArr[9] = new dualPoint(x + w / 2, y + 3 * h, x + w, y + 4 * h);         /*right leg*/
        }

        /*print hanging man according to the current misses done by user.*/
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < gm.misses(); i++) {
                if (i == headIndex)
                    g.drawOval(dpArr[i].x1, dpArr[i].y1, dpArr[i].x2, dpArr[i].y2);
                else
                    g.drawLine(dpArr[i].x1, dpArr[i].y1, dpArr[i].x2, dpArr[i].y2);
            }

        }
    }
}