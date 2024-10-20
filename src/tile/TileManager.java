package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; // Array to store different tile types
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; // Create a tile map

        getTileImage(); // Load tile images
        loadMap("/maps/map01.txt"); // Initialize the map
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Grass_Middle.png")));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Path_Middle.png")));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Water_Middle.png")));
            tile[2].collision = true;

        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if (line != null) { // Check if the line is not null
                    String[] numbers = line.split(" ");

                    for (int col = 0; col < Math.min(gp.maxWorldCol, numbers.length); col++) {
                        int num = Integer.parseInt(numbers[col]);
                        // Ensure that the number fits within the tile range
                        if (num >= 0 && num < tile.length) {
                            mapTileNum[col][row] = num;
                        } else {
                            System.err.println("Tile index " + num + " is out of bounds for the tile array.");
                        }
                    }
                } else {
                    System.err.println("Line " + row + " is null, skipping.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        // Loop through the world rows and columns
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                int tileNum = mapTileNum[col][row]; // Get tile number from the map

                // Calculate world coordinates of the tile
                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;

                // Calculate the position on the screen
                double screenX = worldX - gp.player.worldX + gp.player.screenX;
                double screenY = worldY - gp.player.worldY + gp.player.screenY;

                // Check if the tile is within the screen bounds before drawing
                if (screenX + gp.tileSize > 0 && screenX < gp.screenWidth &&
                        screenY + gp.tileSize > 0 && screenY < gp.screenHeight) {

                    // Draw the tile on the screen
                    g2.drawImage(tile[tileNum].image, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }

}
