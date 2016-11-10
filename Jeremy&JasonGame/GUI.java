import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Mini Project - Part 1
 * Course Java 2
 * @author Jeremy Bolella and Jason Nordheim
 *
 * This projects goal is to make a video game using JFrame and other default tools included in Java
 * This project is a spinoff of battleship
 * Two players can play and each take turns till one player wins
 **/

/** COMPILED WITH JAVA 1.8 **/

public class GUI extends JFrame {
    // ---- Global Variables
    public String player1_name = "";
    public String player2_name = "";
    public boolean initializing = true;
    private int clicks = 0;
    boolean isPlayer1;
    int gameplayCounter;

    ArrayList<JButton> buttons = new ArrayList<>();
    private JFrame f1 = new JFrame();
    private JFrame playerBoard = new JFrame();
    JFrame playerBoard2 = new JFrame();
    private JButton finish = new JButton("Finish");
    // intitial method run
    public void init() {
        JPanel p1 = new JPanel(new BorderLayout());
        JPanel westPanel = new JPanel(new GridLayout(2,1)); // two rows, 1 column
        JPanel eastPanel = new JPanel(new GridLayout(2,1)); // two rows, 1 column

        JButton btn_submit = new JButton("Start Game");
        JTextField txt_p1 = new JTextField(10);
        JTextField txt_p2 = new JTextField(10);
        JLabel lbl_p1 = new JLabel(" Player 1 Name:");
        JLabel lbl_p2 = new JLabel(" Player 2 Name:");

        /**
         * ActionListener for submit button
         * @returns void
         * @params ActionListener
         */
        btn_submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == btn_submit) {
                    String temp1 = txt_p1.getText();
                    String temp2 = txt_p2.getText();
                    f1.setVisible(false);
                    if (temp1.equals("")) {
                        createErrorMessageBox("Please enter a name into the player 1 text field");
                    } else if (temp2.equals("")) {
                        createErrorMessageBox("Please enter a name into the player 2 text field");
                    }
                    else {
                         String RULES = new String(
                                "<html><body style='width: 300'>Each player selects 5 positions on a 5 x 5 grid. These are the locations " +
                                        "of yourship. Once the ships are chosen, the players alternate attacking each other by launching a " +
                                        "missile strike on a position on the grid. If the position is the location of the opponents ship " +
                                        "then the space on the grid will display “hit”, if it is not a position on the grid, it will display " +
                                        "'miss'. The players continue alternating attacks until all the ships are destroyed. The last" +
                                        "player with ships remaining wins.</body></html> ");


                        new JOptionPane().showConfirmDialog(null, RULES + " Player 1 goes first", null, JOptionPane.PLAIN_MESSAGE);
                        playerBoard.setVisible(true);
                        playerBoard.repaint();

                    }
                }
            } // end actionPerformed(ActionEvent ae)
        });
        //adds the labels and text fields to the west and east panel
        westPanel.add(lbl_p1);
        westPanel.add(lbl_p2);
        eastPanel.add(txt_p1);
        eastPanel.add(txt_p2);
        //adds the west/east panel to the p1 panel and then adds the submit button(btn_submit)
        p1.add(westPanel, BorderLayout.WEST);
        p1.add(eastPanel, BorderLayout.EAST);
        p1.add(btn_submit, BorderLayout.SOUTH);
        // add components to the frame
        f1.add(p1);
        f1.setTitle("BattleShip start of the game.");
        f1.pack();
        f1.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f1.setVisible(true);
        f1.setLocationRelativeTo(null);


    }


    /**
     * Method to set up the game board hotspots for each player
     * @param player - Takes in the player1 object
     * @param player2 - Takes in the player2 object
     */
    public void setBoard(Player player, Player player2) {
        playerBoard.setVisible(true);
        ActionListener listener  = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonClick = e.getActionCommand();
                System.out.println(buttonClick);

                // if the player has
                if (clicks < 4) {
                    player.addHotSpot(buttonClick);
                    clicks++;
                    int buttonToInt = Integer.parseInt(buttonClick);
                    JButton disableButton = buttons.get(buttonToInt);
                    disableButton.setEnabled(false);

                } else if (clicks == 4) {
                    finish.setEnabled(true);
                    disableButtons(buttons);

                    if (buttonClick.contentEquals("Finish") && clicks == 4) {
                        playerBoard.setVisible(false);
                        finish.setEnabled(false);
                        clicks = 5;
                        new JOptionPane().showConfirmDialog(null, "Player 2 Please select your playing board", null, JOptionPane.PLAIN_MESSAGE);
                        playerBoard.repaint();
                        resetButtons(buttons);
                        playerBoard.setVisible(true);
                    }
                } else if(clicks >=5) {
                    if (!(buttonClick.contentEquals("Finish"))) {
                        player2.addHotSpot(buttonClick);
                        clicks++;
                        int buttonToInt = Integer.parseInt(buttonClick);
                        JButton disableButton = buttons.get(buttonToInt);
                        disableButton.setEnabled(false);
                    }
                }
                if (clicks > 9) {
                    playerBoard.setVisible(false);
                    buttons.clear();
                    isPlayer1=true;
                    runGame(player, player2);
                    new JOptionPane().showConfirmDialog(null, "The ships are placed. Player 1 will go first", null, JOptionPane.PLAIN_MESSAGE);

                }
            }
        };
        JPanel grid = new JPanel(new GridLayout(5, 5, 10, 10));
        finish.setEnabled(false);
        finish.addActionListener(listener);
        finish.setSize(500, 30);
        playerBoard.setTitle(player.getName() + "Game Board");
        playerBoard.add(grid, BorderLayout.CENTER);
        playerBoard.add(finish, BorderLayout.SOUTH);
        playerBoard.setSize(500, 500);
        playerBoard.setVisible(false);
        for(int i=0; i < 25; i++) {
            String buttonName = Integer.toString(i);
            JButton tempButton = new JButton(buttonName);
            tempButton.addActionListener(listener);
            tempButton.setActionCommand(buttonName);
            buttons.add(tempButton);
            grid.add(tempButton);
        }

    }
    public void runGame(Player player1, Player player2) {

        ActionListener listener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buttonClick = e.getActionCommand();
                System.out.println(buttonClick);

                //ALL PLAYER 1 EVENTS SHOULD HAPPEN HERE!
                if (isPlayer1) {
                    if(testHotSpot(buttonClick, player2)) {
                        player1.addCaughtHotSpot(buttonClick);
                    } else {
                        player1.addSpot(buttonClick);
                    }
                    resetButtons(buttons, 2);
                    nextTurn(player2);
                    playerBoard2.setTitle("Player 2 goes");
                    testWin(player1, "1");
                    isPlayer1=false;

                }

                //ALL PLAYER 2 EVENTS SHOULD HAPPEN HERE!
                else {
                    if(testHotSpot(buttonClick, player1)) {
                        player2.addCaughtHotSpot(buttonClick);
                    } else {
                        player2.addSpot(buttonClick);
                    }
                    isPlayer1=true;
                    resetButtons(buttons, 1);
                    nextTurn(player1);
                    testWin(player2, "1");
                    playerBoard2.setTitle("Player 1 goes");

                }
            }
        };


        JPanel grid = new JPanel(new GridLayout(5, 5, 10, 10));
        finish.setSize(500, 300);
        playerBoard2.setTitle("new board");
        JLabel northTitle = new JLabel("");
        playerBoard2.add(grid, BorderLayout.CENTER);
        playerBoard2.add(finish, BorderLayout.SOUTH);
        playerBoard2.setSize(500, 500);
        playerBoard2.setVisible(true);
        for (int i = 0; i < 25; i++) {
            String buttonName = Integer.toString(i);
            JButton tempButton = new JButton(buttonName);
            tempButton.addActionListener(listener2);
            tempButton.setActionCommand(buttonName);
            buttons.add(tempButton);
            grid.add(tempButton);
        }
        resetButtons(buttons, 1);
        playerBoard2.setTitle("Player 1 goes");

    }

    public void resetButtons(ArrayList<JButton> btns) {
        for(int i=0; i < 25; i++) {
            JButton tempBtn = btns.get(i);
            tempBtn.setEnabled(true);

        }
    }

    public void resetButtons(ArrayList<JButton> btns, int player) {
        playerBoard2.setVisible(false);
        for(int i=0; i < 25; i++) {
            JButton tempBtn = btns.get(i);
            tempBtn.setEnabled(true);

            if (player == 1) {
                tempBtn.setBackground(Color.GREEN);

            } else if (player == 2) {
                tempBtn.setBackground(Color.BLUE);
            }
            playerBoard2.repaint();
            tempBtn.repaint();
            tempBtn.revalidate();
        }
        playerBoard2.setVisible(true);
    }
    public void disableButtons(ArrayList<JButton> btns) {
        for(int i=0; i < 25; i++) {
            JButton tempBtn = btns.get(i);
            tempBtn.setEnabled(false);
        }
    }
    public void nextTurn(Player nextPlayer) {
        ArrayList<String> clickedSpots = nextPlayer.getSpots();
        ArrayList<String> clickedHotSpots = nextPlayer.getCaughtHotspots();

        for(int i=0; i < clickedSpots.size(); i++) {
            int spot = Integer.parseInt(clickedSpots.get(i));
            buttons.get(spot).setEnabled(false);
        }
        for(int i=0; i < clickedHotSpots.size(); i++) {
            int spot = Integer.parseInt(clickedHotSpots.get(i));
            buttons.get(spot).setEnabled(false);
            buttons.get(spot).setBackground(Color.RED);
        }


    }

    public boolean testHotSpot(String btnClicked, Player opposingPlayer) {
        ArrayList<String> hotSpotArray = opposingPlayer.getChosenHotspots();

        for (int i=0; i < hotSpotArray.size();i++) {
            if (hotSpotArray.get(i).equals(btnClicked)) {
                return true;
            }
        }
        return false;
    }
    JFrame win = new JFrame();
    public void testWin(Player thisPlayer, String i) {
        if(thisPlayer.getHotSpotSize() == 5) {

            ActionListener endlisten = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand() =="restart") {
                        win.setVisible(false);
                        GUI restartAGame = new GUI();
                        Player player1 = new Player("Player 1");
                        Player player2 = new Player("Player 2");
                        restartAGame.init();
                        restartAGame.setBoard(player1, player2);
                    } else if (e.getActionCommand() == "quit") {
                        System.exit(10);
                    }
                }
            };
            playerBoard2.setVisible(false);
            new JOptionPane().showConfirmDialog(null, "Congratulation player" + i + "Wins!", null, JOptionPane.PLAIN_MESSAGE);


            JPanel grid = new JPanel(new GridLayout(3, 1, 5, 5));
            JButton restart = new JButton("restart");
            restart.addActionListener(endlisten);
            JButton quit = new JButton("quit");
            quit.addActionListener(endlisten);
            grid.add(new JLabel("Play a--gain or quit?"), BorderLayout.NORTH);
            grid.add(restart);
            grid.add(quit);
            win.add(grid);
            win.setVisible(true);
            win.setSize(200,300);
            win.pack();

        }
    }
    public void createErrorMessageBox(String _msg) {
        String msg = _msg;
        JOptionPane jop = new JOptionPane();
        jop.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}