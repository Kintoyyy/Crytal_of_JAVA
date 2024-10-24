package utils;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static utils.EntityConstants.*;

public class EntityAnimation {
    private BufferedImage image;
    private BufferedImage[][] animationFrames;
    private int aniTick, aniIndex, aniSpeed = 6;
    private final int spriteH = 32, spriteW = 32;
    ImageUtils img = new ImageUtils();

    public EntityAnimation(String path) {
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAnimations() {
        animationFrames = new BufferedImage[10][6];
        for (int i = 0; i < animationFrames.length; i++) {
            for (int j = 0; j < animationFrames[i].length; j++) {
                animationFrames[i][j] = image.getSubimage(j * spriteW, i * spriteH, spriteW, spriteH);
            }
        }
    }

    private void updateAnimationTick(int index) {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(index)) {
                aniIndex = 0;
            }
        }
    }

    public BufferedImage getImage(int index) {
        return getImage(index, "");
    }

    public BufferedImage getImage(int index, String direction) {
        updateAnimationTick(index);

        int frameSet = getFrameSet(index, direction);
        BufferedImage frame = animationFrames[frameSet][aniIndex];

        if (LEFT.equals(direction)) {
            return img.flipX(frame);
        }
        return frame;
    }

    public BufferedImage getImage(int index, String direction, boolean isAttack) {
        updateAnimationTick(index);

        int frameSet = getFrameSet(index, direction);
        BufferedImage frame = animationFrames[frameSet][aniIndex];

        if (LEFT.equals(direction)) {
            return img.flipX(frame);
        }
        return frame;
    }


    private int getFrameSet(int index, String direction) {
        return switch (index) {
            case 0 -> switch (direction) {
                case UP -> 8;
                case DOWN -> 6;
                case LEFT, RIGHT -> 7;
                default -> 0;
            };
            case 1 -> switch (direction) {
                case UP -> 2;
                case DOWN -> 0;
                case LEFT, RIGHT -> 1;
                default -> 0;
            };
            case 2 -> switch (direction) {
                case UP -> 5;
                case DOWN -> 3;
                case LEFT, RIGHT -> 4;
                default -> 0;
            };

            default -> index;
        };
    }
}
