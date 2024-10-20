package tile;

import java.awt.image.BufferedImage;

public class TileTexture {
    public static BufferedImage image;
    public int width, height;
    public int tileWidth, tileHeight;

    public TileTexture(BufferedImage image, int col, int row) {
        TileTexture.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.tileWidth =  width / col;
        this.tileHeight = height / row;
    }

    public BufferedImage GetTileResource(int col, int row ) {
        return image.getSubimage(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
    }
}
