import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

public class EnemyShip extends Asteroid{
	public static final String IMG_FILE = "files/enemyShip.png";
    public static BufferedImage ship; 
    Bullet tempBullet; 
    protected LinkedList<Bullet> bullets = new LinkedList<Bullet>(); 
    
    //This is a basic enemy ship that shoots a bulelt randomly. It also appears randomly at the top just like an asteroid 
	public EnemyShip(int courtWidth, int courtHeight, Color color, int health, int width, int height) {
		super(courtWidth, courtHeight, color, health, width, height);
		try {
            if (ship == null) {
                ship = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
            
        }	
	}
	
	//This is the method that adds a bullet with a random direction. Uses trigonometry to orient a bullet in a random direction 
	public void shoot() {
		Random random = new Random(); 
		int degree = -360 + random.nextInt(720); 
    	//System.out.println("Degree: " + degree);
		tempBullet = new Bullet((int) this.getPx() + 10, (int) this.getPy() + 15, 600, 600, new Color(0,255,10), 7);
		tempBullet.setVx(8*Math.cos(Math.toRadians(degree)));
    	tempBullet.setVy(8*Math.sin(Math.toRadians(degree)));
    	//System.out.println("vx:" + 4*Math.cos(Math.toRadians(degree)) +  "vy:" + 4*Math.sin(Math.toRadians(degree)));
		this.addBullet(tempBullet);
	}

	//Updates the position of the bullet so that if it goes beyond the limits of the screen it is removed 
	  public void updateBulletsPos() {
	    	for(int i = 0; i < bullets.size(); i++) {
	    		tempBullet = bullets.get(i);
	    		if(tempBullet.getPy() < 3 || tempBullet.getPy() > 592|| tempBullet.getPx() > 592 || tempBullet.getPx() < 3) {
	    			removeBullet(tempBullet); 
	    		}
	    		tempBullet.updatePos(); 
	    	}
	   } 
	  
	  //Adds a bullet to the linkedlist
	  public void addBullet(Bullet block) {
	    	bullets.add(block);
	    }
	  
	  //Removes a bullet from the linkedlist 
	    public void removeBullet(Bullet block) {
	    	bullets.remove(block);
	    }
	    
	    //Returns the linkedlist 
	    public LinkedList<Bullet> getBullets() {
	    	return bullets;
	    }
	    
	    @Override
	    public void draw(Graphics g) {
	    	if(this.getIsHit()) {
	    		 g.setColor(new Color(255, 0 , 0, 128));
	    		 g.fillOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 15, (int) this.getHeight() - 15);
	    		 g.setColor(Color.BLACK);
	    		 g.drawOval((int)this.getPx(), (int) this.getPy(), (int) this.getWidth() - 15, (int) this.getHeight() - 15);
	    		 for(int i = 0; i < bullets.size(); i++) {
	 	    		tempBullet = bullets.get(i); 
	 	    		tempBullet.draw(g); 
	 	    	}    		 
	    	}else {
	    	g.drawImage(ship, (int)this.getPx(), (int) this.getPy(), (int) this.getWidth(), (int) this.getHeight(), null); 
	    	for(int i = 0; i < bullets.size(); i++) {
	    		tempBullet = bullets.get(i); 
	    		tempBullet.draw(g); 
	    	}
	    	}	  
	    }
}
