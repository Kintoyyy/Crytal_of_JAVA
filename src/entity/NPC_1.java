package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC_1 extends SuperNpc {
    public NPC_1() {
        name = "NPC 1";
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/Enemies/Skeleton.png"),
                    "Resource not found"
            ));
            loadPlayerImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSolidArea(20, 26, 10 , 8 );
    }

}
