package main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.setResizable(false);
        window.setTitle("Crytal of Java");
        window.setVisible(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = window.getSize();
                gamePanel.adjustSize(newSize.width, newSize.height);
            }
        });

        window.setSize(1400, 1000);
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
