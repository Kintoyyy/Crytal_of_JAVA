package entity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class NPC_2 extends SuperNpc {
    public NPC_2() {
        name = "NPC 1";
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/Enemies/Slime.png"),
                    "Resource not found"
            ));
            loadAnimationImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSolidArea(20, 26, 10 , 8 );
    }
}
