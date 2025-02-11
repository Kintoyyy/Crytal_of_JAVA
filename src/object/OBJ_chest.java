package object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OBJ_chest extends SuperObject{
    public OBJ_chest(){
        name = "Chest";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Outdoor decoration/Chest.png")));
        }catch(IOException e){
            e.printStackTrace();
        }
        collision = true;
        isInteractable = true;
    }
}
