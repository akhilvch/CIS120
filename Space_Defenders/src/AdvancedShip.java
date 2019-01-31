import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class AdvancedShip extends EnemyShip {
	public static final String IMG_FILE = "files/advancedShip.png";
	 public static BufferedImage ship; 
	 
	//Instantiates an AdvancedShip object 
	public AdvancedShip(int courtWidth, int courtHeight, Color color, int health, int width, int height) {
		super(courtWidth, courtHeight, color, health, width, height);
		try {
            if (ship == null) {
                ship = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
            
        }	
	}
	
	//Shoots in the direction of the particular x and y given. In the case of the game, these x and y positions are the positions of 
	//the player. This method is used within a timer. 
	public void shoot(double playerPosX, double playerPosY) {
		double currPx = this.getPx(); 
		double currPy = this.getPy(); 
		double vx; 
		double vy; 
		if(currPx <= playerPosX + 2) {
			 vx = (6*Math.cos(Math.atan((playerPosY - currPy + 2)/(playerPosX - currPx + 2))));
			 vy = (6*Math.sin(Math.atan((playerPosY - currPy + 2)/(playerPosX - currPx + 2))));
		}else {
			vx = -(6*Math.cos(Math.atan((playerPosY - currPy + 2)/(playerPosX - currPx + 2))));
			vy = -(6*Math.sin(Math.atan((playerPosY - currPy + 2)/(playerPosX - currPx + 2))));
		}
    	//System.out.println("Degree: " + degree);
		tempBullet = new Bullet((int) this.getPx() + 10, (int) this.getPy() + 15, 600, 600, new Color(0,255,10), 7);
		tempBullet.setVx(vx);
    	tempBullet.setVy(vy);
    	//System.out.println("vx:" + 4*Math.cos(Math.toRadians(degree)) +  "vy:" + 4*Math.sin(Math.toRadians(degree)));
		this.addBullet(tempBullet);
	}

	//Draws the advanced ship, making sure that it turns into a red circle once hit by a player's bullet 
	@Override
	 public void draw(Graphics g) {
    	if(this.getIsHit()) {
    		 g.setColor(new Color(255, 0 , 0, 128));
    		 g.fillOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 15, (int) this.getHeight() - 15);
    		 g.setColor(Color.BLACK);
    		 g.drawOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 15, (int) this.getHeight() - 15);
    		 for(int i = 0; i < super.getBullets().size(); i++) {
 	    		tempBullet = getBullets().get(i); 
 	    		tempBullet.draw(g); 
 	    	}    		 
    	}else {
    	g.drawImage(ship, (int)this.getPx(), (int) this.getPy(), (int) this.getWidth(), (int) this.getHeight(), null); 
    	for(int i = 0; i < getBullets().size(); i++) {
    		tempBullet = getBullets().get(i); 
    		tempBullet.draw(g); 
    	}
    	}	  
    }
	
}
