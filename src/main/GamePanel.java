package main;

import entity.Player;
import entity.SuperNpc;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    public final boolean DEBUG_MODE = true;
    public final boolean ENABLE_ZOOM = false;

    final int ORIGINAL_TILE_SIZE = 16;
    final int MIN_TILE_SIZE = 48;
    final int MAX_TILE_SIZE = 100;
    final int SCALE = 3;


    public int tileSize = ORIGINAL_TILE_SIZE * SCALE;
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = maxScreenCol * tileSize;
    public int screenHeight = maxScreenRow * tileSize;


    public double zoomFactor = 1.0; // Initialize zoom factor
    private final int FPS = 60;

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;

    TileManager tileM = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;

    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this,this.keyHandler);

    // SET OBJECTS
    public AssetSetter assetSetter = new AssetSetter(this);
    public SuperObject obj[] = new SuperObject[100];

    // SET NPC
    public NpcSetter npcSetter = new NpcSetter(this);
    public SuperNpc npc[] = new SuperNpc[10];


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(0,149,233));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame(){
        assetSetter.setObject();
        npcSetter.setObject();
    }

    public void startGameThread() {
        this.requestFocusInWindow(); // Ensure focus for key events
        gameThread = new Thread(this);
        gameThread.start();
    }



    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);

        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].draw(g2, this);
            }
        }

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }
        player.draw(g2);

        g2.dispose();
    }

    public void zoomInOut(int i) {
        if(!ENABLE_ZOOM){
            return;
        }
//        int oldTileSize = tileSize; // Store the old tile size
//        tileSize += i;
//
//        // Set a minimum tile size if needed
//        System.out.println("tile Size = " + tileSize + " Min" + MIN_TILE_SIZE);
//        if (tileSize < MIN_TILE_SIZE) {
//            tileSize = MIN_TILE_SIZE;
//        }
//
//        if (tileSize > MAX_TILE_SIZE) {
//            tileSize = MAX_TILE_SIZE;
//        }
//
//        // Calculate new player speed based on the new tile size
//        int newWorldWidth = tileSize * maxWorldCol;
//        player.speed = (double) newWorldWidth / 600;
//
//        // Calculate the zoom factor
//        double zoomFactor = (double) tileSize / oldTileSize;
//
//        // Adjust player's world position based on the zoom factor
//        player.worldX = player.worldX * zoomFactor;
//        player.worldY = player.worldY * zoomFactor;
//
//        // Update zoom factor for rendering
//        this.zoomFactor = (double) tileSize / ORIGINAL_TILE_SIZE; // Define ORIGINAL_TILE_SIZE for your base zoom level
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS; // Corrected division
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime = System.nanoTime() + drawInterval; // Update nextDrawTime correctly

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
