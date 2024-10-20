package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class SuperNpc {
    public BufferedImage spriteSheet;
    public String name;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int SCALE = 2;

    public boolean moving = false;
    public int direction = 1; // 0: up, 1: down, 2: left, 3: right

    // Animations
    private Animation movementAnimation;
    private Animation idleAnimation;

    // SET COLLISION AREA
    public void setSolidArea(int x, int y, int width, int height) {
        solidArea.x = x * SCALE;
        solidArea.y = y * SCALE;
        solidArea.width = width * SCALE;
        solidArea.height = height * SCALE;
    }

    public void loadPlayerImages() {
        if (spriteSheet == null) {
            System.err.println("Sprite sheet is not set.");
            return;
        }

        int spriteWidth = 32;
        int spriteHeight = 32;
        int frameCount = 6;

        // Load movement frames
        BufferedImage[][] movementFrames = new BufferedImage[4][frameCount];
        BufferedImage[][] idleFrames = new BufferedImage[4][frameCount];

        for (int i = 0; i < frameCount; i++) {
            movementFrames[0][i] = cropImage(spriteSheet, i, 5, spriteWidth, spriteHeight); // Up
            movementFrames[1][i] = cropImage(spriteSheet, i, 3, spriteWidth, spriteHeight); // Down
            movementFrames[2][i] = flipX(cropImage(spriteSheet, i, 4, spriteWidth, spriteHeight)); // Left
            movementFrames[3][i] = cropImage(spriteSheet, i, 4, spriteWidth, spriteHeight); // Right

            idleFrames[0][i] = cropImage(spriteSheet, i, 2, spriteWidth, spriteHeight); // Idle Up
            idleFrames[1][i] = cropImage(spriteSheet, i, 0, spriteWidth, spriteHeight); // Idle Down
            idleFrames[2][i] = flipX(cropImage(spriteSheet, i, 1, spriteWidth, spriteHeight)); // Idle Left
            idleFrames[3][i] = cropImage(spriteSheet, i, 1, spriteWidth, spriteHeight); // Idle Right
        }

        // Initialize animations with frame speed
        movementAnimation = new Animation(movementFrames, 4);
        idleAnimation = new Animation(idleFrames, 12);
    }

    private BufferedImage cropImage(BufferedImage sheet, int col, int row, int width, int height) {
        return sheet.getSubimage(col * width, row * height, width, height);
    }

    public BufferedImage flipX(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.scale(-1, 1); // Flip horizontally
        transform.translate(-image.getWidth(), 0); // Reposition the image

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public void update() {
        if (moving) {
            movementAnimation.update();
        } else {
            idleAnimation.update();
        }
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        update(); // Update the animation state

        double zoomFactor = gp.zoomFactor;
        double scaledTileSize = gp.tileSize * SCALE;

        double screenX = (worldX - gp.player.worldX + gp.player.screenX) * zoomFactor;
        double screenY = (worldY - gp.player.worldY + gp.player.screenY) * zoomFactor;

        BufferedImage currentFrame;
        if (moving) {
            currentFrame = movementAnimation.getCurrentFrame(direction);
        } else {
            currentFrame = idleAnimation.getCurrentFrame(direction);
        }

        if (screenX + scaledTileSize > 0 && screenX < gp.screenWidth &&
                screenY + scaledTileSize > 0 && screenY < gp.screenHeight) {
            g2.drawImage(currentFrame, (int) screenX, (int) screenY, (int) scaledTileSize, (int) scaledTileSize, null);
        }

        if (gp.DEBUG_MODE) {
            g2.setColor(Color.GRAY);
            g2.drawRect((int) screenX, (int) screenY, (int) scaledTileSize, (int) scaledTileSize);


            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(Color.WHITE);

            int textX = (int) screenX;
            int textY = (int) (screenY - 5);

            g2.drawString("x: " + solidArea.x + " y: " + solidArea.y, textX, textY - 30);
            g2.drawString("size: " + scaledTileSize + "x" + scaledTileSize, textX, textY - 15);
            g2.drawString("x: " + worldX + " y: " + worldY, textX, textY);
        }
    }
}
