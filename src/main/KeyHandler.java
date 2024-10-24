package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, fPressed;
    GamePanel gp;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (key == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_F) {
            fPressed = true;
        }
        if (key == KeyEvent.VK_UP) {
            gp.zoomInOut(1);
        }
        if (key == KeyEvent.VK_DOWN) {
            gp.zoomInOut(-1);
        }
        if (key == KeyEvent.VK_F3) {
            gp.DEBUG_MODE = !gp.DEBUG_MODE;
        }
//        if (key == KeyEvent.VK_F4) {
//            gp.ENABLE_ZOOM = !gp.ENABLE_ZOOM;
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            upPressed = false;
        } else if (key == KeyEvent.VK_S) {
            downPressed = false;
        } else if (key == KeyEvent.VK_A) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_D) {
            rightPressed = false;
        } else if (key == KeyEvent.VK_F) {
            fPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
