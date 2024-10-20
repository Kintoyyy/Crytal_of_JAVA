package entity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Entity {
        public double worldX, worldY;
        public double speed;

        // Changed spriteSheet to private to encapsulate it
        private BufferedImage spriteSheet;

        public Rectangle solidArea;
        public boolean collissionOn = false;

        public String direction;

        /**
         * Set the sprite sheet for this entity.
         * @param spriteSheet The BufferedImage containing all sprites.
         */
        public void setSpriteSheet(BufferedImage spriteSheet) {
                this.spriteSheet = spriteSheet;
        }

        /**
         * Crop a specific image from the sprite sheet.
         * @param col The column index of the sprite.
         * @param row The row index of the sprite.
         * @param width The width of the sprite.
         * @param height The height of the sprite.
         * @return The cropped BufferedImage.
         */
        public BufferedImage cropImage(int col, int row, int width, int height) {
                return spriteSheet.getSubimage(col * width, row * height, width, height);
        }

        /**
         * Flip a given image horizontally (X-axis).
         * @param image The BufferedImage to flip.
         * @return The flipped BufferedImage.
         */
        public BufferedImage flipX(BufferedImage image) {
                AffineTransform transform = new AffineTransform();
                transform.scale(-1, 1);  // Flip horizontally
                transform.translate(-image.getWidth(), 0);  // Reposition the image

                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                return op.filter(image, null);
        }

        /**
         * Flip a given image vertically (Y-axis).
         * @param image The BufferedImage to flip.
         * @return The flipped BufferedImage.
         */
        public BufferedImage flipY(BufferedImage image) {
                AffineTransform transform = new AffineTransform();
                transform.scale(1, -1);  // Flip vertically
                transform.translate(0, -image.getHeight());  // Reposition the image

                AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                return op.filter(image, null);
        }
}
