/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private SpaceShip player; // the player spaceship, keyboard control
    private LinkedList<Asteroid> enemies = new LinkedList<Asteroid>(); 
    private BufferedImage background = null; 
    private BufferedImage homepage = null; 
    private BufferedImage endpage = null; 
    private BufferedImage losepage = null; 
    private BufferedImage instructions = null;
    private BufferedReader r; //reader for the level file
    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
	private int numOfAsteroids; //number of asteroid enemies
	private int numOfEnemyShips; //number of enemyship enemies
	private int numOfAdvancedShips; //number of advancedship enemies
    // Game constants
    private static final int COURT_WIDTH = 600;
    private static final int COURT_HEIGHT = 600;
    private static int score = 0; 
    private static int numOfSeconds = 0;  
    private static int numOfLevels = 10;
    private static boolean levelDone = false; 
    // Update interval for timer, in milliseconds
    private static final int INTERVAL = 25;
    private static int level = 1; 
    private String src; //this is the file for the levels
	private boolean gameDone;  //checks when the game is done
	private boolean start = false;  //checks if that start button is pressed
	private boolean isSaved = false; //checks if the gave is saved
	private boolean isDead = false; //checks if the player is dead
	private boolean rules = false; //checks if the rules are clicked
	Timer timer; 
	Timer enemyTimer;
	private boolean pauseClicked = false; 
	
	
	//Instantiates a GameCourt object
    public GameCourt(JLabel status, String src) throws IllegalArgumentException{ 	
    	if(status == null || src == null) {
    		throw new IllegalArgumentException(); 
    	}
    	player = new SpaceShip(COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 60);
    	try {
    	    background = (ImageIO.read(new File("files/maxresdefault.jpg")));
    	    homepage = (ImageIO.read(new File("files/homepagefinal.png")));
    	    endpage = (ImageIO.read(new File("files/endpage.png")));
    	    losepage = (ImageIO.read(new File("files/Rip.png")));
    	    instructions = ImageIO.read(new File("files/rules.png"));
    	    this.src = src; 
    	}catch (FileNotFoundException e1) {
			System.out.println("File Not Found");
			e1.printStackTrace();
		}catch (IOException e) {
    		System.out.println("Invalid Image");
    	}
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
  
       //This timer is for the enemyShips and allows them to shoot a bullet every single second 
       enemyTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//nextLevel(); 
                for(Asteroid s : enemies) {
                	if(s instanceof AdvancedShip && s instanceof AdvancedShip) {
                		((AdvancedShip) s).shoot(player.getPx(), player.getPy()); 
                	}
                	else if(s instanceof EnemyShip && !(s instanceof AdvancedShip)) {
                		((EnemyShip) s).shoot(); 
                	}
                }
            }
        });
        //timer.start(); // MAKE SURE TO START THE TIMER!
       // enemyTimer.start(); 
        //scoreTimer.start(); 
        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the player to move as long as an arrow key is pressed, by
        // changing the player's velocity accordingly. (The tick method below actually moves the
        // player.)
        
        
        addMouseListener(new MouseAdapter() {
        	Bullet temp; 
        	int x;
        	int y;
        	
        	//gets the x and the y positions of the mouse
        	public void mousePressed(MouseEvent e) {
        		x = e.getX();
        		y = e.getY();      	
            } 
   
        	//finds the angle between the mouseclick the player and fires a bullet in the direction of the mouseclick
        	public void mouseReleased(MouseEvent e) {
        		double playerPosX = player.getPx();
        		double playerPosY = player.getPy();
        		temp = new Bullet((int) playerPosX, (int) playerPosY, COURT_WIDTH, COURT_HEIGHT, Color.WHITE, 5);
        		temp.setPx(player.getPx() + 15);
        		temp.setPy(player.getPy() + 15);
        		double vx; 
        		double vy; 
        		if(x <= playerPosX + 2) {
        			 vx = -(8*Math.cos(Math.atan((y - playerPosY - 15)/(x - playerPosX - 15))));
        			 vy = -(8*Math.sin(Math.atan((y - playerPosY - 15)/(x - playerPosX -15))));
        		}else {
        			vx = (8*Math.cos(Math.atan((y - playerPosY - 15)/(x - playerPosX - 15))));
        			vy = (8*Math.sin(Math.atan((y - playerPosY - 15)/(x - playerPosX - 15))));
        		}
            	temp.setVx(vx); 
            	temp.setVy(vy); 
        		player.addBullet(temp);
        	}
        	
        });
        
        
        //Keeps increasing players direction in the direction specificed by the Arrow Keys until it hits the players 
        //maximum velocity(which is written in gameObj class
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                	System.out.println("left is registered");
                    player.setAccX(-1);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                	player.setAccX(1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                	player.setAccY(1);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                	player.setAccY(-1);        	
                } 
            }
            
       

            public void keyReleased(KeyEvent e) {
                player.setAccX(0);
                player.setAccY(0);
            }
        });
        

        this.status = status;
        repaint(); 
    }
    
    //setters and getters 
    public SpaceShip getPlayer() {
    	return player; 
    }
    
    
    public boolean getIsSaved() {
    	return isSaved; 
    }
    
    public int getLevel() {
		return level;
	}

	public void setRules(boolean b) {
		rules = b; 
	}
	
	public void setPlaying(boolean b) {
		playing = b; 	
	}
	
	public void switchPause() {
		pauseClicked = !pauseClicked; 
	}
	
	public boolean getGameDone() {
		return gameDone; 
	}
	
	public LinkedList<Asteroid> getEnemies() {
	    	return enemies; 
	}
	
	 public int getScore() {
	    	return score; 
	 }
	    
	    public int getHealth() {
	    	return player.getHealth(); 
	}
	    
	  public boolean getLevelDone() {
	    	return levelDone; 
	    }
	    
	 public void setStart(boolean b) {
	    	start = b;
	 }
	 
	 
	//sets up fields and calls the reset method, which begins the base level
    public void start() {
    	start = true; 
    	isDead = false;
    	timer.start();
        enemyTimer.start();
    	levelDone = false; 
    	isSaved = false; 
    	repaint(); 
    	reset(); 
    }

    /**
     * (Re-)set the game to its initial state.
     */
    
    //this method starts up the first level manually by adding a single asteroid and a single enemyShip. But it also sets up FileIO
    // for the nextLevel() method such that it reads from the Levels.txt file
    public void reset() {
    	if(!levelDone) {
    	level = 1;
    	score = 0;
    	playing = true;
    	player.setHealth(60); 
        player.setPx(300); 
        player.setPy(550); 
        player.setVx(0); 
        player.setVy(0); 
        player.resetBullets(); 
        enemies = new LinkedList<Asteroid>(); 
        try {
			r = new BufferedReader(new FileReader(src));
		} catch (FileNotFoundException e1) {
			System.out.println("File not found"); 
		}
        String line = ""; 
    	try {
    		 while(line != null) {
    				if(line.contains("Levels:")) {
    					break; 
    				}
    				line = r.readLine(); 
    		} 
    		 line = r.readLine();
    		 System.out.println("Line:" + line);
    	}catch(IOException e) {
    		System.out.println("Couldn't read line"); 
    	}
       
        for(int i = 0; i < 1; i++) { 
        	enemies.add(new Asteroid(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 40, 30, 30)); //adds single enemy
        }
        for(int i = 0; i < 1; i++) { 
        	enemies.add(new EnemyShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 40, 45, 45 )); //adds single enemy 
        }
        repaint(); 
        gameDone = false;
        status.setText("Level: " + level);
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    	}
    }
    
    //This method goes to the next level and adds the corresponding number of enemies of each type based on what it has read from the
    //levels file. The first number on each line is the number of asteroids, the second number on each line is the number of enemyships, and the
    //third number on each line is the number of advancedShips. Uses integer parsing and the .split method to access the fields
    public void nextLevel() {
    	level++; 
    	levelDone = false; 
    	player.resetBullets();
    	String line = ""; 
    	try {		
    		line = r.readLine(); 
    	}catch(IOException e) {
    		System.out.println("Couldn't read line"); 
    	}
    	player.setPx(300);
    	player.setPy(550);
    	player.setVx(0);
    	player.setVy(0);
    	if(line == null || line.equals("")) {
    		playing = false;
    		gameDone = true;
    		repaint(); 
    		status.setText("CONGRATULATIONS!!! YOU HAVE BEAT THE GAME!");
    		try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		return; 
    	}
    	String[] numbers = line.split(",");
    	numOfAsteroids = Integer.parseInt(numbers[0].trim()); 
    	numOfEnemyShips = Integer.parseInt(numbers[1].trim());
    	numOfAdvancedShips = Integer.parseInt(numbers[2].trim()); 
    	
    	System.out.println("numOfAsteroids:" + numOfAsteroids + " numOfEnemyShips:" + numOfEnemyShips);
 
        enemies = new LinkedList<Asteroid>(); 
        for(int i = 0; i < numOfAsteroids; i++) { 
        	enemies.add(new Asteroid(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 40, 30, 30));
        }
        for(int i = 0; i < numOfEnemyShips; i++) { 
        	enemies.add(new EnemyShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 40, 45, 45 ));
        }
        for(int i = 0; i < numOfAdvancedShips; i++) {
        	enemies.add(new AdvancedShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 20, 35, 35)); 
        }
        player.setHealth(player.getHealth() + 10);
        playing = true;
        status.setText("Level: " + level);
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();	
    	
    }
    
    //Saves the game state, specifically the velocities, positions, and health of each enemy and the player as well as the current score.
    //This writes to a file called SavedGame.txt and uses a colon separated format as well as English text. 
    public void save() throws FileNotFoundException {
    	isSaved = true;
    	playing = false;
    	File fout = new File("files/SavedGame.txt");
    	FileOutputStream fos = new FileOutputStream(fout);
     
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    	try {	bw.write("LevelDone: " + levelDone);
    			bw.newLine();
				bw.write("Score: " + score);
				bw.newLine();	 
				bw.write("Health: " + player.getHealth()); 
				bw.newLine();
				bw.write("Level: " + level);
				bw.newLine(); 
				bw.write("PlayerX: " + player.getPx());
				bw.newLine(); 
				bw.write("PlayerY: " + player.getPy());
				bw.newLine(); 
				bw.write("PlayerVx: " + player.getVx());
				bw.newLine(); 
				bw.write("PlayerVy: " + player.getVy());
				bw.newLine(); 
				int remAsteroids = 0;
				int remEnemyShip = 0;
				int remAdvancedShip = 0; 
				for(Asteroid e : enemies) {		
					if(e instanceof AdvancedShip) {
						remAdvancedShip++;
						bw.write("advancedShip " + remEnemyShip); 
						bw.write(": " + e.getPx()); 
						bw.write(": " + e.getPy());
						bw.write(": " + e.getVx());
						bw.write(": " + e.getVy());
						bw.write(": " + e.getHealth()); 
						bw.newLine(); 
					}else if(e instanceof EnemyShip && !(e instanceof AdvancedShip)) {
						remEnemyShip++;						
						bw.write("enemyShip " + remEnemyShip); 
						bw.write(": " + e.getPx()); 
						bw.write(": " + e.getPy());
						bw.write(": " + e.getVx()); 
						bw.write(": " + e.getVy());
						bw.write(": " + e.getHealth()); 
						bw.newLine(); 
					}else {
						remAsteroids++;						
						bw.write("asteroid " + remEnemyShip); 
						bw.write(": " + e.getPx()); 
						bw.write(": " + e.getPy());
						bw.write(": " + e.getVx()); 
						bw.write(": " + e.getVy());
						bw.write(": " + e.getHealth()); 
						bw.newLine(); 
					}
				}
    	bw.close(); 
    	}catch(IOException e) {
   	
    	}
    }
    
    //This method loads the previously saved gamestate and uses the .split() string method to access particular fields. It also repaints the frame 
    //and allows the player to start right where he left off before. 
    public void load() {
    	isSaved = false;
    	start = true;
    	File fin = new File("files/SavedGame.txt"); 
    	BufferedReader br = null;
    	try {
    		br = new BufferedReader(new FileReader(fin));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String line;
    	boolean isLevelDone = false;
    	int currLevel = 0; 
    	int currScore = 0; 
    	int currHealth = 0; 
    	double playerX = 0; 
    	double playerY = 0; 
    	double playerVx = 0; 
    	double playerVy = 0; 
    	LinkedList<Asteroid> remainingEnemies = new LinkedList<Asteroid>();
		try {
			line = br.readLine();
			while(line != null) {
				if(line.contains("LevelDone:")) {
					String[] splitStrings= line.split(":");
	    			 isLevelDone = Boolean.parseBoolean(splitStrings[1].trim()); 
				}
				if(line.contains("Score:")) {
	    			String[] splitStrings= line.split(":");
	    			 currScore = Integer.parseInt(splitStrings[1].trim()); 
	    		}
				if(line.contains("Health:")) {
	    			String[] splitStrings= line.split(":");
	    			 currHealth = Integer.parseInt(splitStrings[1].trim()); 
				}
	    		if(line.contains("Level:")) {
	    			String[] splitStrings= line.split(":");
	    			 currLevel = Integer.parseInt(splitStrings[1].trim()); 
	    		}
	    		if(line.contains("PlayerX:")) {
	    			String[] splitStrings= line.split(":");
	    			playerX = Double.parseDouble(splitStrings[1].trim()); 
	    		}
	    		if(line.contains("PlayerY:")) {
	    			String[] splitStrings= line.split(":");
	    			playerY = Double.parseDouble(splitStrings[1].trim()); 
	    		}
	    		if(line.contains("PlayerVx:")) {
	    			String[] splitStrings= line.split(":");
	    			playerVx = Double.parseDouble(splitStrings[1].trim()); 
	    		}
	    		if(line.contains("PlayerVy:")) {
	    			String[] splitStrings= line.split(":");
	    			playerVy = Double.parseDouble(splitStrings[1].trim()); 
	    		}
	    		if(line.contains("asteroid")) {
	    			String[] splitStrings= line.split(":"); 
	    			Asteroid asteroid = new Asteroid(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 10, 30, 30);
	    			asteroid.setPx(Double.parseDouble(splitStrings[1].trim()));
	    			asteroid.setPy(Double.parseDouble(splitStrings[2].trim()));
	    			asteroid.setVx(Double.parseDouble(splitStrings[3].trim()));
	    			asteroid.setVy(Double.parseDouble(splitStrings[4].trim()));		
	    			asteroid.setHealth(Integer.parseInt(splitStrings[5].trim()));	
	    			remainingEnemies.add(asteroid);    			
	    		}
	    		
	    		if(line.contains("enemyShip")) {
	    			String[] splitStrings= line.split(":"); 
	    			EnemyShip ship = new EnemyShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 10, 45, 45);
	    			ship.setPx(Double.parseDouble(splitStrings[1].trim()));
	    			ship.setPy(Double.parseDouble(splitStrings[2].trim()));
	    			ship.setVx(Double.parseDouble(splitStrings[3].trim()));
	    			ship.setVy(Double.parseDouble(splitStrings[4].trim()));	
	    			ship.setHealth(Integer.parseInt(splitStrings[5].trim()));	
	    			remainingEnemies.add(ship);
	    		}
	    		
	    		if(line.contains("advancedShip")) {
	    			String[] splitStrings= line.split(":"); 
	    			AdvancedShip ship = new AdvancedShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 10, 45, 45);
	    			ship.setPx(Double.parseDouble(splitStrings[1].trim()));
	    			ship.setPy(Double.parseDouble(splitStrings[2].trim()));
	    			ship.setVx(Double.parseDouble(splitStrings[3].trim()));
	    			ship.setVy(Double.parseDouble(splitStrings[4].trim()));	
	    			ship.setHealth(Integer.parseInt(splitStrings[5].trim()));	
	    			remainingEnemies.add(ship);
	    		}	
	    		line = br.readLine();
	    	}
			if(isLevelDone) {
				System.out.println("Saved to the next level");
				currLevel+=1; 
			}
			start(); 
			while(level < currLevel) {
				nextLevel();  
			}
			if(!isLevelDone) {
				player.setPx(playerX);
				player.setPy(playerY);
				player.setVx(playerVx);
				player.setVy(playerVy);
				enemies = remainingEnemies;
				/*enemies = new LinkedList<Asteroid>();
				for(int i = 0; i < numAsteroids; i++) { 
		        	enemies.add(new Asteroid(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 10, 30, 30));
		        }
		        for(int i = 0; i < numEnemyShips; i++) { 
		        	enemies.add(new EnemyShip(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW, 10, 45, 45 ));
		        }*/
			}
			player.setHealth(currHealth);
			score = currScore;
			player.resetBullets();
			repaint(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    //This method updates every time the timer is called and updates the velocities of the player, the enemies, and the bullets. 
    //It also continuously checks for bullet-player, bullet-enemy, and player-enemy collisions and updates the respective fields such
    //as the score and the health 
    void tick() {
        if (playing) {
            // advance the player and enemy in their current direction.
        	player.updateVelocity();
        	player.updateBulletsPos();
            player.move();
            for(int j = 0; j < enemies.size(); j++) {
            	Asteroid enemy = enemies.get(j); 
            	enemy.move();
            	/*for(Enemy enemy2 : enemies) {
            		enemy.bounce(enemy.hitObj(enemy2));
            	}*/
            	
            	enemy.bounce(enemy.hitWall());	
            	if(enemy instanceof EnemyShip) {	
            		EnemyShip enemyShip = (EnemyShip) enemy;
            		enemyShip.updateBulletsPos(); 
            		for(int i = 0; i < enemyShip.getBullets().size(); i++){
            			if(enemyShip.getBullets().get(i).Intersects(player)) {
            			player.isHit();
            			
            			enemyShip.removeBullet(enemyShip.getBullets().get(i));	
            			}    
            		}
            	}
            	
            	if(player.getHealth() <= 0) {
        			playing = false;
        			player.resetBullets();
        			gameDone = true; 
        			isDead = true;
        			repaint();
        			System.out.println("THIS IS GAME DONE:");
        			status.setText("You Lose! Good Try Though. Your Final Score is " + score);
        		}
            	player.updateHitStatus();

            // make the enemy bounce off walls...
            // enemy.bounce(enemy.hitWall());
            // ...and the mushroom
            // enemy.bounce(enemy.hitObj(poison));

            // check for the game end conditions
            for(int i = 0; i < player.getBullets().size(); i++) {         	
            	if(player.getBullets().get(i).Intersects(enemy)) {
            		enemy.isHit(); 
            		score+=10; 
            		if(enemy.handleCollision() == true) {		
            			enemies.remove(j);
            			score+=50; 
            		 }
            		
            		player.removeBullet(player.getBullets().get(i));		
            	}
            }
      
            enemy.updateHitStatus();
            if (player.Intersects(enemy)) {
                player.collided();
                enemies.remove(enemy);    
                if(player.getHealth() <= 0) {
        			playing = false;
        			isDead = true;
        			gameDone = true;
        			repaint();
        			status.setText("You Lose! Good Try Though. Your Final Score is " + score);
        			return;
        		}    
                                     
            } 
          }
            if(enemies.isEmpty()) {
    			playing = false;
    			//gameDone = true;
    			levelDone = true; 
    			repaint();
    			status.setText("Great Job! Press nextLevel to go to the next Level");
    			return;
    		}
            // update the display
            repaint();
        }
    }
    
   
    //The paintComponent method draws specific backgrounds based on whether or not a specific button was pressed (this is kept track of by boolean
    //fields). 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       // g.drawImage(background, 0, 0, null); 
        if(rules && !start) {
        	g.drawImage(instructions, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
        }
        else if(!start) {
        	g.drawImage(homepage, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
        }
        else if(!gameDone) {
        	g.drawImage(background, 0, 0, null); 
        	player.draw(g);
            // poison.draw(g);
             for(Asteroid enemy : enemies) {
             	enemy.draw(g);
             }
        }else if(gameDone && isDead) {
        	setBackground(Color.RED); 
        	g.drawImage(losepage, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
    	}else if(gameDone && !isDead){
        	g.drawImage(endpage, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
	
    
    //IGNORE THIS 
	public void pause(){	
		if(pauseClicked) {
			if(!gameDone && !levelDone && !isSaved) {
				timer.stop(); 
			}
		} else {
			timer.start(); 
		}
	}
	
	
	
	//These are test methods that are called in the GameTest.java class 
	public void testHealth() {
		player.isHit(); 
	}
	
	public void testLevelDone() {
		enemies = new LinkedList<Asteroid>(); 
	}
	
	public void testTick() {
		setPlaying(false);
	}
	
	public void testCollisions() {
		player.collided();
		player.collided();
		player.collided();
	}
}
