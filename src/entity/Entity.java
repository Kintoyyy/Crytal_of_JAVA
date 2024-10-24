package entity;

import object.SuperObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Entity {
        public double worldX, worldY;
        public double speed;

        public Rectangle solidArea;
        public Rectangle proximityArea;
        public Rectangle spriteArea;
        public boolean collissionOn = false;
        public boolean isNearbyObject = false;
        public int proximityAreaRadius;

        public List<SuperObject> objectNearby = new ArrayList<>();

        public String direction;

        public int getCenterX(){
                return (int) (worldX + ( (double) solidArea.x / 2)) - 32;
        }

        public int getCenterY(){
                return (int) (worldY + ( (double) solidArea.y / 2)) - 32;
        }
}