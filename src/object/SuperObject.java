package object;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int SCALE = 1;
    public String[] prompts = new String[5];

    public void setSolidArea(int x, int y, int width, int height) {
        solidArea.x = x;
        solidArea.y = y;
        solidArea.width = width;
        solidArea.height = height;
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        double zoomFactor = gp.zoomFactor; // Get the zoom factor from the GamePanel
        double scaledTileSize = gp.tileSize *  SCALE;

        // Calculate screen coordinates
        double screenX = (worldX - gp.player.worldX + gp.player.screenX) * zoomFactor;
        double screenY = (worldY - gp.player.worldY + gp.player.screenY) * zoomFactor;

        // Check if the tile is within the screen bounds before drawing
        if (screenX + scaledTileSize > 0 && screenX < gp.screenWidth &&
                screenY + scaledTileSize > 0 && screenY < gp.screenHeight) {

            // Draw the tile on the screen
            g2.drawImage(image, (int) screenX, (int) screenY, (int) scaledTileSize, (int) scaledTileSize, null);
        }


        if (gp.DEBUG_MODE) {
            // Draw the solid area border
            g2.setColor(Color.GRAY); // Set the border color
            g2.drawRect((int) screenX, (int) screenY, (int) scaledTileSize, (int) scaledTileSize); // Draw the border
            g2.setColor(Color.RED); // Set the border color
            g2.drawRect((int) screenX + solidArea.x, (int) screenY +  solidArea.y, (int) solidArea.width, (int) solidArea.height);
            // Set font for coordinates
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(Color.WHITE);

            // Position the text slightly above the object
            int textX = (int) screenX;
            int textY = (int) (screenY - 5); // Adjust the y position to be above the object

            // Draw the coordinates text
            g2.drawString("x: " + solidArea.x + " y: " + solidArea.y, textX, textY - 30);
            g2.drawString("size: " + scaledTileSize + "x" + scaledTileSize, textX, textY - 15);
            g2.drawString("x: " + worldX + " y: " + worldY, textX, textY);
        }
    }
}
