package main;

import entity.Player;
import entity.SuperNpc;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public boolean DEBUG_MODE = true;
    public boolean ENABLE_ZOOM = false;

    final int ORIGINAL_TILE_SIZE = 32;
    final int MIN_TILE_SIZE = 48;
    final int MAX_TILE_SIZE = 100;
    final int SCALE = 2;

    public int canvasSizeX;
    public int canvasSizeY;

    public int tileSize = ORIGINAL_TILE_SIZE * SCALE;
    public int maxScreenCol;
    public int maxScreenRow;
    public int screenWidth;
    public int screenHeight;

    public double zoomFactor = 1.0;
    private final int FPS = 60;
    private double currentFPS = 0;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;

    TileManager tileM = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler(this);
    MouseHandler mouseHandler = new MouseHandler(this);
    Thread gameThread;

    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Player player = new Player(this, this.keyHandler, this.mouseHandler);
    public AssetSetter assetSetter = new AssetSetter(this);
    public SuperObject[] obj = new SuperObject[100];
    public NpcSetter npcSetter = new NpcSetter(this);
    public SuperNpc[] npc = new SuperNpc[10];

    public GamePanel() {
        calculateScreenDimensions(); // Initialize screen dimensions
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(0, 149, 233));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);
    }

    public void adjustSize(int width, int height) {
        this.canvasSizeX = width;
        this.canvasSizeY = height;
        player.screenX = canvasSizeX / 2;
        player.screenY = canvasSizeY / 2;
        calculateScreenDimensions(); // Recalculate screen dimensions on resize
        System.out.println("Adjusting game panel size to: Width = " + width + ", Height = " + height);
    }

    private void calculateScreenDimensions() {
        maxScreenCol = canvasSizeX / tileSize;
        maxScreenRow = canvasSizeY / tileSize;
        screenWidth = maxScreenCol * tileSize;
        screenHeight = maxScreenRow * tileSize;
    }

    public void setupGame() {
        assetSetter.setObject();
        npcSetter.setObject();
    }

    public void startGameThread() {
        this.requestFocusInWindow();
        gameThread = new Thread(this);
        gameThread.start();
    }

    public int getScreenCenterX() {
        return canvasSizeX / 2;
    }

    public int getScreenCenterY() {
        return canvasSizeY / 2;
    }

    public void handleMouseClick(int x, int y) {
        System.out.println("Mouse clicked at: " + x + ", " + y);
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        for (SuperNpc superNpc : npc) {
            if (superNpc != null) {
                superNpc.draw(g2, this);
            }
        }
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }
        player.draw(g2);

        g2.setColor(Color.WHITE);
        g2.drawString("FPS: " + String.format("%.2f", currentFPS), 10, 20);

        g2.dispose();
    }

    public void zoomInOut(int zoomAmount) {
        if (!ENABLE_ZOOM) return;

        int oldTileSize = tileSize;
        tileSize += zoomAmount;

        if (tileSize < MIN_TILE_SIZE) {
            tileSize = MIN_TILE_SIZE;
        } else if (tileSize > MAX_TILE_SIZE) {
            tileSize = MAX_TILE_SIZE;
        }

        // Update player speed based on new tile size
        player.speed = (double) (tileSize * maxWorldCol) / 600;

        // Calculate the zoom factor and adjust player position
        double zoomFactor = (double) tileSize / oldTileSize;
        player.worldX *= zoomFactor;
        player.worldY *= zoomFactor;

        this.zoomFactor = (double) tileSize / ORIGINAL_TILE_SIZE;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        long lastTime = System.nanoTime();

        while (gameThread != null) {
            update();
            repaint();

            long currentTime = System.nanoTime();
            long timePassed = currentTime - lastTime;
            lastTime = currentTime;

            currentFPS = 1000000000.0 / timePassed;

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = Math.max(remainingTime, 0) / 1000000;
                Thread.sleep((long) remainingTime);
                nextDrawTime = System.nanoTime() + drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
