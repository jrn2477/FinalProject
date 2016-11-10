/** 
*  @author: Lauren Hoffman
*  @version: 10-17-16
**/

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.*;

/**
*  @author: Lauren Hoffman
*  @version: 10-1-16
*  Creates a new UpperBoard object.
**/
public class UpperBoard extends JFrame implements ActionListener
{
   private int numMoves;
   private boolean[][] ships;
   private ArrayList <JButton> buttons = new ArrayList <JButton>();
   JLabel p1;
   JLabel resultLabel = new JLabel("Result:", SwingConstants.RIGHT);
   JTextField resultField = new JTextField(5); //stores the hit or miss results
  
   //Row A(1) buttons
   JButton A1 = new JButton("A1");
   JButton A2 = new JButton("A2");
   JButton A3 = new JButton("A3");
   JButton A4 = new JButton("A4");
   JButton A5 = new JButton("A5");
   JButton A6 = new JButton("A6");
   JButton A7 = new JButton("A7");
   JButton A8 = new JButton("A8");

//Row B(2) buttons
   JButton B1 = new JButton("B1");
   JButton B2 = new JButton("B2");
   JButton B3 = new JButton("B3");
   JButton B4 = new JButton("B4");
   JButton B5 = new JButton("B5");
   JButton B6 = new JButton("B6");
   JButton B7 = new JButton("B7");
   JButton B8 = new JButton("B8");

//Row C(3) buttons
   JButton C1 = new JButton("C1");
   JButton C2 = new JButton("C2");
   JButton C3 = new JButton("C3");
   JButton C4 = new JButton("C4");
   JButton C5 = new JButton("C5");
   JButton C6 = new JButton("C6");
   JButton C7 = new JButton("C7");
   JButton C8 = new JButton("C8");

//Row D(4) buttons
   JButton D1 = new JButton("D1");
   JButton D2 = new JButton("D2");
   JButton D3 = new JButton("D3");
   JButton D4 = new JButton("D4");
   JButton D5 = new JButton("D5");
   JButton D6 = new JButton("D6");
   JButton D7 = new JButton("D7");
   JButton D8 = new JButton("D8");
   
   //Row E(5) buttons
   JButton E1 = new JButton("E1");
   JButton E2 = new JButton("E2");
   JButton E3 = new JButton("E3");
   JButton E4 = new JButton("E4");
   JButton E5 = new JButton("E5");
   JButton E6 = new JButton("E6");
   JButton E7 = new JButton("E7");
   JButton E8 = new JButton("E8");

//Row F(6) buttons
   JButton F1 = new JButton("F1");
   JButton F2 = new JButton("F2");
   JButton F3 = new JButton("F3");
   JButton F4 = new JButton("F4");
   JButton F5 = new JButton("F5");
   JButton F6 = new JButton("F6");
   JButton F7 = new JButton("F7");
   JButton F8 = new JButton("F8");

//Row G(7) buttons
   JButton G1 = new JButton("G1");
   JButton G2 = new JButton("G2");
   JButton G3 = new JButton("G3");
   JButton G4 = new JButton("G4");
   JButton G5 = new JButton("G5");
   JButton G6 = new JButton("G6");
   JButton G7 = new JButton("G7");
   JButton G8 = new JButton("G8");

//Row H(8) buttons
   JButton H1 = new JButton("H1");
   JButton H2 = new JButton("H2");
   JButton H3 = new JButton("H3");
   JButton H4 = new JButton("H4");
   JButton H5 = new JButton("H5");
   JButton H6 = new JButton("H6");
   JButton H7 = new JButton("H7");
   JButton H8 = new JButton("H8");
      
