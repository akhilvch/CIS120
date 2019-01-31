/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class SpaceShip extends GameObj {
	private static final int SIZE = 30;
    private static final int INIT_POS_X = 300;
    private static final int INIT_POS_Y = 550;
    private static final int INIT_VEL_X = 0;
    private static final int INIT_VEL_Y = 0;
    private int health = 50; 
    private boolean isHit = false;
    private long initialTime = 0; 
    private LinkedList<Bullet> bullets = new LinkedList<Bullet>(); 
    private BufferedImage spaceShip = null; 
    Bullet tempBullet; 
    private Color color;

    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    
    //Constructs the player spaceship with the specified image and health
    public SpaceShip(int courtWidth, int courtHeight, Color color, int health) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        try {
    	    spaceShip = (ImageIO.read(new File("files/playerShip.png")));
    	} catch (IOException e) {
    		System.out.println("Invalid Image");
    	}
        this.color = color;
        this.health = health; 
       
    }
    
    //Decrements the player health by 10 every time an enemy bullet hits the player and also sets up the time difference 
    //that is necessary to provide the animation of being hit
    public void isHit() {
    	health-=10; 
    	isHit = true;
    	initialTime = System.nanoTime();
    }
    
    //Decrements the player health by 30 when a collision with an enemy occurs
    public void collided() {
    	health-=30; 
    }
    
    //Switches the boolean state to is not hit, and restores the original spaceship graphic instead of the red hit bubble
    public void updateHitStatus() {
    	if(isHit) {
    	long timeDiff = (System.nanoTime() - initialTime)/1000000; 
    		if(timeDiff > 80) {
    			isHit = false;
    			initialTime = 0; 
    		}	
    	}
    }

    //Draws a red hit bubble for 80 milliseconds when the player is hit, otherwise draws the spaceShips
    @Override
    public void draw(Graphics g) {
    	if(isHit) {
    		 g.setColor(new Color(255, 0 , 0, 128));
    		 g.fillOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 5, (int) this.getHeight() - 5);
    		 g.setColor(Color.BLACK);
    		 g.drawOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 4, (int) this.getHeight() - 4);
    	}
    	 g.drawImage(spaceShip, (int)this.getPx(), (int) this.getPy(), (int) this.getWidth(), (int) this.getHeight(), null); 
        for(int i = 0; i < bullets.size(); i++) {
    		tempBullet = bullets.get(i); 
    		tempBullet.draw(g); 
    	}
    }
    
    //gets the health
    public int getHealth() {
    	return this.health;  
    }
    
    //sets the player health
    public void setHealth(int health) {
    	this.health = health; 
    }
    
    //gets the player color
    public Color getColor() {
    	return color;
    }
    
    //updates the position of the bullets that are fired by the player (based on the bullets x and y velocities)
    public void updateBulletsPos() {
    	for(int i = 0; i < bullets.size(); i++) {
    		tempBullet = bullets.get(i);
    		if(tempBullet.getPy() < 3 || tempBullet.getPy() > 592 || tempBullet.getPx() > 592 || tempBullet.getPx() < 3) {
    			removeBullet(tempBullet); 
    		}
    		tempBullet.updatePos(); 
    	}
    }
    
    //updates the bullet list such that it is empty
    public void resetBullets() {
    	bullets = new LinkedList<Bullet>(); 
    }
    
    //adds a bullet to the linkedlist
    public void addBullet(Bullet block) {
    	bullets.add(block);
    }
    
    //removes a bullet from the linkedlist 
    public void removeBullet(Bullet block) {
    	bullets.remove(block);
    }
    
    //gets the bullet linked list 
    public LinkedList<Bullet> getBullets() {
    	return bullets;
    }
}