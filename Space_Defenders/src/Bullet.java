
import java.awt.*;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and that all objects
 * created by this constructor share the same image data (i.e. img is static). This is important for
 * efficiency: your program will go very slowly if you try to create a new BufferedImage every time
 * the draw method is invoked.
 */
//Instantiates a Bullet Object
public class Bullet extends GameObj{
	private Color color; 
    public Bullet(int xpos, int ypos, int courtWidth, int courtHeight, Color color, int size) {
    	super(0, 0, xpos, ypos, size, size, courtWidth, courtHeight);
    	this.color  = color; 
    	
    }
    //updates the position of the object using its current velocity
    public void updatePos() {
    	this.setPy(this.getPy() + this.getVy());
    	this.setPx(this.getPx() + this.getVx());
    }
    
    //Draws a bullet 
    public void draw(Graphics g) {
    	g.setColor(this.color);
        g.fillOval((int) this.getPx(), (int)this.getPy(), (int)this.getWidth(), (int)this.getHeight());
    }
}
