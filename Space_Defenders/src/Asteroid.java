/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * circle of a specified color.
 */
public class Asteroid extends GameObj {
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 170;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 2;
    private int health = 50; 
    private boolean isHit = false;
    private long initialTime = 0; 
    private static final String IMG_FILE = "files/asteroid.png";
    private static BufferedImage asteroid; 

    private Color color;
    
    //Instantiates a basic asteroid object that moves in a random direction after it is positioned from the top of the frame randomly.
    public Asteroid(int courtWidth, int courtHeight, Color color, int health, int width, int height ) {
    	super(INIT_VEL_X, INIT_VEL_Y, Math.random()*600, 0, width, height, courtWidth, courtHeight);
    	this.health = health;
    	Random random = new Random(); 
    	int degree = -360 + random.nextInt(720); 
    	this.setVx(4*Math.cos(Math.toRadians(degree)));
    	this.setVy(4*Math.sin(Math.toRadians(degree)));
        this.color = color;
        try {
            if (asteroid == null) {
                asteroid = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
            
        }
    }
    
    //draws the asteroid as a flashing red circle once it is hit by a byllet 
    @Override
    public void draw(Graphics g) {
    	if(isHit) {
    		 g.setColor(new Color(255, 0 , 0, 128));
    		 g.fillOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 5, (int) this.getHeight() - 5);
    		 g.setColor(Color.BLACK);
    		 g.drawOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 4, (int) this.getHeight() - 4);
    		 
    	}else {
        g.setColor(this.color);
        g.drawImage(asteroid, (int)this.getPx(), (int) this.getPy(), (int) this.getWidth(), (int) this.getHeight(), null); 
    	}
    }
    
    //reduces the health of the asteroid by 10 every time a bullet hits an asteroid 
    public void isHit() {
    	health-=10; 
    	isHit = true;
    	initialTime = System.nanoTime();
    }
    
    //sets up the animation when a bullet hits the asteroid, i.e. the asteroid flashes once a bullet has hit it 
    public void updateHitStatus() {
    	if(isHit) {
    	long timeDiff = (System.nanoTime() - initialTime)/1000000; 
    		if(timeDiff > 80) {
    			isHit = false;
    			initialTime = 0; 
    		}	
    	}
    }
    //getters and setters 
    public boolean getIsHit() {
    	return isHit; 
    }
    
    //checks if the health is less than 0
    public boolean handleCollision() {
    	return health <= 0; 
    }

	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
}