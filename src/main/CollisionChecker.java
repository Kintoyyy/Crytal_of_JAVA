package main;

import entity.Entity;
import object.SuperObject;

import java.awt.*;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = (int) (entity.worldX + entity.solidArea.x);
        int entityRightWorldX = (int) (entity.worldX + entity.solidArea.x + entity.solidArea.width);
        int entityTopWorldY = (int) (entity.worldY + entity.solidArea.y);
        int entityBottomWorldY = (int) (entity.worldY + entity.solidArea.y + entity.solidArea.height);

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (int) ((entityTopWorldY - entity.speed) / gp.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collissionOn = true; // Collision detected
                }
                break;

            case "down":
                entityBottomRow = (int) ((entityBottomWorldY + entity.speed) / gp.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collissionOn = true; // Collision detected
                }
                break;

            case "left":
                entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collissionOn = true; // Collision detected
                }
                break;

            case "right":
                entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collissionOn = true; // Collision detected
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        // Store the original solid area coordinates
        int originalSolidAreaX = entity.solidArea.x;
        int originalSolidAreaY = entity.solidArea.y;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                // Update the solidArea coordinates of the current object
                int objSolidAreaX = gp.obj[i].solidArea.x + gp.obj[i].worldX;
                int objSolidAreaY = gp.obj[i].solidArea.y + gp.obj[i].worldY;

                // Check the entity's direction and calculate temporary coordinates
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y = originalSolidAreaY - (int) entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y = originalSolidAreaY + (int) entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x = originalSolidAreaX - (int) entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x = originalSolidAreaX + (int) entity.speed;
                        break;
                }

                // Update the solidArea coordinates with the object's position
                entity.solidArea.x += (int) entity.worldX;
                entity.solidArea.y += (int) entity.worldY;

                // Check for intersection
                if (entity.solidArea.intersects(new Rectangle(objSolidAreaX, objSolidAreaY, gp.obj[i].solidArea.width, gp.obj[i].solidArea.height))) {
                    if (gp.obj[i].collision) {
                        entity.collissionOn = true;
//                        System.out.println(gp.obj[i].name);
                    }
                    if (player) {
                        index = i;
                    }
                }

                // Reset entity's solidArea coordinates
                entity.solidArea.x = originalSolidAreaX;
                entity.solidArea.y = originalSolidAreaY;
            }
        }

        return index;
    }

    public int checkRaduisObject(Entity entity, boolean player) {
        int index = -1;

        // Precompute the entity's center position and proximity threshold
        int entityCenterX = entity.getCenterX();
        int entityCenterY = entity.getCenterY();
        double proximityThreshold = entity.proximityAreaRadius / 2.0;

        for (int i = 0; i < gp.obj.length; i++) {
            SuperObject obj = gp.obj[i]; // Cache gp.obj[i] in a local variable
            if (obj == null) {
                continue; // Skip null objects early
            }

            // Calculate the object's center position
            int objCenterX = obj.getCenterX();
            int objCenterY = obj.getCenterY();

            // Calculate the Euclidean distance
            double distance = Math.hypot(entityCenterX - objCenterX, entityCenterY - objCenterY);

            if (gp.DEBUG_MODE) {
                obj.debugDistance = distance;
            }

            // Check if the object is within proximity and is interactable
            if (distance < proximityThreshold && obj.isInteractable) {
                entity.isNearbyObject = true;

                if (player) {
                    if (!entity.objectNearby.contains(obj)) {
                        entity.objectNearby.add(obj); // Add only if not already in the list
                    }
                    index = i; // Store the index of the nearby object
                }
                return index; // Early return when a nearby object is found
            }

            // Remove the object from the nearby list if it's no longer in range
            entity.objectNearby.remove(obj);
        }

        entity.isNearbyObject = false;
        return index;
    }


    public int checkNearbyObject(Entity entity, boolean player, int proximityRadius) {
        int index = -1; // Default: no nearby object found

        // Get the entity's center position
        int entityCenterX = (int) (entity.worldX + ((double) entity.proximityArea.width / 2));
        int entityCenterY = (int) (entity.worldY + ((double) entity.proximityArea.height / 2));

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {
                // Calculate the object's center position
                int objCenterX = gp.obj[i].worldX + (gp.obj[i].solidArea.width / 2);
                int objCenterY = gp.obj[i].worldY + (gp.obj[i].solidArea.height / 2);

                // Calculate the Euclidean distance between entity and object
                double distance = Math.hypot(entityCenterX - objCenterX, entityCenterY - objCenterY);

                if (gp.DEBUG_MODE) {
                    gp.obj[i].debugDistance = distance;
                }

                // Check if the object is within the proximity radius
                if (distance <= proximityRadius) {
                    if (gp.obj[i].collision) {
                        entity.collissionOn = true;
                        entity.isNearbyObject = true;
                    }
                    if (player) {
                        index = i; // Store the index of the nearby object
                    }
                    break; // Exit the loop as soon as a nearby object is found
                }
            }
        }

        return index;
    }

}
