package helper;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class TextManager {
    private static final int FRAME_COUNT = 6;
    private final BufferedImage[][] frames = new BufferedImage[4][FRAME_COUNT]; // 0: up, 1: down, 2: left, 3: right
    private final BufferedImage[][] idleFrames = new BufferedImage[4][FRAME_COUNT]; // Same for idle

    private int frameCounter = 0;
    private int frameIndex = 0;
    public boolean moving = false;
    public int direction = 1; // 0: up, 1: down, 2: left, 3: right

    public void loadPlayerImages() {
        BufferedImage spriteSheet = null;

        if (spriteSheet == null) {
            System.err.println("Sprite sheet is not set.");
            return;  // Avoid loading images if spriteSheet is null.
        }

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
    }

    private BufferedImage cropImage(BufferedImage sheet, int col, int row, int width, int height) {
        return sheet.getSubimage(col * width, row * height, width, height);
    }

    public BufferedImage flipX(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.scale(-1, 1);  // Flip horizontally
        transform.translate(-image.getWidth(), 0);  // Reposition the image

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    private void updateAnimation() {
        frameCounter++;
        int frameSpeed = 4;
        int idleFrameSpeed = 12;
        if (moving && frameCounter >= frameSpeed) {
            frameCounter = 0;

            frameIndex = (frameIndex + 1) % FRAME_COUNT; // Loop through movement frames
        } else if (!moving && frameCounter >= idleFrameSpeed) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % FRAME_COUNT; // Loop through idle frames
        }
    }
}
