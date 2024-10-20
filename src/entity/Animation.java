package entity;

import java.awt.image.BufferedImage;

public class Animation {

    private final BufferedImage[][] frames; // 2D array of animation frames [direction][frame]
    private final int frameCount;
    private int frameIndex = 0;
    private int frameCounter = 0;
    private final int frameSpeed; // Speed for switching frames

    public Animation(BufferedImage[][] frames, int frameSpeed) {
        this.frames = frames;
        this.frameCount = frames[0].length;
        this.frameSpeed = frameSpeed;
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= frameSpeed) {
            frameCounter = 0;
            frameIndex = (frameIndex + 1) % frameCount; // Loop through frames
        }
    }

    public BufferedImage getCurrentFrame(int direction) {
        return frames[direction][frameIndex];
    }

    public void reset() {
        frameIndex = 0;
        frameCounter = 0;
    }
}
