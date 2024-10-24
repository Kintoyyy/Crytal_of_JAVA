package entity;

import main.GamePanel;
import main.KeyHandler;
import object.SuperObject;
import utils.EntityAnimation;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class Player extends Entity {
    private static final float DIAGONAL_SPEED = 0.7571F; // 1 / √2
    private static final int PLAYER_SCALE = 2;

    private final GamePanel gp;
    private final KeyHandler keyHandler;

    public boolean moving = false;
    public boolean attacking = false;

    public final int screenX;
    public final int screenY;

    public final int SolidAreaWidth = 10 * PLAYER_SCALE;
    public final int SolidAreaHeight = 8 * PLAYER_SCALE;
    public final int spriteAreaSize = 32 * PLAYER_SCALE;

    public EntityAnimation animation = new EntityAnimation("/player/Kent.png");

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        setDefaultValues();
        animation.loadAnimations();
    }

    private void setDefaultValues() {
        worldX = gp.tileSize * 12;
        worldY = gp.tileSize * 12;
        speed = (double) gp.worldWidth / 600; // Define speed
        proximityAreaRadius = 128 * PLAYER_SCALE;

        direction = "down"; // Default direction

        solidArea = new Rectangle(SolidAreaWidth, SolidAreaHeight * 2, SolidAreaWidth, SolidAreaHeight);

        spriteArea = new Rectangle(0, 0 , spriteAreaSize, spriteAreaSize);

        proximityArea = new Rectangle( (spriteAreaSize / 2) -(proximityAreaRadius / 2),(spriteAreaSize / 2) -(proximityAreaRadius / 2) , proximityAreaRadius, proximityAreaRadius);
    }

    private void handleDiagonalMovement(int dx, int dy) {
        if (Math.abs(dx) > 0 && Math.abs(dy) > 0) {
            dx = (int) (dx * DIAGONAL_SPEED);
            dy = (int) (dy * DIAGONAL_SPEED);
        }
        worldX += dx;
        worldY += dy;
    }

    public void update() {
        moving = false;
        int dx = 0;
        int dy = 0;

        attacking = false;

        if (keyHandler.upPressed) {
            dy -= (int) speed;
            moving = true;
            direction = "up";
        }
        if (keyHandler.downPressed) {
            dy += (int) speed;
            moving = true;
            direction = "down";
        }
        if (keyHandler.leftPressed) {
            dx -= (int) speed;
            moving = true;
            direction = "left";
        }
        if (keyHandler.rightPressed) {
            dx += (int) speed;
            moving = true;
            direction = "right";
        }

        if (keyHandler.fPressed) {
            attacking = true;
        }

        // Check for collisions before moving
        collissionOn = false; // Reset collision status
        gp.collisionChecker.checkTile(this); // Check for collision with tiles

        int objIndex = gp.collisionChecker.checkObject(this, true);

        int objNearby = gp.collisionChecker.checkRaduisObject(this, true);

        if(objIndex < 10){
            System.out.println( gp.obj[objIndex].prompts[1] );
        }

        //        int objectNear = gp.collisionChecker.checkNearbyObject(this, true, 5);
//        System.out.println(objectNear);

        if (!collissionOn) {
            handleDiagonalMovement(dx, dy); // Move if no collision
        }
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(animation.getImage((!moving ? (attacking ? 0 : 1) : 2), direction), screenX, screenY, gp.tileSize * PLAYER_SCALE, gp.tileSize * PLAYER_SCALE, null);

        for(SuperObject objects : objectNearby){
            g2.drawLine(gp.getScreenCenterX(), gp.getScreenCenterY(),objects.getCenterX(),objects.getCenterY());
        }


        showObjectCenter(g2, this.SolidAreaHeight, this.SolidAreaWidth);

        showDisplayCenter(g2, 200);



        if (gp.DEBUG_MODE) { // Assuming you have a DEBUG_MODE boolean in your GamePanel
//            g2.setColor(Color.RED); // Set the border color
//            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height); // Draw the solidArea

//            g2.setColor(Color.GREEN);
//            g2.drawRect(screenX + spriteArea.x, screenY + spriteArea.y, spriteArea.width, spriteArea.height);

            g2.setColor(isNearbyObject ? Color.BLUE : Color.cyan); // Set the border color
            g2.drawOval(screenX + proximityArea.x, screenY + proximityArea.y, proximityArea.width, proximityArea.height );


            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(Color.WHITE);

            g2.drawString("worldX: " + getCenterX() ,50,20);
            g2.drawString("objectNearby: " + isNearbyObject,50,35);
            g2.drawString("direction: " + direction ,50,50);
            g2.drawString("worldX: " + worldX + " worldY: " + worldY,50,65);
            g2.drawString("collision: " + collissionOn,50,80);
            g2.drawString("screenX: " + screenX + " screenY" + screenY,50,95);

            g2.drawString("tileSize: " + gp.tileSize,50,110);
            g2.drawString("tileSize: " + solidArea,50,125);

            g2.drawString("Objects Nearby: " + objectNearby,50,140);
        }
    }

    private void showObjectCenter(Graphics2D g2, int height, int width) {
        // Calculate the screen's center position based on object dimensions
        int centerX = gp.getScreenCenterX() + (width / 2);
        int centerY = gp.getScreenCenterY() + (height / 2);

        // Set the color for the crosshair (center indicator)
        g2.setColor(Color.YELLOW);

        // Dynamically calculate the line length based on object dimensions
        int lineLengthX = width / 4;  // Make the horizontal line 1/4th of the width
        int lineLengthY = height / 4; // Make the vertical line 1/4th of the height

        // Draw horizontal and vertical lines crossing at the object's center
        g2.drawLine(centerX - lineLengthX, centerY, centerX + lineLengthX, centerY);  // Horizontal line
        g2.drawLine(centerX, centerY - lineLengthY, centerX, centerY + lineLengthY);  // Vertical line
    }



    private void showDisplayCenter(Graphics2D g2, int size){
        g2.setColor(Color.YELLOW); // Set the border color
        g2.drawLine(gp.getScreenCenterX() - size, gp.getScreenCenterY(),gp.getScreenCenterX() + size, gp.getScreenCenterY());
        g2.drawLine(gp.getScreenCenterX(), gp.getScreenCenterY() - size,gp.getScreenCenterX(), gp.getScreenCenterY() + size);
    }
}