   public UpperBoard(boolean[][] s, String title)
   {
      p1 = new JLabel(title, SwingConstants.LEFT);
      numMoves = 0;
      ships = s;
      setLayout(new BorderLayout());
      setSize(700,500);
      setLocation(150,100);
      setTitle("Battleship GUI");
      
      //North panel
      JPanel jpNorth = new JPanel();
      jpNorth.add(p1);
      add(jpNorth,BorderLayout.NORTH);
      
      //Panel with results field
      JPanel jpSouth = new JPanel(new GridLayout(0,2));
      jpSouth.add(resultLabel);
      jpSouth.add(resultField);
      add(jpSouth, BorderLayout.SOUTH);
      
      JPanel jpWest = new JPanel(new GridLayout(8,8));
      
      buttons.add(A1);//0
      buttons.add(A2);
      buttons.add(A3);
      buttons.add(A4);
      buttons.add(A5);
      buttons.add(A6);
      buttons.add(A7);
      buttons.add(A8);
   
      buttons.add(B1);//8
      buttons.add(B2);
      buttons.add(B3);
      buttons.add(B4);
      buttons.add(B5);
      buttons.add(B6);
      buttons.add(B7);
      buttons.add(B8);
   
      buttons.add(C1);//16
      buttons.add(C2);
      buttons.add(C3);
      buttons.add(C4);
      buttons.add(C5);
      buttons.add(C6);
      buttons.add(C7);
      buttons.add(C8);
   
      buttons.add(D1);//24
      buttons.add(D2);
      buttons.add(D3);
      buttons.add(D4);
      buttons.add(D5);
      buttons.add(D6);
      buttons.add(D7);
      buttons.add(D8);
   
      buttons.add(E1);
      buttons.add(E2);
      buttons.add(E3);
      buttons.add(E4);
      buttons.add(E5);
      buttons.add(E6);
      buttons.add(E7);
      buttons.add(E8);
   
      buttons.add(F1);
      buttons.add(F2);
      buttons.add(F3);
      buttons.add(F4);
      buttons.add(F5);
      buttons.add(F6);
      buttons.add(F7);
      buttons.add(F8);
   
      buttons.add(G1);
      buttons.add(G2);
      buttons.add(G3);
      buttons.add(G4);
      buttons.add(G5);
      buttons.add(G6);
      buttons.add(G7);
      buttons.add(G8);
   
      buttons.add(H1);
      buttons.add(H2);
      buttons.add(H3);
      buttons.add(H4);
      buttons.add(H5);
      buttons.add(H6);
      buttons.add(H7);
      buttons.add(H8);
      
      //buttons added to west panel
      jpWest.add(A1);
      jpWest.add(A2);
      jpWest.add(A3);
      jpWest.add(A4);
      jpWest.add(A5);
      jpWest.add(A6);
      jpWest.add(A7);
      jpWest.add(A8);
               
      jpWest.add(B1);
      jpWest.add(B2);
      jpWest.add(B3);
      jpWest.add(B4);
      jpWest.add(B5);
      jpWest.add(B6);
      jpWest.add(B7);
      jpWest.add(B8);
   
      jpWest.add(C1);
      jpWest.add(C2);
      jpWest.add(C3);
      jpWest.add(C4);
      jpWest.add(C5);
      jpWest.add(C6);
      jpWest.add(C7);
      jpWest.add(C8);
      
      jpWest.add(D1);
      jpWest.add(D2);
      jpWest.add(D3);
      jpWest.add(D4);
      jpWest.add(D5);
      jpWest.add(D6);
      jpWest.add(D7);
      jpWest.add(D8);
   
      jpWest.add(E1);
      jpWest.add(E2);
      jpWest.add(E3);
      jpWest.add(E4);
      jpWest.add(E5);
      jpWest.add(E6);
      jpWest.add(E7);
      jpWest.add(E8);
      
      jpWest.add(F1);
      jpWest.add(F2);
      jpWest.add(F3);
      jpWest.add(F4);
      jpWest.add(F5);
      jpWest.add(F6);
      jpWest.add(F7);
      jpWest.add(F8);
   
      jpWest.add(G1);
      jpWest.add(G2);
      jpWest.add(G3);
      jpWest.add(G4);
      jpWest.add(G5);
      jpWest.add(G6);
      jpWest.add(G7);
      jpWest.add(G8);
      
      jpWest.add(H1);
      jpWest.add(H2);
      jpWest.add(H3);
      jpWest.add(H4);
      jpWest.add(H5);
      jpWest.add(H6);
      jpWest.add(H7);
      jpWest.add(H8);
   
      
   
      //add action listeners
      A1.addActionListener(this);
      A2.addActionListener(this);
      A3.addActionListener(this);
      A4.addActionListener(this);
      A5.addActionListener(this);
      A6.addActionListener(this);
      A7.addActionListener(this);
      A8.addActionListener(this);
      //A1.setActionCommand("hit"); //these 2 setActionCommands are for testing 
      //A2.setActionCommand("miss");
      
      B1.addActionListener(this);
      B2.addActionListener(this);
      B3.addActionListener(this);
      B4.addActionListener(this);
      B5.addActionListener(this);
      B6.addActionListener(this);
      B7.addActionListener(this);
      B8.addActionListener(this);
      
      C1.addActionListener(this);
      C2.addActionListener(this);
      C3.addActionListener(this);
      C4.addActionListener(this);
      C5.addActionListener(this);
      C6.addActionListener(this);
      C7.addActionListener(this);
      C8.addActionListener(this);
      
      D1.addActionListener(this);
      D2.addActionListener(this);
      D3.addActionListener(this);
      D4.addActionListener(this);
      D5.addActionListener(this);
      D6.addActionListener(this);
      D7.addActionListener(this);
      D8.addActionListener(this);
      
      E1.addActionListener(this);
      E2.addActionListener(this);
      E3.addActionListener(this);
      E4.addActionListener(this);
      E5.addActionListener(this);
      E6.addActionListener(this);
      E7.addActionListener(this);
      E8.addActionListener(this);
      
      F1.addActionListener(this);
      F2.addActionListener(this);
      F3.addActionListener(this);
      F4.addActionListener(this);
      F5.addActionListener(this);
      F6.addActionListener(this);
      F7.addActionListener(this);
      F8.addActionListener(this);
      
      G1.addActionListener(this);
      G2.addActionListener(this);
      G3.addActionListener(this);
      G4.addActionListener(this);
      G5.addActionListener(this);
      G6.addActionListener(this);
      G7.addActionListener(this);
      G8.addActionListener(this);
      
      H1.addActionListener(this);
      H2.addActionListener(this);
      H3.addActionListener(this);
      H4.addActionListener(this);
      H5.addActionListener(this);
      H6.addActionListener(this);
      H7.addActionListener(this);
      H8.addActionListener(this);
      
      add(jpWest,BorderLayout.WEST);
       
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setVisible(true);
   }
   /**
   *   @author Nick Kowalczuk, Lauren Hoffman
   *   @version 10-20-16
   *   @param ae An ActionEvent, all action events in this class are going to be buttons.
   */
   public void actionPerformed(ActionEvent ae)
   {
      numMoves++;
      int r;
      int c = Character.getNumericValue(ae.getActionCommand().charAt(1)) - 1;
      switch (ae.getActionCommand().charAt(0)){
         case 'A':   r = 0;
            break;
         case 'B':   r = 1;
            break;
         case 'C':   r = 2;
            break;
         case 'D':   r = 3;
            break;
         case 'E':   r = 4;
            break;
         case 'F':   r = 5;
            break;
         case 'G':   r = 6;
            break;
         case 'H':   r = 7;
            break;
         default:    r = 0;
            break;
      }
      int indexInArrayList = (r*8)+(c);
      if(ships[r][c]){
         ships[r][c] = false;
         buttons.get(indexInArrayList).setBackground(Color.red);
         buttons.get(indexInArrayList).setOpaque(true);
         buttons.get(indexInArrayList).setBorderPainted(false);
         buttons.get(indexInArrayList).setEnabled(false);
         resultField.setText("Hit!");
         if(!shipsRemaining()){
            JOptionPane.showMessageDialog(null, "Game Over \nMoves Performed: "+ numMoves,"Game Over", JOptionPane.INFORMATION_MESSAGE);
         }
      }
      else{
         buttons.get(indexInArrayList).setBackground(Color.blue);
         buttons.get(indexInArrayList).setOpaque(true);
         buttons.get(indexInArrayList).setBorderPainted(false);
         buttons.get(indexInArrayList).setEnabled(false);
         resultField.setText("Miss");
      }
   }
   /**
   *   @author Nick Kowalczuk
   *   @version 10-18-16
   *   @return true if there are any remaining true values in the array storing the ship locations. false otherwise
   *   This method will return true if there are any true values in ships[][]. if this returns false, the game is over.
   */
   public boolean shipsRemaining(){
      boolean shipsRemain = false;
      for(int j = 0; j < 7; j++){
         for(int k = 0; k < 7; k++){
            if(ships[j][k])
               shipsRemain = true;
         }
      }
      return shipsRemain;
   }
   
   /**
   *  @author: Lauren Hoffman
   *  @version: 10-1-16
   *  Creates a new UpperBoard for testing purposes
   **/
   public static void main(String [] args)
   {
      boolean[][] locations = new boolean[][]{ //added to accommodate modified constructor.
         {false, false, false, true, true, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         {false, false, false, false, false, false, false, false},
         }; 
      
      new UpperBoard(locations,"test");
   }
}