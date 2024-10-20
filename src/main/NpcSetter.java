package main;

import entity.NPC_1;
import entity.NPC_2;
import object.OBJ_Tree;
import object.OBJ_chest;

public class NpcSetter {
    GamePanel gp;

    public NpcSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject(){
        gp.npc[0] = new NPC_1();
        gp.npc[0].worldX = 3 * gp.tileSize;
        gp.npc[0].worldY = 3 * gp.tileSize;

//        gp.npc[0] = new NPC_2();
//        gp.npc[0].worldX = 3 * gp.tileSize;
//        gp.npc[0].worldY = 3 * gp.tileSize;
//
//        gp.obj[1] = new OBJ_chest();
//        gp.obj[1].worldX = 20 * gp.tileSize;
//        gp.obj[1].worldY = 20 * gp.tileSize;
//
//        gp.obj[1] = new OBJ_Tree();
//        gp.obj[1].worldX = 5 * gp.tileSize;
//        gp.obj[1].worldY = 5 * gp.tileSize;
    }
}
