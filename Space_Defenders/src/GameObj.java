/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.Graphics;

import javax.swing.JPanel;

/** 
 * An object in the game. 
 *
 * Game objects exist in the game court. They have a position, velocity, size and bounds. Their
 * velocity controls how they move; their position should always be within their bounds.
 */
public abstract class GameObj{
    /*
     * Current position of the object (in terms of graphics coordinates)
     *  
     * Coordinates are given by the upper-left hand corner of the object. This position should
     * always be within bounds.
     *  0 <= px <= maxX 
     *  0 <= py <= maxY 
     */
    private double px; 
    private double py;

    /* Size of object, in pixels. */
    private double width;
    private double height;

    /* Velocity: number of pixels to move every time move() is called. */
    private double vx;
    private double vy;
    private double accX = 0;
    private double accY = 0;
    private double maxVelocity = 3;
    private double minVelocity = -3;

    /* 
     * Upper bounds of the area in which the object can be positioned. Maximum permissible x, y
     * positions for the upper-left hand corner of the object.
     */
    private double maxX;
    private double maxY;

    /**
     * Constructor
     */
    public GameObj(double vx, double vy, double px, double py, double width, double height, double courtWidth,
        double courtHeight) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width  = width;
        this.height = height;

        // take the width and height doubleo account when setting the bounds for the upper left corner
        // of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    /*** GETTERS **********************************************************************************/
    public double getPx() {
        return this.px;
    }

    public double getPy() {
        return this.py;
    }
    
    public double getVx() {
        return this.vx;
    }
    
    public double getVy() {
        return this.vy;
    }
    
   // @Override
    
    public double getWidth() {
        return this.width;
    }
   // @Override
    public double getHeight() {
        return this.height;
    }

    public double getAccX() {
    	return this.accX; 
    }
    public double getAccY() {
    	return this.accY; 
    }
    /*** SETTERS **********************************************************************************/
    public void setPx(double px) {
        this.px = px;
        clip();
    }

    public void setPy(double py) {
        this.py = py;
        clip();
    }

    public void setVx(double vx) {
        this.vx = vx; 
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
    
    //getters and setters for acceleration
    public void setAccX(double acc) {
    	this.accX = acc; 
    }
    public void setAccY(double acc) {
    	this.accY = acc; 
    }

    /*** UPDATES AND OTHER METHODS ****************************************************************/

    /**
     * Prevents the object from going outside of the bounds of the area designated for the object.
     * (i.e. Object cannot go outside of the active area the user defines for it).
     */ 
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }
    
    //This ensures that the velocity of the player is within the max and minimum bounds
    private void clip2() {
        this.vx = Math.min(Math.max(this.vx, minVelocity), this.maxVelocity);
        this.vy = Math.min(Math.max(this.vy, minVelocity), this.maxVelocity);
    }
    
   //this updates the velocity for the player 
   public void updateVelocity() {
		this.vx += this.accX; 
		this.vy += this.accY; 
		clip2();
    }

    /**
     * Moves the object by its velocity.  Ensures that the object does not go outside its bounds by
     * clipping.
     */
    public void move() {
        this.px += this.vx;
        this.py += this.vy;
        clip();
    }

    /**
     * Determine whether this game object is currently doubleersecting another object.
     * 
     * doubleersection is determined by comparing bounding boxes. If the bounding boxes overlap, then
     * an doubleersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether this object doubleersects the other object.
     */
    //made this method a little more accurate 
    public boolean Intersects(GameObj that) {
        return (this.px - 5 + this.width >= that.px
            && this.py - 5 + this.height >= that.py
            && that.px - 5  + that.width >= this.px 
            && that.py - 5 + that.height >= this.py);
    }


    /**
     * Determine whether this game object will doubleersect another in the next time step, assuming
     * that both objects continue with their current velocity.
     * 
     * doubleersection is determined by comparing bounding boxes. If the  bounding boxes (for the next
     * time step) overlap, then an doubleersection is considered to occur.
     * 
     * @param that The other object
     * @return Whether an doubleersection will occur.
     */
    public boolean willIntersect(GameObj that) {
        double thisNextX = (double) (this.px + this.vx);
        double thisNextY = (double) (this.py + this.vy);
        double thatNextX =  (double) (that.px + that.vx);
        double thatNextY =  (double) (that.py + that.vy);
    
        return (thisNextX + this.width >= thatNextX
            && thisNextY + this.height >= thatNextY
            && thatNextX + that.width >= thisNextX 
            && thatNextY + that.height >= thisNextY);
    }


    /**
     * Update the velocity of the object in response to hitting an obstacle in the given direction.
     * If the direction is null, this method has no effect on the object.
     *
     * @param d The direction in which this object hit an obstacle
     */
    public void bounce(Direction d) {
        if (d == null) return;
        
        switch (d) {
        case UP:
            this.vy = Math.abs(this.vy);
            break;  
        case DOWN:
            this.vy = -Math.abs(this.vy);
            break;
        case LEFT:
            this.vx = Math.abs(this.vx);
            break;
        case RIGHT:
            this.vx = -Math.abs(this.vx);
            break;
        }
    }

    /**
     * Determine whether the game object will hit a wall in the next time step. If so, return the
     * direction of the wall in relation to this game object.
     *  
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.px + this.vx < 0) {
            return Direction.LEFT;
        } else if (this.px + this.vx > this.maxX) {
           return Direction.RIGHT;
        }

        if (this.py + this.vy < 0) {
            return Direction.UP;
        } else if (this.py + this.vy > this.maxY) {
            return Direction.DOWN;
        } else {
            return null;
        }
    }

    /**
     * Determine whether the game object will hit another object in the next time step. If so,
     * return the direction of the other object in relation to this game object.
     * 
     * @param that The other object
     * @return Direction of impending object, null if all clear.
     */
    public Direction hitObj(GameObj that) {
        if (this.willIntersect(that)) {
            double dx = that.px + that.width / 2 - (this.px + this.width / 2);
            double dy = that.py + that.height / 2 - (this.py + this.height / 2);

            double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy *dy)));
            double diagTheta = Math.atan2(this.height / 2, this.width / 2);

            if (theta <= diagTheta) {
                return Direction.RIGHT;
            } else if (theta > diagTheta && theta <= Math.PI - diagTheta) {
                // Coordinate system for GUIs is switched
                if (dy > 0) {
                    return Direction.DOWN;
                } else {
                    return Direction.UP;
                }
            } else {
                return Direction.LEFT;
            }
        } else {
            return null;
        }
    }

    /**
     * Default draw method that provides how the object should be drawn in the GUI. This method does
     * not draw anything. Subclass should override this method based on how their object should
     * appear.
     * 
     * @param g The <code>Graphics</code> context used for drawing the object. Remember graphics
     * contexts that we used in OCaml, it gives the context in which the object should be drawn (a
     * canvas, a frame, etc.)
     */
    public abstract void draw(Graphics g);
}