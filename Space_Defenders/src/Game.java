/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;

import java.awt.event.*;

import java.io.FileNotFoundException;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Space Defenders");
        /*try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("/Users/akhil/Downloads/maxresdefault.jpg")))));
		} catch (IOException e1) {
			e1.printStackTrace()
		}*/
        frame.setLocation(600, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("You Ready?");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status, "files/Levels.txt");
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton pause = new JButton("Pause");
        
        //Save button
        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					court.save();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
      
    
        //reset and start buttons
        final JButton reset = new JButton("Reset");
        final JButton start = new JButton("Start");
        JLabel score = new JLabel(); //score label
        JLabel health = new JLabel();  //health label
        JButton nextLevel = new JButton("nextLevel");
        nextLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.nextLevel();
            }
        });
        
        final JButton rules = new JButton("Rules");
        //load button
        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.load();
                reset.setVisible(true);
                save.setVisible(true); 
                //pause.setVisible(true);
                health.setVisible(true); 
                score.setVisible(true);
                start.setVisible(false);
                rules.setVisible(false);
               rules.setVisible(false);
            }
            
        });
       
       
        //Used in a timer to check when the level is done. If so, it displays the nextLevel button
         ActionListener stateListener = new ActionListener() {
             public void actionPerformed(ActionEvent actionEvent) {
                 score.setText("Score: " + court.getScore());
                 health.setText("Health: " + court.getHealth());
                 if(court.getLevelDone()) {
                	 nextLevel.setVisible(true);
                 }else {
                	 nextLevel.setVisible(false);
                 }
             }
         };
          
         Timer timer3 = new Timer(1, stateListener);
         timer3.start();
         
         
         //Button to display the instructions
         
         
         start.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
            	 court.setRules(false);
            	 reset.setVisible(true);
                 save.setVisible(true); 
                 //pause.setVisible(true);
                 health.setVisible(true); 
                 score.setVisible(true);
                 start.setVisible(false);
                 rules.setVisible(false);
                load.setVisible(false);
                 court.start(); 
             }
         });
       
        //Reset button(goes back to the homepage and sets status to you ready? 
         reset.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
            	 
            	nextLevel.setVisible(false);
            	reset.setVisible(false);
                 save.setVisible(false); 
                // pause.setVisible(false);
                 health.setVisible(false); 
                 score.setVisible(false);
                 nextLevel.setVisible(false);
                 start.setVisible(true);
                 load.setVisible(true);
                 rules.setVisible(true); 
            	court.start(); 
            	status.setText("You Ready?");
             	court.setStart(false); 
             	court.setPlaying(false); 
             }
         });
         
         //Pause button
         pause.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
            	 court.switchPause();
            	 court.pause();
             }
         });
         
         
         //Instructions action listener
         rules.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
            	 	court.setRules(true); 
 					court.repaint();
             }
         });
      
        control_panel.add(rules); 
        control_panel.add(start);
        control_panel.add(load); 
        control_panel.add(save); 
        //control_panel.add(pause); 
        control_panel.add(health); 
        control_panel.add(score); 
        control_panel.add(reset); 
        control_panel.add(nextLevel); 
        nextLevel.setVisible(false);
        reset.setVisible(false);
        save.setVisible(false); 
        //pause.setVisible(false);
        health.setVisible(false); 
        score.setVisible(false);
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        
        // Start game
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}