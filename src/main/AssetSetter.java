package main;

import object.OBJ_Tree;
import object.OBJ_chest;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_chest();
        gp.obj[0].worldX = 10 * gp.tileSize;
        gp.obj[0].worldY = 10 * gp.tileSize;

        gp.obj[1] = new OBJ_chest();
        gp.obj[1].worldX = 20 * gp.tileSize;
        gp.obj[1].worldY = 20 * gp.tileSize;

        gp.obj[1] = new OBJ_Tree();
        gp.obj[1].worldX = 5 * gp.tileSize;
        gp.obj[1].worldY = 5 * gp.tileSize;
    }
}
