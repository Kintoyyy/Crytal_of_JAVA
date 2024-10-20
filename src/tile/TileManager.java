package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        tile = new Tile[100]; // Array to store different tile types
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow]; // Create a tile map

        getTileImage(); // Load tile images
        loadMap("/maps/map02.txt"); // Initialize the map
    }

    public void getTileImage() {
        try {
            tile[3] = new Tile(ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Grass_Middle.png"))));

            tile[2] = new Tile(ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Path_Middle.png"))));

            tile[1] = new Tile(ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Water_Middle.png"))));
            tile[1].collision = true;

            BufferedImage beachTexture = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/tiles/Beach_Tile.png")));

            tile[10] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(1,1));

            tile[11] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(0,0));

            tile[12] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(1,0));

            tile[13] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(2,0));

            tile[14] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(0,1));

            tile[15] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(2,1));

            tile[16] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(0,2));

            tile[17] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(1,2));

            tile[18] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(2,2));

            tile[19] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(3,0));

            tile[20] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(4,0));

            tile[21] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(3,1));

            tile[22] = new Tile(new TileTexture(beachTexture , 5,3).GetTileResource(4,1));
        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        }
    }
//    private BufferedImage cropImage(BufferedImage sheet, int col, int row, int width, int height) {


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
        int tileNum;
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                tileNum = mapTileNum[col][row]; // Get tile number from the map

                // Check if the current tile is of type 2
//                if (tileNum == 2) {
//                    boolean hasTopSand = (row > 0 && mapTileNum[col][row - 1] == 2); // Check the tile above
//                    boolean hasBottomSand = (row < gp.maxWorldRow - 1 && mapTileNum[col][row + 1] == 2); // Check the tile below
//                    boolean hasLeftSand = (col > 0 && mapTileNum[col - 1][row] == 2); // Check the tile to the left
//                    boolean hasRightSand = (col < gp.maxWorldCol - 1 && mapTileNum[col + 1][row] == 2); // Check the tile to the right
//
//                    boolean hasTopWater = (row > 0 && mapTileNum[col][row - 1] == 1); // Check the tile above
//                    boolean hasBottomWater = (row < gp.maxWorldRow - 1 && mapTileNum[col][row + 1] == 1); // Check the tile below
//                    boolean hasLeftWater = (col > 0 && mapTileNum[col - 1][row] == 1); // Check the tile to the left
//                    boolean hasRightWater = (col < gp.maxWorldCol - 1 && mapTileNum[col + 1][row] == 1); // Check the tile to the right
//
//                    // Change tile number based on neighboring tiles
//                    if (hasTopSand && hasBottomSand && hasLeftWater && hasRightSand) {
//                        mapTileNum[col][row] = 14; // Replace current tile with 14
//                    }
//
//                    if (hasTopWater && hasBottomSand && hasLeftWater && hasRightSand) {
//                        mapTileNum[col][row] = 11; // Replace current tile with 14
//                    }
//
//                    if (hasTopSand && hasBottomWater && hasLeftWater && hasRightSand) {
//                        mapTileNum[col][row] = 16; // Replace current tile with 14
//                    }
//                }
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
