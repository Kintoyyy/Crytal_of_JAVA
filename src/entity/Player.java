package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    private static final int FRAME_COUNT = 6; // Number of frames for animations
    private static final float DIAGONAL_SPEED = 0.7071F; // 1 / √2
    private static final int PLAYER_SCALE = 2;

    private GamePanel gp;
    private KeyHandler keyHandler;

    private int frameCounter = 0;
    private int frameIndex = 0;
    private int frameSpeed = 4;
    private int idleFrameSpeed = 12;
    public boolean moving = false;

    public boolean inFrontOfObject = true;

    public final int screenX;
    public final int screenY;

    // Store frames for each direction
    private BufferedImage[][] frames = new BufferedImage[4][FRAME_COUNT]; // 0: up, 1: down, 2: left, 3: right
    private BufferedImage[][] idleFrames = new BufferedImage[4][FRAME_COUNT]; // Same for idle

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

//        solidArea = new Rectangle(20, 24, 8, 10);
        setDefaultValues();
        loadPlayerImages();
    }

    private void setDefaultValues() {
        // Initial location
        worldX = gp.tileSize * 8;
        worldY = gp.tileSize * 8;
        speed = gp.worldWidth / 600; // Define speed
        direction = "down"; // Default direction

        // Update solidArea for the new size (assuming you are scaling by 2)
        solidArea = new Rectangle(18* PLAYER_SCALE, 26* PLAYER_SCALE, 10 * PLAYER_SCALE, 8 * PLAYER_SCALE); // Center the solidArea
    }

    private void loadPlayerImages() {
        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/Kent.png")));
            int spriteWidth = 32;
            int spriteHeight = 32;

            for (int i = 0; i < FRAME_COUNT; i++) {
                frames[0][i] = cropImage(spriteSheet, i, 5, spriteWidth, spriteHeight); // Up
                frames[1][i] = cropImage(spriteSheet, i, 3, spriteWidth, spriteHeight); // Down
                frames[2][i] = flipX(cropImage(spriteSheet, i, 4, spriteWidth, spriteHeight)); // Left
                frames[3][i] = cropImage(spriteSheet, i, 4, spriteWidth, spriteHeight); // Right

                idleFrames[0][i] = cropImage(spriteSheet, i, 2, spriteWidth, spriteHeight); // Idle Up
                idleFrames[1][i] = cropImage(spriteSheet, i, 0, spriteWidth, spriteHeight); // Idle Down
                idleFrames[2][i] = flipX(cropImage(spriteSheet, i, 1, spriteWidth, spriteHeight)); // Idle Left
                idleFrames[3][i] = cropImage(spriteSheet, i, 1, spriteWidth, spriteHeight); // Idle Right
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage cropImage(BufferedImage sheet, int col, int row, int width, int height) {
        return sheet.getSubimage(col * width, row * height, width, height);
    }

    private void updateAnimation() {
        frameCounter++;
        if (moving && frameCounter >= frameSpeed) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % FRAME_COUNT; // Loop through frames for movement
        } else if (!moving && frameCounter >= idleFrameSpeed) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % FRAME_COUNT; // Loop through frames for idle
        }
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

        // Check for key presses and set dx/dy accordingly
        if (keyHandler.upPressed) {
            dy -= speed;
            moving = true;
            direction = "up";
        }
        if (keyHandler.downPressed) {
            dy += speed;
            moving = true;
            direction = "down";
        }
        if (keyHandler.leftPressed) {
            dx -= speed;
            moving = true;
            direction = "left";
        }
        if (keyHandler.rightPressed) {
            dx += speed;
            moving = true;
            direction = "right";
        }

        // Check for collisions before moving
        collissionOn = false; // Reset collision status
        gp.collisionChecker.checkTile(this); // Check for collision with tiles

        int objIndex = gp.collisionChecker.checkObject(this, true);

        if(objIndex < 10){
            System.out.println( gp.obj[objIndex].prompts[1] );

        }

        int npcIndex = gp.collisionChecker.checkNpc(this, true);
//        int objectNear = gp.collisionChecker.checkNearbyObject(this, true, 5);
//        System.out.println(objectNear);

        if (!collissionOn) {
            handleDiagonalMovement(dx, dy); // Move if no collision
        }

        updateAnimation(); // Update animation state
    }

    public void draw(Graphics2D g2) {
        BufferedImage image;
        int directionIndex = switch (direction) {
            case "up" -> 0;
            case "down" -> 1;
            case "left" -> 2;
            case "right" -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };

        image = moving ? frames[directionIndex][frameIndex] : idleFrames[directionIndex][frameIndex];

        // Scale the size as needed (e.g., 1.5 times the original size)
        g2.drawImage(image, screenX, screenY, gp.tileSize * PLAYER_SCALE, gp.tileSize * PLAYER_SCALE, null); // Draw the player image with scaling

        if (gp.DEBUG_MODE) { // Assuming you have a DEBUG_MODE boolean in your GamePanel
            g2.setColor(Color.RED); // Set the border color
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height); // Draw the solidArea
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(Color.WHITE);
            g2.drawString("direction: " + direction ,50,50);
            g2.drawString("x: " + worldX + " y: " + worldY,50,65);
            g2.drawString("collision: " + collissionOn,50,80);
            g2.drawString("screenX: " + screenX + " screenY" + screenY,50,95);
            g2.drawString("tileSize: " + gp.tileSize,50,110);
            g2.drawString("tileSize: " + solidArea,50,125);
        }
    }

}
