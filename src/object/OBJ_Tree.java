package object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Tree extends SuperObject{
    public OBJ_Tree(){
        name = "Tree";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Outdoor decoration/Oak_Tree.png")));
        }catch(IOException e){
            e.printStackTrace();
        }
        setSolidArea(80, 80, 28, 70);
        SCALE = 4;
        collision = true;
    }
}
